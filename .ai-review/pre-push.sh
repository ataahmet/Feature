#!/usr/bin/env bash
#
# .git/hooks/pre-push
#
# Sıra:
#   1. ./gradlew ktlintFormat   → hata: BLOK
#   2. format dosyaları değiştirdi mi → değiştirdiyse: BLOK (amend gerekir)
#   3. .ai-review/agent.py      → critical bulgu: BLOK
#
# Bypass:  git push --no-verify
#

set -e

REPO_ROOT="$(git rev-parse --show-toplevel)"
cd "$REPO_ROOT"

YELLOW='\033[33m'
RED='\033[31m'
GREEN='\033[32m'
BOLD='\033[1m'
RESET='\033[0m'

# ─── 1) KTLINT FORMAT ────────────────────────────────────────────────
if [ -x "./gradlew" ]; then
    echo -e "${BOLD}━━━ ktlint format ━━━${RESET}"
    if ! ./gradlew ktlintFormat --quiet --console=plain; then
        echo -e "${RED}❌ ktlint hata verdi — push iptal.${RESET}"
        echo -e "${YELLOW}   Hataları düzelt ve tekrar push'la.${RESET}"
        exit 1
    fi

    # ─── 2) FORMAT TARAFINDAN DEĞİŞTİRİLEN DOSYA VAR MI? ────────────
    if ! git diff --quiet; then
        echo -e "${YELLOW}⚠️  ktlint dosyaları formatladı.${RESET}"
        echo -e "${YELLOW}   Lütfen şunu çalıştır:${RESET}"
        echo -e "${YELLOW}     git add -A && git commit --amend --no-edit${RESET}"
        echo -e "${YELLOW}   Sonra tekrar push'la.${RESET}"
        exit 1
    fi
    echo -e "${GREEN}✓ ktlint temiz.${RESET}"
else
    echo -e "${YELLOW}⚠️  ./gradlew bulunamadı, ktlint atlanıyor.${RESET}"
fi

# ─── 3) AI REVIEW AGENT ──────────────────────────────────────────────
AGENT_DIR="$REPO_ROOT/.ai-review"
PY_BIN="$AGENT_DIR/venv/bin/python"
[ -x "$PY_BIN" ] || PY_BIN="$(command -v python3)"

if [ -z "$PY_BIN" ] || [ ! -x "$PY_BIN" ]; then
    echo -e "${YELLOW}⚠️  Python bulunamadı, AI review atlanıyor.${RESET}"
    exit 0
fi

if [ ! -f "$AGENT_DIR/agent.py" ]; then
    echo -e "${YELLOW}⚠️  $AGENT_DIR/agent.py yok, AI review atlanıyor.${RESET}"
    exit 0
fi

"$PY_BIN" "$AGENT_DIR/agent.py"
AGENT_EXIT=$?

if [ $AGENT_EXIT -ne 0 ]; then
    exit $AGENT_EXIT
fi

exit 0
