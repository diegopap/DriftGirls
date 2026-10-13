# System Architecture Specification

## Overview
**Drift Girls** is built with **Kotlin Multiplatform (KMP)** and **Compose Multiplatform**. The shared module (`/shared`) contains the complete application architecture, including domain models, game physics engine, custom vector graphics canvases, screens, and platform persistence bridges.

## Module Structure

```
DriftGirls/
├── androidApp/               # Android entry point Activity and Manifest
│   └── src/
│       ├── MainActivity.kt   # Context initialization & App entry
│       └── AndroidManifest.xml # Orientation handling & launcher config
├── iosApp/                   # iOS app entry point
└── shared/                   # KMP Shared Logic & UI
    ├── module.yaml           # Dependencies (Compose, Lifecycle)
    └── src/
        ├── App.kt            # Navigation, lifecycle observers, audio dispatch
        ├── Platform.kt       # KMP expect/actual platform declarations
        ├── engine/
        │   └── GameEngine.kt # State machine, race physics, scoring & save persistence
        ├── model/
        │   └── GameModels.kt # Domain data models & enums
        ├── ui/
        │   ├── graphics/     # Vector graphics (Track, Car, Girl Avatars)
        │   └── screens/      # Compose screen UI compositions
        ├── src@android/      # Android-specific actual implementations
        └── src@ios/          # iOS-specific actual implementations
```

## State Management & Re-composition Strategy
- **`GameEngine`**: The core state container instantiated once per app session.
- **`ActiveRaceState`**: Frame-by-frame mutable race session state. All properties use `by mutableStateOf(...)` to guarantee immediate Compose re-composition on frame updates.
- **Orientation Resilience**: The Android `MainActivity` configures `android:configChanges` to handle `orientation|screenSize` without destroying the Activity or losing memory state.
- **Persistent Storage**: Save data payload is serialized into a delimeter-separated string and persisted using `SharedPreferences` on Android (`NSUserDefaults` on iOS).

## Navigation & Lifecycle
- Screen state uses `rememberSaveable` with `Screen` enum stack.
- Lifecycle event observers pause/resume background audio when the app is minimized or restored.
