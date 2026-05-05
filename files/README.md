# SDD Kit — Android için Specification-Driven Development

Copilot CLI ile çalışan, plugin gerektirmeyen, terminal-first bir SDD akışı.

## Felsefe

Üç farklı kamptan iyi olan ne varsa aldık:

- **Spec Kit** → constitution (AGENTS.md) ve dosya yapısı disiplini
- **Minimal SDD** → tek dosyalı sade spec
- **Agent OS** → alignment fazı (clarifying questions + default cevaplar)

Sonuç: ağır framework yok, slash command yok, plugin yok. Sadece:

- Bir adet `AGENTS.md` (evrensel kurallar)
- Dört şablon (feature, bug, test, refactor)
- Bir bash script (`./scripts/sdd`)
- Copilot CLI (`gh copilot suggest` / `gh copilot explain`)

## Kurulum

### 1. Gereksinimler

```bash
# git zaten varsa atla
brew install git              # macOS
sudo apt install git          # Ubuntu

# GitHub CLI
brew install gh               # macOS
# veya: https://cli.github.com

# Auth
gh auth login

# Copilot extension
gh extension install github/gh-copilot
```

### 2. Kit'i Projene Kur

Bu repo'yu klonla, sonra Android projende install script'i çalıştır:

```bash
git clone <bu-repo> /tmp/sdd-kit
cd /path/to/android-project
/tmp/sdd-kit/install.sh
```

Veya manuel:

```bash
cp /tmp/sdd-kit/AGENTS.md ./
cp -r /tmp/sdd-kit/specs ./
cp -r /tmp/sdd-kit/.github ./
cp -r /tmp/sdd-kit/scripts ./
chmod +x ./scripts/sdd
```

### 3. AGENTS.md'yi Projene Göre Düzenle

```bash
$EDITOR AGENTS.md
```

Mimari, stack, naming, yasaklar — projende ne kullanıyorsan onu yansıt.

## Klasör Yapısı

```
proje-kökü/
├── AGENTS.md                          # evrensel kurallar
├── .github/
│   └── copilot-instructions.md        # AGENTS.md'ye yönlendirir
├── scripts/
│   └── sdd                            # ana CLI script
└── specs/
    ├── _templates/
    │   ├── feature.md
    │   ├── bug.md
    │   ├── test.md
    │   └── refactor.md
    ├── features/
    │   └── 001-kullanici-girisi/
    │       └── spec.md
    ├── bugs/
    ├── tests/
    └── refactors/
```

## Komutlar

### Yeni Spec Oluştur

```bash
./scripts/sdd new feature "Kullanıcı Girişi"
./scripts/sdd new bug "Token refresh sonsuz döngü"
./scripts/sdd new test "LoginViewModel coverage"
./scripts/sdd new refactor "Repository'leri Flow'a geçir"
```

Bu komut:
1. `specs/<tip>s/<numara>-<slug>/spec.md` oluşturur
2. Şablonu kopyalar
3. Branch oluşturur (`feature/001-kullanici-girisi`)
4. Branch'e checkout yapar

### Alignment — Soruları Üret

Spec'i kabaca doldurduktan sonra, AI'a clarifying soru ürettir:

```bash
./scripts/sdd align specs/features/001-kullanici-girisi/spec.md
```

Çıktıyı spec'in "Açık Kararlar" bölümüne yapıştır, hızlıca cevapla.

### Task Implement Et

Tek tek task'ları implement et:

```bash
./scripts/sdd implement specs/features/001-kullanici-girisi/spec.md T3
```

Copilot çıktısını incele, kabul/reddet, build & test, commit.

### Doğrula

Spec'in AGENTS.md ile çelişip çelişmediğini kontrol et:

```bash
./scripts/sdd verify specs/features/001-kullanici-girisi/spec.md
```

### Listele

```bash
./scripts/sdd list              # hepsi
./scripts/sdd list feature      # sadece feature'lar
./scripts/sdd list bug
```

## Tipik İş Akışı

```bash
# 1. Yeni feature başlat
./scripts/sdd new feature "Biometric Login"

# 2. Spec'i editörde aç, Neden/Ne/Kabul Kriterleri'ni doldur
$EDITOR specs/features/002-biometric-login/spec.md

# 3. Alignment soruları üret, cevapla, spec'e yaz
./scripts/sdd align specs/features/002-biometric-login/spec.md

# 4. Spec'i son kez doğrula
./scripts/sdd verify specs/features/002-biometric-login/spec.md

# 5. Spec'i commit'le
git add specs/features/002-biometric-login/
git commit -m "docs(spec): biometric login feature spec"

# 6. Task'ları sırayla implement et
./scripts/sdd implement specs/features/002-biometric-login/spec.md T1
./gradlew check
git add . && git commit -m "feat(auth): T1 - biometric dependencies"

./scripts/sdd implement specs/features/002-biometric-login/spec.md T2
./gradlew check
git add . && git commit -m "feat(auth): T2 - BiometricManager wrapper"

# ... her task için tekrarla

# 7. Tüm task'lar bittiğinde, kabul kriterlerini manuel test et
# 8. PR aç
gh pr create --title "feat: biometric login" --body "Closes spec 002"
```

## Spec Tipleri Ne Zaman Kullanılır

| Durum | Tip |
|-------|-----|
| Yeni davranış ekliyorum | feature |
| Mevcut davranış yanlış | bug |
| Mevcut davranışı koruyorum | test |
| Davranış aynı, kod yapısı değişiyor | refactor |
| 1 saatten kısa cleanup | spec'siz commit |

## İpuçları

**Bir spec, bir tip.** Hibrit durumlar için iki ayrı spec aç ve birbirine link ver.

**Task'ları küçük tut.** 30-60 dakika. Daha büyükse böl.

**Her task ayrı commit.** Conventional commits kullan: `feat:`, `fix:`, `refactor:`, `test:`.

**Refactor'da test güvenlik ağı şart.** Coverage yetersizse, refactor öncesi test spec'i aç.

**AGENTS.md'yi tekrar etme.** Spec'lerde sadece referans ver. Sapma varsa "AGENTS.md'den Sapmalar" bölümünde gerekçele.

**Implement öncesi fresh chat.** Uzun planlama oturumundan sonra Copilot CLI bağlamı şişer. Yeni terminal session, yeni implement.

## Sınırlamalar

- Bu kit Copilot CLI'a optimize. Cursor/Claude Code/Aider kullanıyorsan `scripts/sdd`'nin `gh copilot` çağrılarını ilgili tool'a çevir.
- AGENTS.md değişikliği takım onayı gerektirir — solo proje değilsen.
- Spec yazmak overhead. Çok küçük işler için zorlama.

## Lisans

İstediğin gibi kullan, değiştir, dağıt.
