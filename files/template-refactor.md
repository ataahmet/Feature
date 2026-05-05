# Refactor: <kısa başlık>

> Bu spec AGENTS.md'deki tüm kurallara tabidir.
> Bu dosyada AGENTS.md kuralları tekrar edilmez; sadece bu refactor'a özgü
> bilgiler yer alır.

**Branch:** `refactor/<numara>-<ad>`
**Durum:** draft | in-progress | done
**Oluşturulma:** YYYY-MM-DD

---

## Motivasyon

<bir paragraf — neden şimdi, hangi acıyı azaltıyor>

## Tetikleyici

> Refactor'u zorunlu kılan şey neydi?

- [ ] Yeni feature gereksinimi: <feature spec referansı>
- [ ] Tekrar eden bug pattern'i: <bug spec referansları>
- [ ] Performance ölçümü: <varsa metrik>
- [ ] Sadece kod kalitesi (kabul edilen teknik borç ödemesi)

---

## Davranış Değişmezliği Sözü

> Bu refactor sonrası AŞAĞIDAKILER **DEĞİŞMEYECEK**:

- Kullanıcı görünümünden hiçbir şey
- Public API imzaları (eğer kütüphaneyse)
- Database şeması ve migration'lar
- Mevcut testlerin sonuçları
- Network çağrı imzaları (eğer external client varsa)

---

## Kapsam (Sadece Bunlar)

- `<dosya/sınıf 1>`
- `<dosya/sınıf 2>`
- `<dosya/sınıf 3>`

## Kapsam Dışı (Açıkça Yapılmayacaklar)

> Bu bölüm refactor sırasında ortaya çıkacak "boy scout" çekiciliklerine
> direnmek için. Aşağıdakiler bu spec'in dışında:

- <yapılmayacak iyileştirme 1>
- <yapılmayacak iyileştirme 2>
- <ayrı spec'e ayrılması gereken konular>

---

## Mevcut Durum

```kotlin
// Şu an nasıl
suspend fun getUser(id: String): User
```

## Hedef Durum

```kotlin
// Refactor sonrası nasıl olacak
fun getUser(id: String): Flow<User>
```

---

## Migrasyon Stratejisi

- [ ] Big bang mi, kademeli mi: <gerekçe>
- [ ] Geriye dönük uyumluluk: <eski API silinecek mi, deprecate mi>
- [ ] Çağıran kod güncellemesi: <kaç dosya etkileniyor, nasıl ilerlenecek>
- [ ] Feature flag arkasında mı: <evet/hayır>

## Bu Spec'e Özel Kısıtlar

- Yeni kütüphane: <yok>
- Eski kütüphane silinecek mi: <evet/hayır + ad>

## AGENTS.md'den Sapmalar

<varsa açıkça yaz, yoksa "Yok" yaz>

---

## Güvenlik Ağı

### Mevcut Test Coverage

- [ ] Etkilenen kodun mevcut testleri pass ediyor:
  - <test adı 1> ✓
  - <test adı 2> ✓
- [ ] Coverage yeterli mi? <evet | hayır → T1'de test eklenecek>

### Eksik Test Stratejisi

> Refactor öncesi test coverage yetersizse, **refactor'a başlamadan önce**
> test spec'i açılır ve testler yazılır.

- Test spec referansı: <varsa>

---

## Tasks

### Hazırlık

- [ ] T1: Mevcut test coverage'ı ölç ve raporla
- [ ] T2: Eksik testler varsa `specs/tests/<ad>` ile tamamla, sonra devam
- [ ] T3: Refactor öncesi tüm testler pass (baseline)

### Refactor

- [ ] T4: <Hedef durum> implementation
- [ ] T5: Çağıran kodlardan ilki güncellendi
- [ ] T6: Çağıran kodlardan ikincisi güncellendi
- [ ] T7: ... (her ana çağıran için bir task)
- [ ] T8: Eski API silindi (kademeli ise son adımda)

### Doğrulama

- [ ] T9: Tüm mevcut testler **değiştirilmeden** pass etmeli
- [ ] T10: Build + lint + detekt temiz
- [ ] T11: APK boyutu / build süresi regression yok
- [ ] T12: Manuel smoke test — kritik akışlar çalışıyor

---

## Geri Dönüş Planı

> Bir şey ters giderse nasıl revert edilecek?

- Tek commit mi, çoklu commit mi: <strateji>
- Feature flag arkasında mı: <evet/hayır>
- Vazgeçme noktası: <hangi adımdan sonra revert maliyeti yüksek>
- Revert komutu: `git revert <commit-range>`

---

## Doğrulama

- [ ] Davranış değişmezliği sözü tutuldu (mevcut testler değişmeden pass)
- [ ] `./gradlew check` pass
- [ ] `./gradlew :app:lintDebug` pass
- [ ] `./gradlew :app:detekt` pass
- [ ] Manuel smoke test geçti
- [ ] Kapsam dışı bırakılan iyileştirmeler için yeni spec'ler açıldı (gerekirse)
