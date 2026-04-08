# Skill: Yeni MVI Ekranı Oluştur

## Açıklama
MVI mimarisine uygun yeni bir ekran oluşturur.

## Talimatlar
Verilen Login için şu dosyaları oluştur:
- ui/Login/LoginScreen.kt (Compose UI)
- ui/Login/LoginViewModel.kt (ViewModel)
- ui/Login/LoginContract.kt (State, Event, Effect)
- domain/usecase/LoginiUseCase.kt
- di/LoginiModule.kt (Hilt modülü)
- test/LoginViewModelTest.kt (Unit test)
- Splash başarılı olur ise Login açılsın
- Login başarılı olur ise Main açılsın

## Kesin Kurallar
- ZORUNLU: Jetpack Compose kullan, XML layout YASAK
- ZORUNLU: DataBinding ve ViewBinding YASAK
- ZORUNLU: sealed class ile State, Event, Effect tanımla
- ZORUNLU: Hilt ile dependency injection yap
- ZORUNLU: Unit test dosyası oluştur