#!/usr/bin/env bash
#
# install.sh — sdd-kit'i mevcut bir Android projesine kur.
#
# Kullanım:
#   ./install.sh              # Mevcut dizine kur
#   ./install.sh /path/to/proj  # Belirli bir dizine kur

set -euo pipefail

TARGET="${1:-$(pwd)}"
SOURCE="$(cd "$(dirname "$0")" && pwd)"

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

err() { echo -e "${RED}ERROR:${NC} $*" >&2; exit 1; }
info() { echo -e "${BLUE}→${NC} $*"; }
ok() { echo -e "${GREEN}✓${NC} $*"; }
warn() { echo -e "${YELLOW}!${NC} $*"; }

[ -d "$TARGET" ] || err "Hedef dizin bulunamadı: $TARGET"

info "sdd-kit kuruluyor: $TARGET"
echo

# AGENTS.md
if [ -f "${TARGET}/AGENTS.md" ]; then
  warn "AGENTS.md zaten var, atlanıyor."
else
  cp "${SOURCE}/AGENTS.md" "${TARGET}/AGENTS.md"
  ok "AGENTS.md kopyalandı"
fi

# .github/copilot-instructions.md
mkdir -p "${TARGET}/.github"
if [ -f "${TARGET}/.github/copilot-instructions.md" ]; then
  warn ".github/copilot-instructions.md zaten var, atlanıyor."
else
  cp "${SOURCE}/.github/copilot-instructions.md" "${TARGET}/.github/copilot-instructions.md"
  ok ".github/copilot-instructions.md kopyalandı"
fi

# specs klasörleri
mkdir -p "${TARGET}/specs/_templates"
mkdir -p "${TARGET}/specs/features"
mkdir -p "${TARGET}/specs/bugs"
mkdir -p "${TARGET}/specs/tests"
mkdir -p "${TARGET}/specs/refactors"
ok "specs/ klasör yapısı oluşturuldu"

# Şablonlar
for tpl in feature bug test refactor; do
  if [ -f "${TARGET}/specs/_templates/${tpl}.md" ]; then
    warn "specs/_templates/${tpl}.md zaten var, atlanıyor."
  else
    cp "${SOURCE}/specs/_templates/${tpl}.md" "${TARGET}/specs/_templates/${tpl}.md"
    ok "specs/_templates/${tpl}.md kopyalandı"
  fi
done

# Klasör placeholder'ları (boş klasörler git'e gitsin diye)
for d in features bugs tests refactors; do
  [ -f "${TARGET}/specs/${d}/.gitkeep" ] || touch "${TARGET}/specs/${d}/.gitkeep"
done

# Scripts
mkdir -p "${TARGET}/scripts"
if [ -f "${TARGET}/scripts/sdd" ]; then
  warn "scripts/sdd zaten var, atlanıyor."
else
  cp "${SOURCE}/scripts/sdd" "${TARGET}/scripts/sdd"
  chmod +x "${TARGET}/scripts/sdd"
  ok "scripts/sdd kopyalandı (executable)"
fi

# README ek bilgi
echo
info "Bağımlılık kontrolü:"

if command -v git >/dev/null 2>&1; then
  ok "git: $(git --version | head -1)"
else
  err "git kurulu değil!"
fi

if command -v gh >/dev/null 2>&1; then
  ok "gh: $(gh --version | head -1)"

  if gh extension list 2>/dev/null | grep -q "github/gh-copilot"; then
    ok "gh-copilot extension yüklü"
  else
    warn "gh-copilot extension yüklü değil!"
    echo "    Kurmak için: gh extension install github/gh-copilot"
  fi
else
  warn "gh (GitHub CLI) kurulu değil!"
  echo "    https://cli.github.com adresinden kur."
fi

echo
ok "Kurulum tamamlandı."
echo
info "İlk spec'ini oluşturmak için:"
echo "    cd ${TARGET}"
echo "    ./scripts/sdd new feature \"İlk Feature\""
echo
info "AGENTS.md'yi kendi projene göre düzenle:"
echo "    \$EDITOR ${TARGET}/AGENTS.md"
