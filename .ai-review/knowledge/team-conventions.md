# Team Conventions

## Logging

Production kodunda `Log.d`, `println`, `System.out` kullanımı yasaktır.
Tüm logging Timber üzerinden yapılmalıdır: `Timber.d("...")`, `Timber.e(throwable, "...")`.
PII (kullanıcı email, telefon, isim) ve API token loglara yazılmamalıdır.

## Testing

Public fonksiyonlar için unit test zorunludur (>80% coverage hedefi).
Test isimleri `should_<expected>_when_<condition>` formatında olmalıdır.
Mock framework olarak MockK kullanılır, Mockito değil.

## Git Commit

Commit mesajları conventional commits formatında olmalıdır: `feat:`, `fix:`, `refactor:`, `docs:`, `test:`.
Bir commit tek mantıksal değişiklik içermelidir; karışık değişiklikler ayrı commit'lere bölünmelidir.

## TODO ve FIXME

`TODO` veya `FIXME` yorumu eklenirken yanına ticket ID veya geliştirici ismi yazılmalıdır:
`// TODO(JIRA-1234): caching ekle` veya `// FIXME(ahmet): null durumu handle edilmeli`.
Ticket'sız TODO yorumları code review'da reddedilir.