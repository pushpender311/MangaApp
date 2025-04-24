# Manga App 📚🧠

An Android application built using **Jetpack Compose**, **Clean Architecture + MVVM**, and **Single Activity Architecture** with **Jetpack Navigation Component**.  
The app provides user **authentication**, **manga data fetching & caching**, all packaged within a modern Android app architecture.

---

## ✨ Features

### 🔐 User Authentication (Room DB)
- Sign In screen for users to authenticate using email and password.
- New users are automatically registered and signed in.
- Securely stores user credentials using **Room Database**.
- Remembers signed-in users across app restarts.
- Automatically navigates to Home if user is already signed in.

### 🧭 Bottom Navigation
- Two main screens accessible via Bottom Navigation:
    - **Manga**
    - **Face Recognition** (pending)
- Sign In screen does **not** display bottom navigation.

### 📖 Manga Screen
- Fetches manga data using the [MangaVerse API](https://rapidapi.com/sagararofie/api/mangaverse-api) (`fetch-manga` endpoint).
- Implements efficient **pagination** for large manga lists.
- Caches manga data locally using **Room DB** for offline access.
- Automatically syncs and refreshes cache when the device is online.
- Tap on a manga to view detailed description in a dedicated screen.

### 🤳 Face Recognition
- Real-time **Face Recognition** screen (implementation included but not completed).
- Integrates Android Camera and ML features for detecting and recognizing faces.

---

## 🛠 Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Architecture:** Clean Architecture + MVVM
- **Navigation:** Jetpack Navigation Component (Single Activity)
- **Data Storage:** Room Database
- **Networking:** Retrofit, OkHttp
- **Image Loading:** Coil
- **Pagination:** Paging 3
- **Face Recognition:** CameraX + ML Kit (or custom implementation)

---

## 🛠 Screenshots

![alt text](Screenshot_20250424_112111-1.png)

![alt text](Screenshot_20250424_112653-1.png)
