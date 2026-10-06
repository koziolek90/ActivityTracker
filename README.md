# 🚴 Movy — Automatic Physical Activity Tracker

> 🤖 **Note:** This entire application was created using AI.

**Movy** is a modern, cross-platform mobile application built with **Kotlin Multiplatform (KMP)** and **Compose Multiplatform**, designed for automatically tracking, recognizing, and analyzing the user's daily physical activity in real time.

The app works seamlessly in the background — identifying the user's current activity (e.g., walking, running, cycling, driving/in-vehicle, or resting) without needing to manually start workouts, and collecting detailed statistics throughout the day.

---

## 🚀 Key Features

- 🚶 **Real-Time Background Activity Recognition**
  - Detection of physical states: *Resting (Still)*, *Walking*, *Running*, *Cycling (On Bicycle)*, and *In Vehicle*.
  - Utilizes native device sensors and system algorithms optimized for low battery consumption.
- 📱 **Live Status View & Event History**
  - Clean dashboard presenting the user's current activity state with dedicated color palettes and Material Design 3 iconography.
  - Chronological history log of recorded activity state transitions with timestamps and confidence scores (`confidence %`).
- 📊 **Detailed Daily Statistics**
  - Summary cards and graphs for total active movement time during the day.
  - Time and percentage breakdown for each activity type.
  - Interactive date selector to inspect historical statistics from previous days.
- 💾 **Offline-First Data Storage**
  - Persistent local database storage on the device for activity records and history.

---

## 🛠️ Tech Stack

| Area | Technologies / Libraries Used |
| :--- | :--- |
| **Cross-Platform Architecture** | **Kotlin Multiplatform (KMP)** — 100% shared business logic, database, and UI layer |
| **User Interface (UI)** | **Compose Multiplatform** + **Material Design 3** |
| **Navigation** | **Navigation Compose** with type-safe routing (`kotlinx.serialization`) |
| **Local Database** | **androidx.room (Room KMP)** + **Bundled SQLite Driver** |
| **Dependency Injection (DI)** | **Koin** (`koin-core`, `koin-compose`, `koin-compose-viewmodel`) |
| **Asynchrony & Reactive Streams** | **Kotlin Coroutines** + **Flow** (`StateFlow`, `SharedFlow`) |
| **Date & Time Handling** | **Kotlinx DateTime** |
| **Serialization** | **Kotlinx Serialization** |

---
