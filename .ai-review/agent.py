"""
agent.py — Pre-push AI review agent.

Akış:
  1. Push edilecek Kotlin diff'ini topla.
  2. Diff'ten anahtar sorgular çıkar.
  3. FAISS ile lokal KB'den ilgili kuralları çek.
  4. Claude'a structured (JSON) review yaptır.
  5. Severity 'critical' bulgu varsa exit 1 — push BLOK.

Tamamen lokal embedding (sentence-transformers) + Anthropic API kullanır.
"""

from __future__ import annotations

import json
import os
import subprocess
import sys
from pathlib import Path

import faiss
import numpy as np
from anthropic import Anthropic
from dotenv import load_dotenv
from sentence_transformers import SentenceTransformer

ROOT = Path(__file__).parent
load_dotenv(ROOT / ".env")

INDEX_PATH = ROOT / "kb.faiss"
META_PATH = ROOT / "kb.meta.json"

CLAUDE_MODEL = "claude-sonnet-4-6"
TOP_K = 6
MAX_DIFF_CHARS = 12000      # çok uzun diff'leri kırp (token bütçesi koruma)
MAX_QUERY_CHARS = 4000      # embedding query üst sınırı

# ANSI renkler — terminal çıktısı için
RED = "\033[31m"
YELLOW = "\033[33m"
GREEN = "\033[32m"
CYAN = "\033[36m"
BOLD = "\033[1m"
RESET = "\033[0m"


# ─────────────────────────────────────────────────────────────────────────────
# 1) GIT DIFF
# ─────────────────────────────────────────────────────────────────────────────

def get_kotlin_diff() -> str:
    """
    Push edilecek Kotlin değişikliklerini getirir.
    Önce upstream ile karşılaştırır; upstream yoksa son commit'e bakar.
    """
    # Upstream branch ile karşılaştır
    upstream_check = subprocess.run(
        ["git", "rev-parse", "--abbrev-ref", "@{u}"],
        capture_output=True, text=True
    )
    if upstream_check.returncode == 0:
        result = subprocess.run(
            ["git", "diff", "@{u}..HEAD", "--", "*.kt", "*.kts"],
            capture_output=True, text=True
        )
        if result.stdout.strip():
            return result.stdout

    # Upstream yok (ilk push) — son commit'e bak
    result = subprocess.run(
        ["git", "diff", "HEAD~1..HEAD", "--", "*.kt", "*.kts"],
        capture_output=True, text=True
    )
    return result.stdout


def extract_changed_summary(diff: str) -> str:
    """
    Diff'ten retrieval sorgusu için özet çıkarır:
    - eklenen satırların metni
    - dosya yolları
    Bu, ham diff'e göre çok daha iyi embedding sorgusu olur.
    """
    lines = []
    for line in diff.splitlines():
        if line.startswith("+++") or line.startswith("---"):
            continue
        if line.startswith("+") and not line.startswith("+++"):
            lines.append(line[1:])
        elif line.startswith("diff --git"):
            lines.append(line)
    summary = "\n".join(lines)
    return summary[:MAX_QUERY_CHARS]


# ─────────────────────────────────────────────────────────────────────────────
# 2) RETRIEVAL
# ─────────────────────────────────────────────────────────────────────────────

def retrieve(query: str, k: int = TOP_K) -> list[dict]:
    """FAISS index'inden lokal embedding ile en yakın k chunk'ı döndürür."""
    if not INDEX_PATH.exists() or not META_PATH.exists():
        raise SystemExit(
            f"❌ Index bulunamadı. Önce çalıştır: python {ROOT}/indexer.py"
        )

    meta = json.loads(META_PATH.read_text(encoding="utf-8"))
    model = SentenceTransformer(meta["model"])
    index = faiss.read_index(str(INDEX_PATH))

    q_emb = model.encode(
        [query],
        normalize_embeddings=True,
        convert_to_numpy=True,
    ).astype("float32")

    scores, idx = index.search(q_emb, min(k, len(meta["chunks"])))
    results = []
    for score, i in zip(scores[0], idx[0]):
        if i < 0:
            continue
        chunk = meta["chunks"][i]
        results.append({
            "text": chunk["text"],
            "source": chunk["source"],
            "header": chunk["header"],
            "score": float(score),
        })
    return results


# ─────────────────────────────────────────────────────────────────────────────
# 3) REVIEW (Claude, structured JSON)
# ─────────────────────────────────────────────────────────────────────────────

SYSTEM_PROMPT = """Sen kıdemli bir Kotlin/Android kod inceleyicisisin.

Sana iki şey verilecek:
1. <rules> — ekibin kural dökümanlarından çıkarılmış ilgili kurallar
2. <diff>  — incelenecek git diff'i

Kuralların:
- SADECE <rules> içinde geçen kuralları temel al. Orada yoksa o konuda yorum YAPMA.
- Her bulgu için kaynağı belirt: "kotlin-style.md → Null Safety" gibi.
- Severity şöyle olmalı:
  * "critical": kural ihlali kesin. Push bloklanmalı. (Örn: !! kullanımı, GlobalScope, hardcoded API key.)
  * "warning":  muhtemel sorun, kural belirsiz veya yorumcu kararlı değil.
  * "suggestion": iyileştirme önerisi, kural ihlali değil.
- Hiçbir sorun yoksa findings dizisi boş olsun.

ÇIKTI: SADECE şu JSON şemasında, başka hiçbir metin olmadan:

{
  "findings": [
    {
      "severity": "critical" | "warning" | "suggestion",
      "file": "app/src/.../Foo.kt",
      "line_hint": "etkilenen sembol veya kod parçası",
      "rule_source": "kotlin-style.md → Null Safety",
      "issue": "neyin yanlış olduğu (1-2 cümle)",
      "fix": "nasıl düzeltileceği (1-2 cümle)"
    }
  ],
  "summary": "kısa genel değerlendirme"
}"""


