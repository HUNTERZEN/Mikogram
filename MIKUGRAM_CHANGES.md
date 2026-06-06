# Mikugram – All Changes & Additions

> Summary of every file created or modified to transform the official Telegram Android client into **Mikugram**.

---

## 🆕 New Files Created

### 1. `MikugramConfig.java`
**Path:** `TMessagesProj/src/main/java/org/telegram/messenger/MikugramConfig.java`

Singleton class that stores all Mikugram-specific user preferences using `SharedPreferences`.

- `darkAmoled` – Toggle for pure black AMOLED backgrounds
- `showNetworkSpeed` – Toggle for network speed display
- `networkSpeedType` – 0 = both, 1 = download only, 2 = upload only
- Methods: `isDarkAmoled()`, `toggleDarkAmoled()`, `isShowNetworkSpeed()`, `toggleShowNetworkSpeed()`, `getNetworkSpeedType()`, `setNetworkSpeedType()`

---

### 2. `NetworkSpeedMonitor.java`
**Path:** `TMessagesProj/src/main/java/org/telegram/messenger/NetworkSpeedMonitor.java`

Singleton that monitors real-time network speed using Android's `TrafficStats` API.

- Polls every 1 second for the app's UID traffic
- Calculates download/upload speed in bytes/sec
- Formats as human-readable: `B/s`, `KB/s`, `MB/s`
- Broadcasts updates via `NotificationCenter.mikugramNetworkSpeedUpdated`
- Methods: `start()`, `stop()`, `getFormattedSpeed()`, `hasSpeed()`

---

### 3. `MikugramPreferencesActivity.java`
**Path:** `TMessagesProj/src/main/java/org/telegram/ui/MikugramPreferencesActivity.java`

Full settings page for Mikugram preferences, following Telegram's standard `RecyclerListView` + adapter pattern.

**Sections:**
| Section | Items |
|---------|-------|
| **Appearance** | Dark AMOLED toggle + info text |
| **Network** | Show Network Speed toggle + info text |
| **About** | Mikugram Version display |

- Toggling Dark AMOLED forces a theme refresh via `Theme.reloadAllResources()` + `NotificationCenter.needSetDayNightTheme`
- Toggling Network Speed starts/stops the `NetworkSpeedMonitor`

---

### 4. `local.properties`
**Path:** `local.properties`

Created to point the project to the installed Android SDK.
```
sdk.dir=/opt/android-sdk
```

---

## ✏️ Modified Files

### 5. `strings.xml`
**Path:** `TMessagesProj/src/main/res/values/strings.xml`

| Change | Before | After |
|--------|--------|-------|
| App name | `Telegram` | `Mikugram` |
| Beta name | `Telegram Beta` | `Mikugram Beta` |

**Added string resources:**
- `MikugramPreferences` – "Mikugram Preferences"
- `MikugramPreferencesInfo` – "Customize your Mikugram experience"
- `MikugramAppearance` – "Appearance"
- `MikugramDarkAmoled` – "Dark AMOLED"
- `MikugramDarkAmoledInfo` – "Use pure black backgrounds for OLED screens..."
- `MikugramNetwork` – "Network"
- `MikugramShowNetworkSpeed` – "Show Network Speed"
- `MikugramShowNetworkSpeedInfo` – "Display download/upload speed in the title bar"
- `MikugramAbout` – "About"
- `MikugramVersion` – "Mikugram Version"

---

### 6. `gradle.properties`
**Path:** `gradle.properties`

| Change | Before | After |
|--------|--------|-------|
| Package name | `org.telegram.messenger` | `org.mikugram.messenger` |

This allows Mikugram to be installed alongside official Telegram.

---

### 7. `NotificationCenter.java`
**Path:** `TMessagesProj/src/main/java/org/telegram/messenger/NotificationCenter.java`

**Added:**
```java
//Mikugram
public static final int mikugramNetworkSpeedUpdated = totalEvents++;
```
New event ID used by `NetworkSpeedMonitor` to broadcast speed updates to UI observers.

---

### 8. `ApplicationLoader.java`
**Path:** `TMessagesProj/src/main/java/org/telegram/messenger/ApplicationLoader.java`

**Added** in `postInitApplication()`:
```java
MikugramConfig.loadConfig();
```
Loads Mikugram preferences early during app startup, right after `SharedConfig.loadConfig()`.

