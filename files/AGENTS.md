# AGENTS.md — Project Instructions for AI Coding Assistants

> This file governs how AI tools interact with this codebase.
> Read this entire file before writing any code.

---

## 0. FIRST RULE — Spec Before Code

**Never write implementation code without a spec file.**

Before starting any task, check if a spec exists:
- Spec files live in `/specs/` folder
- Named as `[feature-name]-spec.md`

If no spec exists:
1. Stop
2. Tell the user: "No spec found for this feature. Please create one using the spec template at `/specs/_template.md` before I proceed."
3. Wait for the spec to be provided

If a spec exists:
1. Read it fully before writing a single line
2. Validate your output against it when done

---

## 1. Project Overview

- **Platform:** Android
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Min SDK:** (fill in)
- **Target SDK:** (fill in)

---

## 2. Architecture Rules

### Pattern
This project follows **MVVM + Clean Architecture**.

```
ui/
  screens/          ← Composables only, no business logic
  components/       ← Reusable UI components
  viewmodels/       ← State holders, no direct data access

domain/
  usecases/         ← One class, one responsibility
  models/           ← Pure Kotlin data classes
  repositories/     ← Interfaces only

data/
  repositories/     ← Implementations
  remote/           ← API calls
  local/            ← Room / DataStore
```

### Hard Rules
- Composables never access repositories or data sources directly
- ViewModels never import Android framework classes (Context, Activity)
- Use cases do one thing only — no multi-purpose use cases
- Data layer never knows about UI layer

---

## 3. Code Style

### Kotlin
- Prefer `data class` over plain classes for models
- Use `sealed class` for UI state (Loading, Success, Error)
- Prefer `StateFlow` over `LiveData`
- No nullable types without explicit reason — document why
- Use `when` exhaustively for sealed classes

### Jetpack Compose
- No business logic inside `@Composable` functions
- Side effects only in `LaunchedEffect`, `DisposableEffect`
- Preview annotations for every composable
- State hoisting — composables receive state, don't hold it

### Naming
| Type | Convention | Example |
|---|---|---|
| Composable | PascalCase | `UserProfileScreen` |
| ViewModel | PascalCase + ViewModel | `UserProfileViewModel` |
| UseCase | PascalCase + UseCase | `GetUserProfileUseCase` |
| Repository interface | PascalCase + Repository | `UserRepository` |
| Flow/StateFlow | camelCase | `uiState`, `userList` |

---

## 4. Dependency Injection

- Use **Hilt** for dependency injection
- Every ViewModel injected via `@HiltViewModel`
- No manual instantiation of repositories or use cases
- Modules live in `di/` folder

---

## 5. Security Rules

- No API keys, secrets, or credentials in source code
- Sensitive data stored in **EncryptedSharedPreferences**, not plain SharedPreferences
- No personal data (name, email, phone) in logs or analytics events
- All user inputs validated before processing
- HTTPS only — no plain HTTP calls

---

## 6. Firebase Analytics Rules

- Never call Firebase directly from Composables or ViewModels
- All analytics go through `AnalyticsService` interface
- Event names defined in `AnalyticsEvents.kt` constants file — no hardcoded strings
- No PII in event parameters

```kotlin
// ❌ Wrong
FirebaseAnalytics.getInstance(context).logEvent("button_click", null)

// ✅ Correct
analyticsService.logEvent(AnalyticsEvents.BUTTON_CLICK)
```

---

## 7. Error Handling

- Every network call wrapped in `Result<T>` or `sealed class`
- No silent failures — every error must be handled or explicitly ignored with a comment
- User-facing errors shown via UI state, never via raw exception messages
- No empty `catch` blocks

```kotlin
// ❌ Wrong
try { ... } catch (e: Exception) { }

// ✅ Correct
try { ... } catch (e: Exception) {
    _uiState.value = UiState.Error(e.toUserMessage())
}
```

---

## 8. Testing Requirements

- ViewModels must have unit tests
- Use cases must have unit tests
- Repository implementations must have unit tests
- Use `kotlinx-coroutines-test` for coroutine testing
- No test should depend on Android framework — use fakes/mocks

---

## 9. Review Checklist

Before marking any task as complete, verify:

- [ ] Spec exists and output matches it
- [ ] Architecture rules followed (no layer violations)
- [ ] No hardcoded strings, credentials, or magic numbers
- [ ] Error states handled
- [ ] No personal data in logs or analytics
- [ ] No compiler warnings introduced
- [ ] Naming conventions followed

---

## 10. What NOT to Do

- Do not refactor code outside the scope of the current spec
- Do not add dependencies not mentioned in the spec
- Do not change existing interfaces without updating the spec first
- Do not generate boilerplate "just in case" — only what the spec requires
- Do not assume missing spec details — ask instead
