# Android Best Practices

## Activity ve Fragment

Activity ve Fragment'larda business logic yazma — tüm logic ViewModel'a taşınmalıdır.
Fragment constructor'ı parametre almamalı; argümanlar `Bundle` ile `arguments` üzerinden geçirilmeli.
`findViewById` yerine ViewBinding veya DataBinding kullanılmalıdır.
Fragment içinde view referansı `onDestroyView` içinde null'a set edilmelidir (memory leak önleme).

## Dependency Injection

Ekipte standart DI framework Hilt'tir. Manuel singleton (`object`) kullanımı yeni kodda yasaktır.
ViewModel'lar `@HiltViewModel` ile annotate edilmeli ve constructor injection kullanmalıdır.
Repository sınıfları `@Singleton` scope'unda olmalıdır.

## Networking

Network çağrıları doğrudan ViewModel veya Activity içinde yapılmamalı — Repository pattern üzerinden geçmelidir.
Retrofit response'ları `Result<T>` veya sealed class wrapper ile sarılmalıdır.
HTTP timeout default değerlerle bırakılmamalı — connect/read/write için açıkça 15-30 saniye set edilmeli.

## Resources

Hardcoded string yasaktır — tüm UI metinleri `strings.xml` içinde olmalı.
Hardcoded color ve dimension değerleri yasaktır — `colors.xml` ve `dimens.xml` kullan.
Image asset'ler için drawable yerine vector drawable tercih edilmelidir.