def review(diff: str, retrieved: list[dict]) -> dict:
    """Claude'a JSON formatında review yaptırır."""
    api_key = os.environ.get("ANTHROPIC_API_KEY")
    if not api_key:
        raise SystemExit("❌ ANTHROPIC_API_KEY tanımlı değil (.ai-review/.env)")

    client = Anthropic(api_key=api_key)

    rules_block = "\n\n".join(
        f'<rule source="{c["source"]}" topic="{c["header"]}">\n{c["text"]}\n</rule>'
        for c in retrieved
    )

    user_message = f"""<rules>
{rules_block}
</rules>

<diff>
{diff[:MAX_DIFF_CHARS]}
</diff>

Yukarıdaki kuralları kullanarak diff'i incele ve SADECE belirtilen JSON şemasıyla cevap ver."""

    msg = client.messages.create(
        model=CLAUDE_MODEL,
        max_tokens=2500,
        system=SYSTEM_PROMPT,
        messages=[{"role": "user", "content": user_message}],
    )

    raw = msg.content[0].text.strip()
    # Bazen model ```json ... ``` ile sarabilir, temizle
    if raw.startswith("```"):
        raw = raw.strip("`")
        if raw.startswith("json"):
            raw = raw[4:]
        raw = raw.strip()

    try:
        return json.loads(raw)
    except json.JSONDecodeError as e:
        # Parse edilemezse fail-safe: warning olarak göster, push'u BLOKLAMA
        print(f"{YELLOW}⚠️  Agent çıktısı parse edilemedi: {e}{RESET}", file=sys.stderr)
        print(f"Ham çıktı:\n{raw}", file=sys.stderr)
        return {"findings": [], "summary": "agent yanıtı parse edilemedi"}


# ─────────────────────────────────────────────────────────────────────────────
# 4) RAPOR + BLOCKING KARAR
# ─────────────────────────────────────────────────────────────────────────────

SEVERITY_STYLE = {
    "critical": (RED, "🔴 KRİTİK"),
    "warning": (YELLOW, "🟡 UYARI "),
    "suggestion": (CYAN, "🔵 ÖNERİ "),
}


def print_report(result: dict, retrieved: list[dict]) -> None:
    print(f"\n{BOLD}━━━━━━━━━━━━━━━━━━ AI REVIEW ━━━━━━━━━━━━━━━━━━{RESET}")
    print(f"{BOLD}📚 Kullanılan kurallar ({len(retrieved)}):{RESET}")
    for c in retrieved:
        print(f"   • {c['source']} → {c['header']}  (skor: {c['score']:.3f})")

    findings = result.get("findings", [])
    print(f"\n{BOLD}🔎 Bulgular ({len(findings)}):{RESET}")

    if not findings:
        print(f"   {GREEN}✓ Tüm değişiklikler ekip standartlarına uygun.{RESET}")
    else:
        for f in findings:
            color, label = SEVERITY_STYLE.get(f.get("severity", "warning"),
                                              (YELLOW, "UYARI"))
            print(f"\n   {color}{label}{RESET}  {f.get('file', '?')}")
            print(f"      🔸 {f.get('line_hint', '')}")
            print(f"      📖 Kaynak: {f.get('rule_source', '?')}")
            print(f"      ❌ Sorun:  {f.get('issue', '')}")
            print(f"      ✅ Çözüm:  {f.get('fix', '')}")

    summary = result.get("summary")
    if summary:
        print(f"\n{BOLD}📝 Özet:{RESET} {summary}")
    print(f"{BOLD}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━{RESET}\n")


def main() -> int:
    diff = get_kotlin_diff()
    if not diff.strip():
        print(f"{GREEN}✅ Push'ta Kotlin değişikliği yok, AI review atlanıyor.{RESET}")
        return 0

    print(f"{CYAN}🔍 İlgili kurallar aranıyor (lokal embedding)...{RESET}")
    query = extract_changed_summary(diff)
    if not query.strip():
        query = diff[:MAX_QUERY_CHARS]
    retrieved = retrieve(query)

    print(f"{CYAN}🤖 Claude inceleme yapıyor...{RESET}")
    result = review(diff, retrieved)
    print_report(result, retrieved)

    # BLOCKING KARARI
    critical = [f for f in result.get("findings", [])
                if f.get("severity") == "critical"]
    if critical:
        print(f"{RED}{BOLD}❌ {len(critical)} kritik sorun bulundu — push BLOKLANDI.{RESET}")
        print(f"{YELLOW}   Düzelt ve tekrar push'la, ya da gerçekten gerekirse:{RESET}")
        print(f"{YELLOW}   git push --no-verify{RESET}\n")
        return 1

    print(f"{GREEN}✅ Kritik sorun yok — push devam ediyor.{RESET}\n")
    return 0


if __name__ == "__main__":
    sys.exit(main())