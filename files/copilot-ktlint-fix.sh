#!/usr/bin/env bash
# =============================================================================
#  copilot-ktlint-fix.sh
#  Pre-push hook: ktlintFormat → ktlintCheck → Copilot CLI (headless) refactor
#
#  Kullanım : lefthook üzerinden otomatik çağrılır (pre-push)
#  Manuel   : bash scripts/copilot-ktlint-fix.sh
# =============================================================================
set -euo pipefail

# ── Ayarlar ──────────────────────────────────────────────────────────────────
MAX_ITERATIONS=3                          # Copilot max deneme sayısı
REPORT_DIR="build/reports/ktlint"         # Checkstyle XML çıktı klasörü
GRADLEW="./gradlew"                       # Gradle wrapper yolu
COPILOT_MODEL=""                          # Boş = varsayılan model; ör. "claude-sonnet-4-6"
# ─────────────────────────────────────────────────────────────────────────────

# ── Renk yardımcıları ───────────────────────────────────────────────────────
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

info()  { echo -e "${CYAN}ℹ ${NC}$1"; }
ok()    { echo -e "${GREEN}✓ ${NC}$1"; }
warn()  { echo -e "${YELLOW}⚠ ${NC}$1"; }
fail()  { echo -e "${RED}✗ ${NC}$1"; }

# ── 1. Değişen .kt dosyalarını bul ──────────────────────────────────────────
detect_changed_kt_files() {
    local current_branch
    current_branch="$(git rev-parse --abbrev-ref HEAD)"

    local remote_branch="origin/${current_branch}"

    # Remote tracking branch yoksa fallback: origin/main veya origin/develop
    if ! git rev-parse --verify "$remote_branch" &>/dev/null; then
        if git rev-parse --verify "origin/main" &>/dev/null; then
            remote_branch="origin/main"
        elif git rev-parse --verify "origin/develop" &>/dev/null; then
            remote_branch="origin/develop"
        else
            warn "Remote branch bulunamadı, tüm staged dosyalar kullanılacak."
            git diff --cached --name-only --diff-filter=ACMR | grep '\.kt$' || true
            return
        fi
    fi

    local merge_base
    merge_base="$(git merge-base HEAD "$remote_branch" 2>/dev/null || echo "")"

    if [ -n "$merge_base" ]; then
        # Push edilecek commit'lerde değişen .kt dosyaları
        git diff --name-only --diff-filter=ACMR "${merge_base}...HEAD" | grep '\.kt$' || true
    else
        # Merge base bulunamazsa cached fallback
        git diff --cached --name-only --diff-filter=ACMR | grep '\.kt$' || true
    fi
}

CHANGED_KT_FILES=$(detect_changed_kt_files)

if [ -z "$CHANGED_KT_FILES" ]; then
    ok "Push'ta değişen .kt dosyası yok — atlanıyor."
    exit 0
fi

FILE_COUNT=$(echo "$CHANGED_KT_FILES" | wc -l | tr -d ' ')
info "Push'ta değişen ${FILE_COUNT} adet .kt dosyası bulundu."

# ── 2. ktlintFormat — otomatik düzeltilebilenleri düzelt ─────────────────────
info "ktlintFormat çalıştırılıyor..."
$GRADLEW ktlintFormat --quiet 2>/dev/null || true
ok "ktlintFormat tamamlandı."

# ── 3. ktlintCheck — kalan ihlalleri kontrol et ─────────────────────────────
run_ktlint_check() {
    # Sadece değişen dosyaları kontrol et. ktlint-gradle plugin'i dosya
    # filtresi destekliyorsa kullan, yoksa full check yapılır.
    $GRADLEW ktlintCheck --quiet 2>/dev/null
}

if run_ktlint_check; then
    ok "ktlintCheck temiz — Copilot'a gerek yok."
    exit 0
fi

