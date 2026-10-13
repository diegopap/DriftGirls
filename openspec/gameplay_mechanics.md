# Gameplay Mechanics Specification

## 1. Hold-to-Release Drift Engine

### Entry & Exit Timing Matrix
Each corner allows **EXACTLY 1 Entry Attempt** and **EXACTLY 1 Exit Evaluation**:

| Phase | Timing Window (`cornerProgress`) | Rating Awarded | Effects |
| :--- | :--- | :--- | :--- |
| **Entry** | `0.10f .. 0.35f` | **`PERFECT ENTRY!`** | +Score, +Nitro, Combo Increment |
| **Entry** | `0.05f .. 0.50f` | **`GREAT ENTRY!`** | +Score, +Nitro |
| **Entry** | `0.00f .. 0.65f` | **`GOOD ENTRY!`** | Base Score |
| **Entry** | Outside window | **`MISS!`** | Combo Reset to x1 |
| **Exit** | `0.82f .. 1.12f` | **`PERFECT EXIT!`** | +Score, +Nitro |
| **Exit** | `0.68f .. 1.25f` | **`GREAT EXIT!`** | +Score |
| **Exit** | `0.55f .. 1.35f` | **`GOOD EXIT!`** | Base Score |
| **Exit** | `< 0.55f` (Early Release) | **`EARLY EXIT! (MISS)`** | Combo Reset to x1 |
| **Exit** | `> 1.35f` (Held Too Long) | **`LATE EXIT! (MISS)`** | Combo Reset to x1 |
| **Double Perfect** | Both Entry & Exit PERFECT | **`DOUBLE PERFECT DRIFT!! 🚀`** | **+2,500 Bonus Score, +50% Nitro, +2 Combo** |

## 2. Neon Yellow Drift Zone Visual Indicator
- **Approach**: Glowing neon yellow asphalt overlay appears at the horizon ($v = 0.0$) and extends down to the front of the car ($v = 0.70$).
- **Entry Cue**: Reaching $v = 0.70$ signifies the exact physical moment to touch down on `◄ DRIFT` or `DRIFT ►`.
- **Corner Execution**: The track section under and behind the car stays painted neon yellow.
- **Exit Cue**: The trailing edge clears the car at $v = 0.70$ (when `cornerProgress` reaches $1.0f$), signaling the exact physical moment to release the drift button.

## 3. Tachometer RPM Countdown Launch
During the 5-second start countdown:
- Player taps/holds `🏎️ GAS` to rev engine RPM.
- Target Green Zone: **6,400 RPM – 7,300 RPM**.
- **Rocket Start (`PERFECT LAUNCH!`)**: Launches car at $70\%$ top speed with $+1,500$ bonus score and $+50\%$ Nitro!

## 4. 28-Slice Perspective Canvas Geometry
- **Slices**: 28 horizontal trapezoidal slices rendered from horizon ($v = 0.0$) down to screen bottom ($v = 1.0$).
- **Upper Horizon Curvature**: Only slices $v < 0.40$ bend to show upcoming turns.
- **Lower Mouth Anchor**: Slices $v \ge 0.40$ stay $100\%$ centered ($x_{\text{offset}} = 0$) so the road mouth under the car remains fixed.
