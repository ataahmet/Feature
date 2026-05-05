# Bug: <kısa başlık>

> Bu spec AGENTS.md'deki tüm kurallara tabidir.
> Bu dosyada AGENTS.md kuralları tekrar edilmez; sadece bu bug'a özgü
> bilgiler yer alır.

**Branch:** `bugfix/<numara>-<ad>`
**Durum:** draft | investigating | fixing | done
**Severity:** critical | high | medium | low
**Oluşturulma:** YYYY-MM-DD

---

## Belirti

<kullanıcının gördüğü davranış — tek paragraf>

## Beklenen Davranış

<ne olmalıydı>

---

## Reproduction

### Adımlar

1. <adım>
2. <adım>
3. <adım>

### Ortam

- Cihaz: <Pixel 6 / Samsung S22 / Emulator>
- API seviyesi: <33 / 34>
- Build: <debug / release / commit hash veya tag>
- Sıklık: <her zaman / bazen / belirli koşulda>
- İlk gözlemlendiği sürüm: <2.3.0>

### Ek Veri

- Crash log: <varsa link veya stack trace özeti>
- Ekran görüntüsü/video: <varsa link>
- Network log: <varsa link>

---

## Etki

- Kullanıcı etkisi: <crash | data loss | yanlış davranış | cosmetic>
- Etkilenen kullanıcı oranı: <varsa metrik>
- Workaround: <varsa kullanıcının yapabileceği geçici çözüm>

---

## Root Cause Hipotezi

> İnceleme sonrası doldurulur. Öncesinde "Henüz bilinmiyor" yaz.

<hipotez ve hangi kod parçasında olduğu>

### İlgili Dosyalar

- `<paket>/<sınıf>.kt:<satır>` — <neden ilgili>
- `<paket>/<sınıf>.kt:<satır>` — <neden ilgili>

---

## Düzeltme Stratejisi

> Minimal değişiklik tercih edilir. Semptomu gizlemek değil, sebebi
> düzeltmek hedeflenir.

<hangi katman, hangi yaklaşım>

## Bu Spec'e Özel Kısıtlar

- Yeni kütüphane: <yok>
- Davranış değişikliği: <sadece bug'ı düzeltir, başka bir şeyi değiştirmez>

## AGENTS.md'den Sapmalar

<varsa açıkça yaz, yoksa "Yok" yaz>

---

## Regression Test

> Bu test düzeltme öncesi yazılmalı ve **fail etmeli**.
> Düzeltme sonrası **pass etmeli**.

- Test dosyası: `<paket>/<TestSınıfı>.kt`
- Test fonksiyonu: `` `should not <bug davranışı> when <koşul>` ``
- Test tipi: unit | integration | UI

---

## Tasks

- [ ] T1: Bug'ı reproduce eden regression test yaz (fail etmeli)
- [ ] T2: Root cause'u doğrula (debug, log, breakpoint)
- [ ] T3: Root cause hipotezini bu spec'e yaz
- [ ] T4: Minimal fix uygula
- [ ] T5: Regression test pass etmeli
- [ ] T6: İlişkili akışların testleri kırılmadığını doğrula
- [ ] T7: Manuel reproduction adımlarını tekrar çalıştır → bug yok

---

## Doğrulama

- [ ] `./gradlew check` pass (tüm mevcut testler dahil)
- [ ] `./gradlew :app:lintDebug` pass
- [ ] Reproduction adımları manuel olarak çalıştırıldı, bug görünmüyor
- [ ] Bu bug'ın testi `specs/tests/<ilgili-test-spec>` altına taşındı/eklendi