# ── 4. Kalan ihlalleri rapor olarak topla ────────────────────────────────────
collect_violations() {
    local violations=""

    # Checkstyle XML raporlarını tara
    if [ -d "$REPORT_DIR" ]; then
        while IFS= read -r report_file; do
            violations+="$(cat "$report_file")"
            violations+=$'\n'
        done < <(find "$REPORT_DIR" -name "*.xml" -type f 2>/dev/null)
    fi

    # XML yoksa plain text raporları dene
    if [ -z "$violations" ]; then
        while IFS= read -r report_file; do
            violations+="$(cat "$report_file")"
            violations+=$'\n'
        done < <(find "$REPORT_DIR" -name "*.txt" -type f 2>/dev/null)
    fi

    # Rapor dosyası bulunamazsa terminal çıktısını kullan
    if [ -z "$violations" ]; then
        violations=$($GRADLEW ktlintCheck 2>&1 || true)
    fi

    echo "$violations"
}

# ── 5. Copilot CLI ile iteratif düzeltme ─────────────────────────────────────
warn "ktlintFormat çözemedi — Copilot CLI devrede (max ${MAX_ITERATIONS} iterasyon)."

MODEL_FLAG=""
if [ -n "$COPILOT_MODEL" ]; then
    MODEL_FLAG="--model $COPILOT_MODEL"
fi

ITERATION=0
while [ $ITERATION -lt $MAX_ITERATIONS ]; do
    ITERATION=$((ITERATION + 1))
    info "Copilot iterasyon ${ITERATION}/${MAX_ITERATIONS}..."

    VIOLATIONS=$(collect_violations)

    # Değişen dosya listesini Copilot'a ver
    FILE_LIST=$(echo "$CHANGED_KT_FILES" | tr '\n' ', ')

    PROMPT=$(cat <<PROMPT_EOF
Sen bir Kotlin code-quality agent'ısın.
Görevin: ktlintFormat'ın otomatik çözemediği ktlint ihlallerini düzeltmek.

## Kurallar
- Sadece aşağıdaki dosyalarda değişiklik yap, başka dosyaya dokunma.
- Asla iş mantığını, fonksiyon davranışını veya API sözleşmesini değiştirme.
- Sadece stil/format düzeltmesi yap: satır uzunluğu kısaltma, isimlendirme, import düzenleme, trailing comma ekleme vb.
- Emin olmadığın bir düzeltmeyi yapma, o ihlali atla.
- Her düzeltmeden sonra dosyayı kaydet.

## Değiştirilecek dosyalar
${FILE_LIST}

## Mevcut ktlint ihlalleri (Checkstyle XML veya plain text)
${VIOLATIONS}

## İşlem adımları
1. Yukarıdaki ihlalleri oku ve her birini ilgili dosyada düzelt.
2. Düzeltmelerden sonra './gradlew ktlintCheck --quiet' çalıştır.
3. Hâlâ hata varsa sonuçları listele.
PROMPT_EOF
    )

    # Headless mode: onay istemeden çalıştır
    if ! copilot -p "$PROMPT" --allow-all-tools $MODEL_FLAG 2>/dev/null; then
        warn "Copilot iterasyon ${ITERATION} başarısız oldu, bir sonraki denemeye geçiliyor..."
        continue
    fi

    # Copilot'tan sonra tekrar kontrol
    if run_ktlint_check; then
        ok "Copilot iterasyon ${ITERATION}'de tüm ihlalleri çözdü!"

        # Düzeltilen dosyaları yeniden stage'le
        echo "$CHANGED_KT_FILES" | xargs git add
        ok "Düzeltilen dosyalar yeniden staging'e eklendi."
        exit 0
    fi

    warn "Iterasyon ${ITERATION} sonrası hâlâ ihlal var."
done

# ── 6. Max iterasyona ulaşıldı — son durum ──────────────────────────────────
echo ""
fail "═══════════════════════════════════════════════════════════════"
fail "  ${MAX_ITERATIONS} iterasyon sonunda hâlâ ktlint ihlalleri var."
fail "  Manuel düzeltme gerekli. Kalan ihlaller:"
fail "═══════════════════════════════════════════════════════════════"
echo ""
$GRADLEW ktlintCheck 2>&1 || true
echo ""
warn "Push iptal edildi. Düzelt ve tekrar dene."
warn "Acil bypass: git push --no-verify"
exit 1
