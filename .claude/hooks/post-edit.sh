#!/bin/bash
INPUT=$(cat)
FILE=$(echo "$INPUT" | jq -r '.tool_input.file_path // empty' 2>/dev/null)

if [[ "$FILE" == *.kt ]]; then
  echo "$(date): Formatlanıyor → $FILE" >> /Users/ahmetata/hook-test.log
  cd /Users/ahmetata/AndroidStudioProjects/Feature
  ./gradlew ktlintFormat 2>/dev/null
  echo "$(date): Formatlama tamamlandı" >> /Users/ahmetata/hook-test.log
fi