---

### 9. `SettingsActivity.java`
**Path:** `TMessagesProj/src/main/java/org/telegram/ui/SettingsActivity.java`

**Changes:**

1. **Added "Mikugram Preferences" menu entry** in `fillItems()`:
   - Pink/purple gradient icon (`0xFFE91E8C` → `0xFFAB47BC`)
   - Uses `settings_premium` drawable
   - Placed at the top of settings list (before Account, Chat Settings, etc.)
   - ID: `100`

2. **Added click handler** in `onClick()`:
   ```java
   case 100:
       presentFragment(new MikugramPreferencesActivity());
       break;
   ```

3. **Updated version string** in `getVersionName()`:
   - Before: `Telegram for Android v12.7.3 (675) universal...`
   - After: `Mikugram v12.7.3 (675) universal...`

---

### 10. `Theme.java`
**Path:** `TMessagesProj/src/main/java/org/telegram/ui/ActionBar/Theme.java`

**Changes:**

1. **Added import:** `import org.telegram.messenger.MikugramConfig;`

2. **Added AMOLED override** at the end of `getColor()` method:
   - When `MikugramConfig.isDarkAmoled()` is `true` AND the current theme is dark:
   - Overrides these color keys to pure black (`0xFF000000`):
     - `key_windowBackgroundWhite`
     - `key_windowBackgroundGray`
     - `key_actionBarDefault`
     - `key_actionBarDefaultArchived`
     - `key_dialogBackground`
     - `key_windowBackgroundUnchecked`
     - `key_windowBackgroundChecked`
     - `key_chats_menuBackground`
     - `key_chat_wallpaper`
   - Overrides to near-black (`0xFF0A0A0A`):
     - `key_graySection`
     - `key_actionBarDefaultSubmenuBackground`

---

### 11. `DialogsActivity.java`
**Path:** `TMessagesProj/src/main/java/org/telegram/ui/DialogsActivity.java`

**Changes:**

1. **Added imports:**
   ```java
   import org.telegram.messenger.MikugramConfig;
   import org.telegram.messenger.NetworkSpeedMonitor;
   ```

2. **Registered observer** for `mikugramNetworkSpeedUpdated` in global observers group

3. **Added notification handler** in `didReceivedNotification()`:
   - When speed is updated and the feature is enabled, sets the action bar subtitle to show formatted speed (e.g., `↓ 1.5 MB/s  ↑ 256.0 KB/s`)

4. **Auto-start speed monitor** in `onFragmentCreate()` if enabled in config

---

### 12. `TMessagesProj/build.gradle`
**Path:** `TMessagesProj/build.gradle`

| Change | Before | After |
|--------|--------|-------|
| NDK version | `27.2.12479018` | `28.2.13676358` |

Changed because NDK 27.2 installation was empty/incomplete on this system.

---

## 🏗️ Build Notes

- **JDK Requirement:** Must use JDK 17 (not JDK 26) due to Kotlin compiler compatibility
  ```bash
  JAVA_HOME=/usr/lib/jvm/java-17-openjdk ./gradlew :TMessagesProj_AppStandalone:assembleAfatDebug
  ```
- **API Keys:** Uses default Telegram API ID/Hash. For distribution, get your own from [my.telegram.org](https://my.telegram.org)
- **Signing:** Uses default debug keystore. Generate your own for release builds.

---

## 📁 File Tree Summary

```
Telegram/
├── local.properties                          [NEW]
├── gradle.properties                         [MODIFIED]
├── MIKUGRAM_CHANGES.md                       [NEW - this file]
└── TMessagesProj/
    ├── build.gradle                          [MODIFIED]
    └── src/main/
        ├── java/org/telegram/
        │   ├── messenger/
        │   │   ├── ApplicationLoader.java    [MODIFIED]
        │   │   ├── MikugramConfig.java       [NEW]
        │   │   ├── NetworkSpeedMonitor.java   [NEW]
        │   │   └── NotificationCenter.java   [MODIFIED]
        │   └── ui/
        │       ├── ActionBar/
        │       │   └── Theme.java            [MODIFIED]
        │       ├── DialogsActivity.java      [MODIFIED]
        │       ├── MikugramPreferencesActivity.java  [NEW]
        │       └── SettingsActivity.java     [MODIFIED]
        └── res/values/
            └── strings.xml                   [MODIFIED]
```
