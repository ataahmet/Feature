# Feature: <ad>

> Bu spec AGENTS.md'deki tüm kurallara tabidir.
> Bu dosyada AGENTS.md kuralları tekrar edilmez; sadece bu spec'e özgü
> kısıtlar ve sapmalar belirtilir.

**Branch:** `feature/<numara>-<ad>`
**Durum:** draft | in-progress | done
**Oluşturulma:** YYYY-MM-DD

---

## Neden

<bir paragraf — hangi kullanıcı problemini çözüyor, neden şimdi>

## Ne

<2-3 cümle ile kapsam — kullanıcı görünümünden ne yapılacak>

## Kabul Kriterleri

- [ ] <kullanıcı görünümünden test edilebilir madde>
- [ ] <ikinci madde>
- [ ] <üçüncü madde>

## Kapsam Dışı

- <yapılmayacaklar — eklenmesi cazip ama bu spec'in dışında olanlar>
- <başka bir spec'e ayrılması gereken konu>

---

## Açık Kararlar (Alignment)

> Bu bölüm Copilot ile alignment fazında doldurulur.
> Her soruya numaralı varsayılan cevap verilir, sen evet/hayır/değiştir
> diyerek hızlıca onaylarsın.

1. <soru> [default: <cevap>]
2. <soru> [default: <cevap>]
3. <soru> [default: <cevap>]

---

## Bu Spec'e Özel Kısıtlar

- Yeni kütüphane: <yok | <ad> + gerekçe>
- Yeni modül: <yok | <ad>>
- Diğer feature'larla bağımlılık: <yok | spec referansı>

## AGENTS.md'den Sapmalar

<varsa açıkça yaz, yoksa "Yok" yaz>

---

## Etkilenen Dosyalar

### Yeni Dosyalar

- `<paket>/<sınıf>.kt`
- `<paket>/<sınıf>.kt`

### Değişecek Dosyalar

- `<paket>/<sınıf>.kt` — <ne değişiyor>

---

## Tasks

> Her task tek commit olur. Task başına 30-60 dk hedeflenir.
> Katmanlar sırayla: data → domain → presentation → DI → test.

### Setup

- [ ] T1: Gerekli bağımlılıkları `libs.versions.toml`'e ekle
- [ ] T2: Modül `build.gradle.kts` güncelle (gerekirse)

### Data Katmanı

- [ ] T3: DTO sınıfları (`<Ad>Dto.kt`)
- [ ] T4: Retrofit API interface (`<Ad>Api.kt`)
- [ ] T5: Repository implementasyonu (`<Ad>RepositoryImpl.kt`)
- [ ] T6: Mapper'lar (DTO ↔ domain)

### Domain Katmanı

- [ ] T7: Domain modeli (`<Ad>.kt`)
- [ ] T8: Repository interface (`<Ad>Repository.kt`)
- [ ] T9: UseCase (`<Eylem>UseCase.kt`)

### Presentation Katmanı

- [ ] T10: UiState sealed interface (`<Ekran>UiState.kt`)
- [ ] T11: ViewModel (`<Ekran>ViewModel.kt`)
- [ ] T12: Composable ekran (`<Ekran>Screen.kt`)
- [ ] T13: Navigation entegrasyonu

### DI

- [ ] T14: Hilt module (`<Sorumluluk>Module.kt`)

### Test (ayrı test spec'i ile)

- [ ] T15: Test spec'i oluştur — `specs/tests/<numara>-<ad>/`
- [ ] T16: Test spec'ini implement et

---

## Doğrulama

### Build

- [ ] `./gradlew check` pass
- [ ] `./gradlew :app:lintDebug` pass
- [ ] `./gradlew :app:detekt` pass

### Manuel Test

- [ ] Tüm kabul kriterleri manuel olarak doğrulandı
- [ ] Edge case'ler test edildi (boş veri, network hatası, vs.)

### Code Review

- [ ] Self-review yapıldı
- [ ] AGENTS.md kurallarına uyumluluk kontrol edildi
