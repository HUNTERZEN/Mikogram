# Mikogram — High-Performance Telegram Client for Android

**Mikogram** is a highly optimized, customized fork of the official Telegram Android client. Designed for users who demand visual excellence, fine-grained personalization, and maximum performance, Mikogram integrates cutting-edge features while keeping the security and core messaging features of official Telegram intact.

---

## 🚀 Key Features

### ⚡ Download Speed Boost
* **Parallel Connections:** Accelerates media and file downloads by opening up to 32 parallel network channels, bypassing standard client throttling.
* **Smart Buffering:** Optimized chunk requests dynamically adjust according to network conditions.

### 📊 Real-Time Network Speed Monitor
* **Dynamic Title Bar Display:** Displays real-time download and upload transfer rates directly in the main chat title bar (e.g., `↓ 2.4 MB/s  ↑ 180 KB/s`).
* **Low Overhead:** Runs a highly efficient polling loop using native traffic stats APIs, minimizing battery drain.


---

## 🛠️ Build and Compilation

### Prerequisites
To build Mikogram, you will need:
* **JDK 17** (strictly required due to Kotlin compiler compatibility)
* Android NDK **28.2.13676358** (recommended)

### Local Configuration
1. Initialize your local SDK paths by creating a `local.properties` file in the root directory:
   ```properties
   sdk.dir=/opt/android-sdk
   ```
2. Set up your Firebase settings by downloading `google-services.json` from your Firebase console and placing it in the same directory as the project.

### Building
To compile a standalone debug variant of the app, run:
```bash
JAVA_HOME=/usr/lib/jvm/java-17-openjdk ./gradlew :TMessagesProj_AppStandalone:assembleAfatDebug
```

For release builds:
```bash
JAVA_HOME=/usr/lib/jvm/java-17-openjdk ./gradlew :TMessagesProj_AppStandalone:assembleAfatRelease
```

---

## 🔒 Security & Privacy
Mikogram operates entirely client-side. Your login credentials, chat databases, and media files are stored locally and encrypted using the official Telegram protocols (MTProto). All custom preferences (e.g. Speed Boost, AMOLED mode) are stored locally in Android's private storage.
