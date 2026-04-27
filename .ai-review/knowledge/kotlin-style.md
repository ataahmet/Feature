# Kotlin Style Guide

## Naming Conventions

Sınıf isimleri PascalCase olmalıdır: `UserRepository`, `LoginViewModel`.
Fonksiyon ve değişken isimleri camelCase olmalıdır: `fetchUserData`, `isLoggedIn`.
Sabitler (top-level `const val`) UPPER_SNAKE_CASE olur: `MAX_RETRY_COUNT`.
Boolean değişkenler `is`, `has`, `can`, `should` öneki almalıdır.

## Null Safety

`!!` (non-null assertion) operatörü production kodunda yasaktır. Mutlaka null kontrolü, `?.` veya `?:` kullanılmalıdır.
Platform tipleri (Java interop) için açıkça `String?` veya `String` tipini belirt.
`lateinit var` sadece dependency injection veya test setup için kullanılmalı, business logic'te değil.

## Coroutines

Suspend fonksiyonlar `Dispatchers.IO` veya `Dispatchers.Default` üzerinde çalıştırılmalıdır, asla `Dispatchers.Main` üzerinde uzun iş yapma.
`GlobalScope` kullanımı yasaktır. Her zaman `viewModelScope`, `lifecycleScope` veya structured concurrency kullan.
Coroutine içinde yakalanmamış exception'lar uygulamayı çökertir — `CoroutineExceptionHandler` ekle veya try/catch kullan.

## Immutability

`val` her zaman `var`'a tercih edilir. Mutable state minimum tutulmalıdır.
Data class'larda tüm property'ler `val` olmalıdır. State değişikliği için `copy()` kullan.
Collection'lar için `List`, `Set`, `Map` (immutable interface) kullan; `MutableList`'i sadece gerektiğinde.