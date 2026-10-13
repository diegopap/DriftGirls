# 🏎️ Drift Girls — Arcade Drift Racing & Anime Companion Game

**Drift Girls** is a high-octane 2.5D arcade drift racing and anime companion dating game built with **Kotlin Multiplatform (KMP)** and **Compose Multiplatform** targeting Android and iOS.

---

## 🌟 Key Features

- **🏎️ Hold-to-Release Precision Drift Engine**: Dual-phase entry and exit timing evaluation with instant visual feedback badges (`PERFECT ENTRY!`, `PERFECT EXIT!`, and `DOUBLE PERFECT DRIFT!! 🚀`).
- **🛣️ 28-Slice Perspective Track Engine**: Custom vector Canvas graphics featuring upper horizon curvature and an anchored road mouth for 2.5D arcade driving feel.
- **⚡ Neon Yellow Drift Zone**: Dynamic on-screen asphalt overlay indicating exact entry and exit points for optimal drift scoring.
- **⏱️ Tachometer RPM Launch System**: Interactive 5-second start countdown with revving engine RPM and a 6,400–7,300 RPM Green Target Zone for Rocket Starts.
- **👧 Girl Companions & Dating System**: Anime companion profiles, dates (Cat Cafe, Lookout Drive, Ferris Wheel), gift shop, affection meters, and racing stat buffs.
- **🛠️ Garage & Tuning**: Car showroom, performance component upgrades (`TOP SPEED`, `DRIFT CONTROL`, `NITRO CAPACITY`) with explicit level badges and upgrade costs, plus a custom paint color palette.
- **💾 Full State Persistence & Orientation Resilience**: Save progression across app restarts and seamless screen rotation without losing live race or menu state.

---

## 🛠️ Build & Run Instructions

### Prerequisites
- Android Studio 2024.2+ or IntelliJ IDEA with Kotlin Multiplatform plugin.
- JDK 17+.

### Building
Unset `ANDROID_PREFS_ROOT` before building with `./kotlin` to prevent Amper preference directory errors:

```bash
unset ANDROID_PREFS_ROOT && ./kotlin build -m androidApp
```

### Running

- **Android App**:
  ```bash
  unset ANDROID_PREFS_ROOT && ./kotlin run -m androidApp
  ```
- **iOS App**:
  ```bash
  ./kotlin run -m iosApp
  ```

---

## 🎮 Game Controls & Rules

1. **Launch Phase**:
   - Hold `🏎️ GAS` during the 5-second countdown to keep engine RPM inside the green zone (**6,400 – 7,300 RPM**).
2. **Drift Entry Phase**:
   - Touch down on `◄ DRIFT` or `DRIFT ►` when the glowing neon yellow drift zone reaches your car ($v = 0.70$).
3. **Drift Exit Phase**:
   - Hold the drift button through the turn and release it as the trailing edge of the yellow drift zone clears your car's wheels.
   - Releasing too early ($< 0.55f$) or holding too long past the exit ($> 1.35f$) results in an **`EARLY/LATE EXIT (MISS)`**.
4. **Nitro Boost**:
   - Tap `⚡ NITRO` when the nitro meter reaches $\ge 25\%$ for speed boosts.

---

## 🏗️ Open-Spec Documentation
Specifications for AI agents and developers are located in the [`/openspec`](./openspec) directory:
- [`spec.yaml`](./openspec/spec.yaml) — Project & Feature Manifest
- [`architecture.md`](./openspec/architecture.md) — System Architecture & KMP Module Structure
- [`gameplay_mechanics.md`](./openspec/gameplay_mechanics.md) — Precision Drift Timing & Canvas Algorithms
- [`data_models.md`](./openspec/data_models.md) — Data Schemas & Save Payload Format

---

## 📄 License
Developed for Drift Girls KMP.
