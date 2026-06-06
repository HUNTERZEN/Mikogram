# Mikogram — Technical Modifications & Extensions

This document outlines the changes applied to the codebase to convert the standard Telegram Android client into the high-performance **Mikogram** client.

---

## 🆕 Added Components

### 1. Configuration Store
* **Path:** `TMessagesProj/src/main/java/org/telegram/messenger/MikugramConfig.java`
* Manages user preferences for custom features (`showNetworkSpeed`, `networkSpeedType`, `downloadSpeedBoost`, `darkAmoled`).
* Persisted using Android `SharedPreferences`.

### 2. Network Speed Monitor
* **Path:** `TMessagesProj/src/main/java/org/telegram/messenger/NetworkSpeedMonitor.java`
* Leverages Android's traffic statistics (`TrafficStats`) to calculate exact bandwidth usage on a 1-second interval.
* Emits real-time data updates via `NotificationCenter`.

---

## ✏️ Key Code Modifications

### 1. Application Name & UI Branding
* **`strings.xml`:** Renamed core string keys (`AppName`, `AppNameBeta`, `MikugramPreferences`, `MikugramVersion`) to **Mikogram**.
* **`DialogsActivity.java`:** Removed the default Telegram ImageSpan title layout, replacing it with the text string corresponding to the app name.
* **`DialogStoriesCell.java`:** Changed `telegramLogoView` from an `ImageView` displaying the Telegram drawable to a `SimpleTextView` displaying the **Mikogram** branding.
* **`SettingsActivity.java`:** Set the display version prefix to read **Mikogram**.

### 2. Theme Customization (Dark AMOLED)
* **`Theme.java`:** Overrode standard layout backgrounds to pure black `#000000` (for gray/white window frames, dialog backgrounds, and wallpaper backgrounds) when `darkAmoled` is enabled and a dark theme is active.
* **`ThemeActivity.java`:** Integrated the `darkAmoledRow` setting toggle immediately under the horizontal list of themes in Chat Settings for quick user adjustments.

### 3. File Downloads (Speed Boost)
* **`FileLoadOperation.java`:** Integrated connection pooling limits. When `downloadSpeedBoost` is enabled, the maximum number of parallel chunk requests is increased from `4` to `32`.

### 4. Build Environment Configuration
* **`build.gradle`:** Updated the NDK configuration parameters to use version `28.2.13676358` to match target build tool chains.
