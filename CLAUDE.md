# Proje: Feature (Android)

## Teknik yapı
- Dil: Kotlin
- Mimari: MVI
- Min SDK: 24
- DataBinding kullanılıyor, findViewById yasak
- Hilt dependency injection

## Branch convention
- Yeni özellik: feature/xxx
- Bug fix: bugfix/xxx
- Hotfix: hotfix/xxx

## Commit mesajı formatı
- feat: yeni özellik
- fix: bug düzeltme
- refactor: yeniden yapılandırma
- test: test ekleme

## Code review kriterleri
- Memory leak kontrolü yap
- Kotlin idiomatic kullanım kontrol et
- Null safety ihlali var mı bak
- Rx doğru thread de  kullanılmış mı
- Resource leak (cursor, stream) var mı