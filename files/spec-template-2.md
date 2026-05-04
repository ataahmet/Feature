# [FEATURE NAME] — Spec

**Date:**
**Author:**
**Status:** Draft / Approved / Implemented
**Spec file:** `specs/[feature-name]-spec.md`

---

## 1. PROBLEM

> What problem are we solving?

- **Current state:** What is happening now?
- **Desired state:** What should happen instead?
- **Impact:** Who is affected and how much?

---

## 2. INTENT

### What does it do?
*(1-2 sentences. Product language, not technical.)*

### Why does it exist?
*(Which user problem does it solve?)*

### Why now?
*(Priority justification)*

---

## 3. SCOPE

### In Scope
- [ ] ...
- [ ] ...

### Out of Scope
- [ ] ... — *why excluded?*
- [ ] ... — *consider for future*

---

## 4. ARCHITECTURAL DECISIONS

### Layer breakdown
| Layer | Component | Responsibility |
|---|---|---|
| UI | `XxxScreen.kt` | Display state, handle user input |
| UI | `XxxViewModel.kt` | Hold UI state, call use cases |
| Domain | `XxxUseCase.kt` | Business logic |
| Domain | `XxxModel.kt` | Data model |
| Data | `XxxRepositoryImpl.kt` | Fetch / store data |

### Key decisions
| Decision | Chosen | Alternative | Reason |
|---|---|---|---|
| | | | |

### Data flow
```
XxxScreen
  ↓ user action
XxxViewModel
  ↓ calls
XxxUseCase
  ↓ calls
XxxRepository (interface)
  ↓ implemented by
XxxRepositoryImpl
```

### UI State definition
```kotlin
sealed class XxxUiState {
    object Loading : XxxUiState()
    data class Success(val data: XxxModel) : XxxUiState()
    data class Error(val message: String) : XxxUiState()
}
```

---

## 5. SCREENS AND NAVIGATION

### Screen list
| Screen | Composable | ViewModel | Route |
|---|---|---|---|
| | | | |

### Happy path
1. User opens screen
2. ViewModel loads data
3. ...

### Edge cases
| Scenario | Expected Behavior |
|---|---|
| No internet | Show cached data + warning banner |
| Empty list | Show empty state composable |
| API error | Show error state with retry button |
| Loading | Show shimmer / progress indicator |

---

## 6. ANALYTICS EVENTS

| User Action | Event Constant | Parameters |
|---|---|---|
| | `AnalyticsEvents.XXX` | |

> All events must be added to `AnalyticsEvents.kt` — no hardcoded strings.
> No PII (name, email, phone) in parameters.

---

## 7. RISKS AND ALTERNATIVES

### Why not the alternative?
| Alternative | Reason Rejected |
|---|---|
| | |

### Known risks
| Risk | Likelihood | Mitigation |
|---|---|---|
| | | |

---

## 8. IMPLEMENTATION STEPS

> Each step must be verified before moving to the next.
> Give each step to the AI separately — not all at once.

```
Step 1: Domain layer
→ Create XxxModel data class
→ Create XxxRepository interface
→ Create XxxUseCase
→ Verify: unit tests pass

Step 2: Data layer
→ Implement XxxRepositoryImpl
→ Wire up with Hilt module
→ Verify: unit tests pass with fake data

Step 3: ViewModel
→ Create XxxViewModel with @HiltViewModel
→ Expose uiState: StateFlow<XxxUiState>
→ Verify: ViewModel unit tests pass

Step 4: UI
→ Create XxxScreen composable
→ Collect uiState, render each state
→ Add @Preview annotations
→ Verify: all UI states render correctly

Step 5: Navigation
→ Add route to NavGraph
→ Verify: navigation works end to end

Step 6: Analytics
→ Add events to AnalyticsEvents.kt
→ Wire up in ViewModel
→ Verify: events fire correctly
```

---

## 9. SUCCESS CRITERIA

### Technical
- [ ] All architecture rules followed (no layer violations)
- [ ] No hardcoded strings, credentials, or magic numbers
- [ ] All edge cases handled
- [ ] No compiler warnings introduced
- [ ] Unit tests written and passing

### Product
- [ ] Happy path works end to end
- [ ] All edge case states visible in UI
- [ ] Analytics events firing correctly

### Performance
| Metric | Limit |
|---|---|
| Screen load time | max 2 seconds |
| | |

---

## 10. SECURITY AND COMPLIANCE

- [ ] No secrets committed to repo
- [ ] Sensitive data in EncryptedSharedPreferences
- [ ] All inputs validated
- [ ] No PII in logs or analytics

---

## 11. OPEN QUESTIONS

> Must be resolved before implementation begins.

| Question | Owner | Due Date |
|---|---|---|
| | | |

---

## 12. CHANGE LOG

| Date | Change | Reason |
|---|---|---|
| | Initial draft | — |
