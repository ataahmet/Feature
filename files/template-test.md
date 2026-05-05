# Test: <hedef sınıf veya akış>

> Bu spec AGENTS.md'deki tüm kurallara tabidir.
> Bu dosyada AGENTS.md kuralları tekrar edilmez; sadece bu test çalışmasına
> özgü detaylar yer alır.

**Branch:** `test/<numara>-<ad>`
**Durum:** draft | in-progress | done
**Oluşturulma:** YYYY-MM-DD

---

## Hedef

<test edilen birim — sınıf adı veya akış>

Örnek:
- `LoginViewModel` — birim test
- Login akışı — end-to-end UI test
- `AuthRepositoryImpl` — entegrasyon test

## Tetikleyici

> Bu test spec'i neden açıldı?

- [ ] Yeni feature: <feature spec referansı>
- [ ] Bug düzeltmesi: <bug spec referansı>
- [ ] Coverage boşluğu (proaktif)
- [ ] Refactor güvenlik ağı: <refactor spec referansı>

---

## Mevcut Durum

- Mevcut coverage: <varsa yüzde>
- Mevcut test sayısı: <varsa>
- Bilinen test boşlukları:
  - <hangi davranışlar test edilmiyor>
  - <hangi edge case'ler eksik>

---

## Test Edilecek Davranışlar

> Davranış kontratı — implementation'a değil, gözlemlenebilir davranışa
> bağlı yazılır.

### Happy Path

1. <senaryo açıklaması>

### Error Path

2. <senaryo — invalid input>
3. <senaryo — network failure>
4. <senaryo — server error>

### Edge Cases

5. <edge case — boş input>
6. <edge case — token expire mid-session>
7. <edge case — concurrent requests>

## Test Edilmeyecekler (Kapsam Dışı)

- <ileride eklenebilecek ama bu spec'te olmayan>
- <başka bir test spec'ine ayrılan>

---

## Test Stratejisi

### Tip

- [ ] Unit test (saf logic, no Android)
- [ ] Integration test (DB, network, multiple components)
- [ ] UI test (Compose UI Test)
- [ ] Instrumented test (gerçek cihaz/emulator)

### Yaklaşım

- Mock vs Fake: <hangisi tercih edildi, gerekçe>
- Coroutine handling: `StandardTestDispatcher` + `runTest`
- State observation: Turbine ile Flow collection

### Test Fixtures

- `<FakeRepositoryAdı>` — `<TestPaket>/fakes/`
- `<TestDataAdı>` — `<TestPaket>/fixtures/`

## Bu Spec'e Özel Kısıtlar

- Yeni test kütüphanesi: <yok>
- Test execution süresi hedefi: <unit < 100ms / integration < 5s>

## AGENTS.md'den Sapmalar

<varsa açıkça yaz, yoksa "Yok" yaz>

---

## Davranış Kontratı

> Test'lerin koruması gereken kontrat — refactor'da kırılmamalı.
> "Şu input → şu output" değil, "şu durumda şu state'e geçmeli" yazılır.

- Login başladığında UI `Loading` state'inde olmalı
- Geçerli bilgilerle login → UI `Success(user)` state'ine geçmeli
- Geçersiz bilgilerle login → UI `Error(message)` state'ine geçmeli
- Network hatası → UI `Error` state'i + retry imkanı

---

## Tasks

- [ ] T1: Test fixture/fake'lerin kurulumu
- [ ] T2: Happy path testi (`should X when valid Y`)
- [ ] T3: Error path testi — invalid input
- [ ] T4: Error path testi — network failure
- [ ] T5: Edge case testi — <ad>
- [ ] T6: Test çalıştır, hepsi pass
- [ ] T7: Coverage'ı ölç ve raporla
- [ ] T8: Hedef coverage'a ulaşıldı mı kontrol et

---

## Doğrulama

- [ ] `./gradlew :<modül>:test` pass
- [ ] Coverage hedefi karşılandı (domain %80+, presentation %60+)
- [ ] Test'ler implementation detayına değil davranışa bağlı
- [ ] Test isimleri açıklayıcı (`should X when Y` formatı)
- [ ] Test'ler bağımsız (sıra önemsiz, test'ler birbirini etkilemez)
