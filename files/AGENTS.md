# AGENTS.md

Bu dosya bu repo'da çalışan tüm AI ajanlar için bağlayıcı kuralları içerir.
Spec dosyalarında bu kurallar tekrar edilmez; sadece referans verilir.

Spec'ler bu dosyadan sapmak zorunda kalırsa, sapma açıkça gerekçeli olarak
spec'in "AGENTS.md'den Sapmalar" bölümünde belirtilir.

---

## Mimari

### Katmanlar (Clean Architecture)

- `data/` — Repository implementasyonları, Retrofit API'leri, Room DAO'ları,
  DataStore wrapper'ları, DTO'lar, mapper'lar
- `domain/` — Repository interface'leri, UseCase'ler, domain modelleri.
  Android SDK'ya bağımlılık YOK. Saf Kotlin.
- `presentation/` — ViewModel'ler, Composable'lar, UI state'leri, navigation

### Bağımlılık Yönü

```
presentation → domain ← data
```

- Domain hiçbir katmana bağımlı değildir.
- Data ve presentation katmanları domain'i bilir.
- Data ve presentation birbirini doğrudan tanımaz.

### Modül Yapısı

- `:app` — Entry point, navigation graph, DI bootstrap
- `:core:ui` — Tasarım sistemi, ortak Composable'lar, theme
- `:core:network` — Retrofit, OkHttp, interceptor'lar, base config
- `:core:database` — Room database, base entity'ler
- `:core:common` — Result wrapper, extension'lar, ortak util'ler
- `:feature:<ad>` — Her feature kendi modülünde, kendi data/domain/presentation
  yapısıyla

---

## Teknoloji Stack'i (Pazarlık Konusu Değil)

- Kotlin 2.0, Java 17 toolchain
- Gradle Kotlin DSL (`build.gradle.kts`), Version Catalog (`libs.versions.toml`)
- Compose UI — yeni ekranlarda XML kullanılmaz
- Hilt — DI için tek seçim, Koin yasak
- Coroutines + Flow — async için, RxJava yasak
- Retrofit + OkHttp + Kotlinx Serialization — Gson yasak
- Room — yerel DB için, raw SQLite yasak
- DataStore Preferences — SharedPreferences yasak
- Coil — image loading için, Glide/Picasso yasak
- Timber — logging için, `Log.d` production'da yasak
- minSdk 24, targetSdk 34, compileSdk 34

---

## State Yönetimi

- ViewModel'lerde `StateFlow` kullanılır, `LiveData` yeni kodda yasak
- UI state `sealed interface` ile modellenir:
  ```kotlin
  sealed interface LoginUiState {
      data object Idle : LoginUiState
      data object Loading : LoginUiState
      data class Success(val user: User) : LoginUiState
      data class Error(val message: String) : LoginUiState
  }
  ```
- One-shot eventler için `Channel` + `receiveAsFlow()`
- UI event'ler için `sealed interface` (örn. `NavigateTo`, `ShowSnackbar`)

---

## Hata Yönetimi

- Repository'ler `Result<T>` döndürür (Kotlin's built-in), exception fırlatmaz
- Domain katmanında özel exception sınıfları:
  ```kotlin
  sealed class DomainException(message: String) : Exception(message) {
      data object NetworkUnavailable : DomainException("No network")
      data class ServerError(val code: Int) : DomainException("Server $code")
      data object Unauthorized : DomainException("Unauthorized")
  }
  ```
- UI'da kullanıcıya gösterilecek hata mesajları `strings.xml` üzerinden
- Try-catch sadece sınır katmanlarında (Repository implementasyonları)

---

## Kod Stili

- Public API'lerde KDoc zorunlu, internal'larda önerilir
- Function uzunluğu max 40 satır (gerekçesiz aşılmaz)
- Sınıf uzunluğu max 300 satır (gerekçesiz aşılmaz)
- `!!` operatörü YASAK — null-safety açıkça ele alınır
- `runBlocking` test dışında YASAK
- Magic number yok — `private const val` ile isimlendirilir
- `companion object` sadece factory ve sabitler için
- ktlint ve detekt CI'da pass etmek zorunda

