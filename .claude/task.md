# Login ekranına "Beni Hatırla" özelliği ekle

## Yapılacaklar
- LoginViewModel'e rememberMe state'i ekle (MVI)
- SharedPreferences'ı Hilt ile inject et
- LoginFragment'a DataBinding ile checkbox ekle
- Uygulama tekrar açıldığında token varsa direkt MainActivity'e yönlendir

## İlgili dosyalar
- app/src/main/java/com/example/ui/login/LoginViewModel.kt
- app/src/main/java/com/example/ui/login/LoginFragment.kt
- app/src/main/res/layout/fragment_login.xml
