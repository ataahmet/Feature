#!/bin/bash
set -e

GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

TASK_FILE=".claude/task.md"

if [ ! -f "$TASK_FILE" ]; then
  echo -e "${RED}❌ .claude/task.md bulunamadı!${NC}"
  echo -e "${YELLOW}👉 Önce .claude/task.md dosyasını oluştur ve görevi yaz.${NC}"
  exit 1
fi

if ! command -v gh &> /dev/null; then
  echo -e "${RED}❌ GitHub CLI (gh) kurulu değil!${NC}"
  echo -e "${YELLOW}👉 brew install gh  veya  sudo apt install gh${NC}"
  exit 1
fi

if ! gh auth status &> /dev/null; then
  echo -e "${RED}❌ GitHub'a giriş yapılmamış!${NC}"
  echo -e "${YELLOW}👉 gh auth login komutunu çalıştır.${NC}"
  exit 1
fi

TASK=$(cat "$TASK_FILE")
TASK_TITLE=$(head -1 "$TASK_FILE" | sed 's/^#* *//')

echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${BLUE}  🤖 Claude Agent Başlatılıyor${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${YELLOW}📋 Görev: $TASK_TITLE${NC}"
echo ""

echo -e "${BLUE}🚀 GitHub Actions tetikleniyor...${NC}"

gh workflow run claude-agent.yml \
  -f task="$TASK"

echo ""
echo -e "${GREEN}✅ Workflow tetiklendi!${NC}"
echo -e "${YELLOW}📊 Durumu takip etmek için:${NC}"
echo ""
echo -e "   gh run list --workflow=claude-agent.yml"
echo ""

REPO=$(gh repo view --json nameWithOwner -q .nameWithOwner)
echo -e "   ${BLUE}https://github.com/$REPO/actions${NC}"
echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
