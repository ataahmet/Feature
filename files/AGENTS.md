# AGENTS.md — Copilot CLI Proje Bağlamı

## Genel Kurallar
- Bu bir Android projesidir (Kotlin).
- Kotlin style: ktlint default kuralları + Android studio style.
- ktlint ihlallerini düzeltirken **asla** iş mantığını, fonksiyon imzasını veya API sözleşmesini değiştirme.
- Sadece stil/format düzeltmesi yap.

## Kotlin Stil Kuralları
- Wildcard import (`*`) yasaktır — her import açıkça belirtilmeli.
- Trailing comma kullan (Kotlin 1.4+).
- Fonksiyon isimlendirme: camelCase (test fonksiyonları hariç, onlar backtick ile).
- Sınıf isimlendirme: PascalCase.
- Sabit isimlendirme: SCREAMING_SNAKE_CASE.
- Maksimum satır uzunluğu: 120 karakter.
- Tek satırlık `if`/`when` ifadelerinde süslü parantez opsiyonel ama tercih edilen: süslü parantez kullan.

## Satır Bölme Stratejisi
- Uzun fonksiyon parametreleri: her parametreyi ayrı satıra al, son parametreden sonra trailing comma koy.
- Uzun zincir çağrıları (builder pattern): her `.method()` çağrısını yeni satıra al.
- Uzun string concatenation: string template (`${}`) tercih et, gerekiyorsa `trimIndent()` ile multiline string kullan.

## Yapılmaması Gerekenler
- Fonksiyonları inline'a çevirme veya extract etme — bu refactor, style fix değil.
- Değişken veya fonksiyon isimlerini "daha iyi" olacak diye değiştirme — sadece ktlint kuralı ihlal ediyorsa değiştir.
- Yorum ekleme veya silme.
- Test dosyalarında assertion mantığını değiştirme.
