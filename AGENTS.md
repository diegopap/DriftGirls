# 🤖 AGENTS.md — Agent Working Guidelines for Drift Girls

This document provides guidelines and domain rules for AI software agents working on the **Drift Girls** Kotlin Multiplatform repository.

---

## ⚙️ Environment & Build Commands

- **Build Command**: Always prepend `unset ANDROID_PREFS_ROOT` before building with `./kotlin` to prevent Amper preference directory location errors:
  ```bash
  unset ANDROID_PREFS_ROOT && ./kotlin build -m androidApp
  ```
- **Execution Safeguard**: Never commit changes to git unless explicitly instructed by the user.

---

## 📐 Architecture & Domain Rules

### 1. Jetpack Compose State Management
- All frame-by-frame mutable properties in `ActiveRaceState` and `GameEngine` **must** use `by mutableStateOf(...)` to guarantee frame updates trigger Compose re-composition.
- Never wrap state mutations in background threads without dispatching back to the main/UI thread.

### 2. Perspective Track Canvas Geometry (`ArcadeTrackCanvas`)
- The perspective track is drawn using **28 horizontal trapezoidal slices**.
- $v = 0.0$ represents the distant horizon; $v = 1.0$ represents the bottom of the canvas screen.
- **Car Position**: The car is rendered at $v = 0.70$.
- **Lower Road Anchor**: Slices $v \ge 0.40$ must remain fixed at $x_{\text{offset}} = 0$ so the road mouth under the car stays stationary.
- **Upper Horizon Curvature**: Only slices $v < 0.40$ curve to show turns near the horizon.

### 3. Hold-to-Release Drift State Machine
- Each corner allows **EXACTLY 1 Entry Attempt** (`hasAttemptedEntry`) and **EXACTLY 1 Exit Evaluation** (`hasAttemptedExit`).
- Physical touch gestures MUST use low-level pointer event listeners (`awaitPointerEventScope` with `awaitFirstDown` and `waitForUpOrCancellation`).
- If the user releases before $0.55f$ or holds past $1.35f$, evaluate to `DriftRating.MISS`.
- If the user holds through the curve and releases in $0.82f \le \text{cornerProgress} \le 1.12f$, evaluate to `DriftRating.PERFECT`.

### 4. Orientation Resilience & Persistence
- Activity destruction on screen rotation is disabled via `android:configChanges="orientation|screenSize|screenLayout|keyboardHidden|smallestScreenSize|uiMode"` in `AndroidManifest.xml`.
- Any game state modifications (cash, gems, car purchases, upgrades, custom colors, companion affection, track unlocks) must invoke `persistSaveToDisk()` in `GameEngine.kt`.

---

## 📂 Open-Spec Reference Specifications
Detailed technical specs are available in `/openspec/`:
- `openspec/spec.yaml`: Project manifest & feature matrix
- `openspec/architecture.md`: System architecture & KMP module layout
- `openspec/gameplay_mechanics.md`: Mathematical timing matrices & canvas formulas
- `openspec/data_models.md`: Data schemas & save payload delimiter format
