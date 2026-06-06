package org.telegram.messenger;

import android.app.Activity;
import android.content.SharedPreferences;

public class MikugramConfig {

    private static final String PREFS_NAME = "mikugramconfig";

    // Appearance
    private static final String KEY_DARK_AMOLED = "dark_amoled";

    // Network
    private static final String KEY_SHOW_NETWORK_SPEED = "show_network_speed";
    private static final String KEY_NETWORK_SPEED_TYPE = "network_speed_type"; // 0=both, 1=download, 2=upload

    public static final int SPEED_TYPE_BOTH = 0;
    public static final int SPEED_TYPE_DOWNLOAD = 1;
    public static final int SPEED_TYPE_UPLOAD = 2;

    // Cached values
    private static boolean darkAmoled;
    private static boolean showNetworkSpeed;
    private static int networkSpeedType;
    private static boolean loaded;

    private static SharedPreferences getPrefs() {
        return ApplicationLoader.applicationContext.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
    }

    public static void loadConfig() {
        if (loaded) return;
        SharedPreferences prefs = getPrefs();
        darkAmoled = prefs.getBoolean(KEY_DARK_AMOLED, false);
        showNetworkSpeed = prefs.getBoolean(KEY_SHOW_NETWORK_SPEED, false);
        networkSpeedType = prefs.getInt(KEY_NETWORK_SPEED_TYPE, SPEED_TYPE_BOTH);
        loaded = true;
    }

    // Dark AMOLED
    public static boolean isDarkAmoled() {
        loadConfig();
        return darkAmoled;
    }

    public static void toggleDarkAmoled() {
        darkAmoled = !darkAmoled;
        getPrefs().edit().putBoolean(KEY_DARK_AMOLED, darkAmoled).apply();
    }

    public static void setDarkAmoled(boolean value) {
        darkAmoled = value;
        getPrefs().edit().putBoolean(KEY_DARK_AMOLED, value).apply();
    }

    // Network speed
    public static boolean isShowNetworkSpeed() {
        loadConfig();
        return showNetworkSpeed;
    }

    public static void toggleShowNetworkSpeed() {
        showNetworkSpeed = !showNetworkSpeed;
        getPrefs().edit().putBoolean(KEY_SHOW_NETWORK_SPEED, showNetworkSpeed).apply();
    }

    public static void setShowNetworkSpeed(boolean value) {
        showNetworkSpeed = value;
        getPrefs().edit().putBoolean(KEY_SHOW_NETWORK_SPEED, value).apply();
    }

    public static int getNetworkSpeedType() {
        loadConfig();
        return networkSpeedType;
    }

    public static void setNetworkSpeedType(int type) {
        networkSpeedType = type;
        getPrefs().edit().putInt(KEY_NETWORK_SPEED_TYPE, type).apply();
    }
}
