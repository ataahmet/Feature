#!/bin/bash

# Pre-push hook: ktlint ile Kotlin dosyalarını kontrol et

KTLINT=$(which ktlint 2>/dev/null)

if [ -z "$KTLINT" ]; then
    echo "⚠️  ktlint bulunamadı. Kurulum için: brew install ktlint"
    echo "   Hook atlanıyor..."
    exit 0
fi

echo "🔍 ktlint kontrolü çalışıyor..."

# Push edilecek branch'teki değişen Kotlin dosyalarını bul
CHANGED_FILES=$(git diff --name-only HEAD origin/$(git rev-parse --abbrev-ref HEAD) 2>/dev/null | grep "\.kt$")

if [ -z "$CHANGED_FILES" ]; then
    # origin karşılaştırması başarısız olursa staged/committed dosyaları al
    CHANGED_FILES=$(git diff --name-only origin/master...HEAD 2>/dev/null | grep "\.kt$")
fi

if [ -z "$CHANGED_FILES" ]; then
    echo "✅ Kontrol edilecek Kotlin dosyası yok."
    exit 0
fi

echo "Kontrol edilen dosyalar:"
echo "$CHANGED_FILES" | sed 's/^/  - /'

# ktlint çalıştır
echo "$CHANGED_FILES" | xargs $KTLINT 2>&1
EXIT_CODE=$?

if [ $EXIT_CODE -ne 0 ]; then
    echo ""
    echo "❌ ktlint hataları bulundu. Push iptal edildi."
    echo "   Otomatik düzeltme için: ktlint --format <dosya>"
    exit 1
fi

echo "✅ ktlint kontrolü başarılı."
exit 0