---

## Test

### Stack

- Unit test: JUnit 5, MockK, Turbine, Truth (veya Kotest)
- UI test: Compose UI Test, Espresso (legacy)
- Coroutine test: `StandardTestDispatcher`, `runTest`
- Hilt test: `@HiltAndroidTest`, `HiltTestRunner`

### Kurallar

- Coverage hedefi: domain %80+, presentation %60+, data %50+
- Mock yerine **fake** tercih edilir (özellikle Repository ve DataSource için)
- Her ViewModel için minimum: happy path + error path + edge case testi
- Test isimleri: `` `should X when Y` `` formatında
- Test dosyaları kaynak dosyayla aynı paket yapısında

---

## Naming

- Composable'lar: PascalCase, fiil yerine isim
  - ✓ `LoginScreen`, `UserCard`, `OrderList`
  - ✗ `ShowLogin`, `RenderUser`
- ViewModel'ler: `<Ekran>ViewModel` (`LoginViewModel`, `HomeViewModel`)
- UseCase'ler: `<Eylem>UseCase` (`LoginUseCase`, `FetchUsersUseCase`)
- Repository: interface `<Domain>Repository`, impl `<Domain>RepositoryImpl`
- DTO'lar: `<Ad>Dto` (`UserDto`, `LoginResponseDto`)
- Mapper'lar: `<Ad>Mapper` veya extension `toDomain()` / `toDto()`
- Hilt module'ler: `<Sorumluluk>Module` (`NetworkModule`, `DatabaseModule`)

---

## Yasaklar

- `GlobalScope.launch` kullanılmaz
- `Activity.runOnUiThread` kullanılmaz (Compose'da gerek yok)
- Hardcoded string yok — `strings.xml` kullanılır
- `Log.d`, `Log.e` production'da yasak — Timber kullanılır
- `findViewById` ve View Binding yeni kodda yasak (Compose tercih)
- `lateinit var` Composable'larda ve ViewModel'lerde yasak
- Reflection production kodunda yasak (test'te serbest)
- `Thread.sleep` yasak — `delay` kullanılır

---

## CI / Build

- PR açılmadan önce lokal pass etmeli:
  ```bash
  ./gradlew check
  ./gradlew :app:lintDebug
  ./gradlew :app:detekt
  ```
- Commit mesajı: Conventional Commits
  - `feat:` yeni özellik
  - `fix:` bug düzeltme
  - `refactor:` davranış değişmeden yeniden yapılanma
  - `test:` test ekleme/güncelleme
  - `chore:` build, ci, doc
- Branch isimleri:
  - `feature/<numara>-<kısa-ad>`
  - `bugfix/<numara>-<kısa-ad>`
  - `refactor/<numara>-<kısa-ad>`
  - `test/<numara>-<kısa-ad>`

---

## Spec'lerle İlişki

Tüm yeni iş `specs/` altında bir spec dosyasıyla başlar. Spec tipleri:

- `specs/features/` — yeni özellikler
- `specs/bugs/` — hata düzeltmeleri
- `specs/tests/` — test coverage çalışmaları
- `specs/refactors/` — davranış değişmeden kod yeniden yapılanması

### Her Spec'in Kuralları

1. Bu AGENTS.md'deki kurallar tekrar edilmez, sadece referans verilir
2. Spec şablonu `specs/_templates/` altındadır
3. Implementation task'lara bölünür, her task ayrı commit olur
4. AGENTS.md'den sapma varsa açıkça "AGENTS.md'den Sapmalar" bölümünde
   gerekçesiyle belirtilir
5. Spec yazılmadan kod yazılmaz — istisna: 1 saatten kısa cleanup'lar

---

## AGENTS.md Değişiklikleri

Bu dosya değiştirildiğinde:

- Commit mesajı: `chore(agents): <kısa açıklama>`
- PR açılır, takım review eder
- Mevcut spec'ler etkileniyorsa migrate path belirtilir
- Sprint retrospektifinde haftada bir bu dosya hızlıca okunur
