"""
indexer.py — Knowledge base'i markdown'lardan okur, semantic chunk'lara böler,
sentence-transformers ile lokal embedding'leri alır ve FAISS index'ine yazar.

Bu script tamamen offline çalışır (ilk model indirme hariç).
KB dosyaları her değiştiğinde tekrar çalıştır:  python indexer.py
"""

from __future__ import annotations

import json
import re
from pathlib import Path

import faiss
import numpy as np
from sentence_transformers import SentenceTransformer

ROOT = Path(__file__).parent
KB_DIR = ROOT / "knowledge"
INDEX_PATH = ROOT / "kb.faiss"
META_PATH = ROOT / "kb.meta.json"

# Multilingual model — Türkçe + İngilizce karışık KB'lerde iyi çalışır.
# Boyut: 768. İlk seferinde ~470 MB indirir, sonra ~/.cache/huggingface/'tan okur.
MODEL_NAME = "paraphrase-multilingual-mpnet-base-v2"

CHUNK_TARGET_CHARS = 1500   # ~400 token civarı
CHUNK_MIN_CHARS = 200       # bundan kısa parçaları birleştir


def chunk_markdown(text: str, source: str) -> list[dict]:
    """
    Markdown'u başlık (## ve ###) sınırlarında böler.
    Çok uzun bölümleri paragraf bazında parçalar.
    Her chunk için kaynak dosya ve başlık metadata olarak saklanır.
    """
    # Önce ## ve ### başlıkları ayır
    sections = re.split(r"\n(?=#{2,3} )", text)
    chunks: list[dict] = []

    for sec in sections:
        sec = sec.strip()
        if not sec:
            continue

        header_match = re.match(r"^#{1,3}\s+(.+)", sec)
        header = header_match.group(1).strip() if header_match else "intro"

        if len(sec) <= CHUNK_TARGET_CHARS:
            if len(sec) >= CHUNK_MIN_CHARS or not chunks:
                chunks.append({"text": sec, "source": source, "header": header})
            else:
                # Çok kısa — bir öncekine yapıştır
                chunks[-1]["text"] += "\n\n" + sec
            continue

        # Uzun bölümü paragraf bazında böl
        paragraphs = [p.strip() for p in sec.split("\n\n") if p.strip()]
        buf = ""
        for p in paragraphs:
            if len(buf) + len(p) + 2 > CHUNK_TARGET_CHARS and buf:
                chunks.append({"text": buf, "source": source, "header": header})
                buf = p
            else:
                buf = f"{buf}\n\n{p}" if buf else p
        if buf:
            chunks.append({"text": buf, "source": source, "header": header})

    return chunks


def main() -> None:
    if not KB_DIR.exists():
        raise SystemExit(f"❌ Knowledge dizini bulunamadı: {KB_DIR}")

    md_files = sorted(KB_DIR.rglob("*.md"))
    if not md_files:
        raise SystemExit(f"❌ {KB_DIR} içinde .md dosyası yok.")

    print(f"📂 {len(md_files)} markdown dosyası bulundu.")

    all_chunks: list[dict] = []
    for md in md_files:
        text = md.read_text(encoding="utf-8")
        chunks = chunk_markdown(text, md.name)
        all_chunks.extend(chunks)
        print(f"   • {md.name}: {len(chunks)} chunk")

    print(f"\n🧠 Model yükleniyor: {MODEL_NAME}")
    print("   (ilk seferinde ~470 MB indirilir, sonra cache'ten okur)")
    model = SentenceTransformer(MODEL_NAME)

    print(f"🔢 {len(all_chunks)} chunk için embedding hesaplanıyor...")
    texts = [c["text"] for c in all_chunks]
    embeddings = model.encode(
        texts,
        batch_size=16,
        show_progress_bar=True,
        normalize_embeddings=True,  # cosine similarity için
        convert_to_numpy=True,
    ).astype("float32")

    dim = embeddings.shape[1]
    print(f"   embedding boyutu: {dim}")

    # IndexFlatIP = inner product. normalize_embeddings=True ile cosine = IP olur.
    index = faiss.IndexFlatIP(dim)
    index.add(embeddings)

    faiss.write_index(index, str(INDEX_PATH))
    META_PATH.write_text(
        json.dumps(
            {"model": MODEL_NAME, "dim": dim, "chunks": all_chunks},
            ensure_ascii=False,
            indent=2,
        ),
        encoding="utf-8",
    )

    print(f"\n✅ Index hazır:")
    print(f"   {INDEX_PATH}  ({INDEX_PATH.stat().st_size // 1024} KB)")
    print(f"   {META_PATH}   ({len(all_chunks)} chunk)")


if __name__ == "__main__":
    main()