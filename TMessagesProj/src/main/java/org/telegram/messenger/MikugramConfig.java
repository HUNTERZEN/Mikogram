package org.telegram.messenger;

import android.app.Activity;
import android.content.SharedPreferences;

public class MikugramConfig {

    private static final String PREFS_NAME = "mikugramconfig";

    // ── General ──
    private static final String KEY_USE_SYSTEM_EMOJI = "use_system_emoji";
    private static final String KEY_DISABLE_NUMBER_ROUNDING = "disable_number_rounding";
    private static final String KEY_ASK_BEFORE_CALL = "ask_before_call";
    private static final String KEY_OPEN_ARCHIVE_ON_PULL = "open_archive_on_pull";
    private static final String KEY_PREFER_IPV6 = "prefer_ipv6";

    // ── Chats ──
    private static final String KEY_IGNORE_BLOCKED = "ignore_blocked";
    private static final String KEY_HIDE_KEYBOARD_ON_SCROLL = "hide_keyboard_on_scroll";
    private static final String KEY_DISABLE_GREETING_STICKER = "disable_greeting_sticker";
    private static final String KEY_DISABLE_JUMP_TO_NEXT = "disable_jump_to_next";
    private static final String KEY_DISABLE_VOICE_AUTOPLAY = "disable_voice_autoplay";
    private static final String KEY_CONFIRM_AV_MESSAGE = "confirm_av_message";
    private static final String KEY_AUTO_PAUSE_VIDEO = "auto_pause_video";
    private static final String KEY_DISABLE_PROXIMITY = "disable_proximity";
    private static final String KEY_DISABLE_INSTANT_CAMERA = "disable_instant_camera";
    private static final String KEY_UNMUTE_WITH_VOLUME = "unmute_with_volume";
    private static final String KEY_DOUBLE_TAP_ACTION = "double_tap_action";
    private static final String KEY_HIDE_TIME_ON_STICKER = "hide_time_on_sticker";

    // ── Appearance ──
    private static final String KEY_DARK_AMOLED = "dark_amoled";
    private static final String KEY_TRANSPARENT_STATUS_BAR = "transparent_status_bar";
    private static final String KEY_TABLET_MODE = "tablet_mode";
    private static final String KEY_DISABLE_APPBAR_SHADOW = "disable_appbar_shadow";
    private static final String KEY_MEDIA_PREVIEW = "media_preview";
    private static final String KEY_FORMAT_TIME_WITH_SECONDS = "format_time_with_seconds";

    // ── Network & Experimental ──
    private static final String KEY_SHOW_NETWORK_SPEED = "show_network_speed";
    private static final String KEY_NETWORK_SPEED_TYPE = "network_speed_type";
    private static final String KEY_DOWNLOAD_SPEED_BOOST = "download_speed_boost";
    private static final String KEY_SHOW_RPC_ERROR = "show_rpc_error";
    private static final String KEY_MAP_DRIFTING_FIX = "map_drifting_fix";

    // ── Constants: Double-tap actions ──
    public static final int DOUBLE_TAP_NONE = 0;
    public static final int DOUBLE_TAP_REACTION = 1;
    public static final int DOUBLE_TAP_REPLY = 2;
    public static final int DOUBLE_TAP_SAVE = 3;
    public static final int DOUBLE_TAP_EDIT = 4;

    // ── Constants: Tablet mode ──
    public static final int TABLET_AUTO = 0;
    public static final int TABLET_ENABLE = 1;
    public static final int TABLET_DISABLE = 2;

    // ── Constants: Network speed type ──
    public static final int SPEED_TYPE_BOTH = 0;
    public static final int SPEED_TYPE_DOWNLOAD = 1;
    public static final int SPEED_TYPE_UPLOAD = 2;

    // ── Cached values: General ──
    private static boolean useSystemEmoji;
    private static boolean disableNumberRounding;
    private static boolean askBeforeCall;
    private static boolean openArchiveOnPull;
    private static boolean preferIPv6;

    // ── Cached values: Chats ──
    private static boolean ignoreBlocked;
    private static boolean hideKeyboardOnScroll;
    private static boolean disableGreetingSticker;
    private static boolean disableJumpToNext;
    private static boolean disableVoiceAutoplay;
    private static boolean confirmAVMessage;
    private static boolean autoPauseVideo;
    private static boolean disableProximity;
    private static boolean disableInstantCamera;
    private static boolean unmuteWithVolume;
    private static int doubleTapAction;
    private static boolean hideTimeOnSticker;

    // ── Cached values: Appearance ──
    private static boolean darkAmoled;
    private static boolean transparentStatusBar;
    private static int tabletMode;
    private static boolean disableAppBarShadow;
    private static boolean mediaPreview;
    private static boolean formatTimeWithSeconds;

    // ── Cached values: Network & Experimental ──
    private static boolean showNetworkSpeed;
    private static int networkSpeedType;
    private static boolean downloadSpeedBoost;
    private static boolean showRPCError;
    private static boolean mapDriftingFix;

    private static boolean loaded;

    private static SharedPreferences getPrefs() {
        return ApplicationLoader.applicationContext.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
    }

    public static void loadConfig() {
        if (loaded) return;
        SharedPreferences prefs = getPrefs();

        // General
        useSystemEmoji = prefs.getBoolean(KEY_USE_SYSTEM_EMOJI, false);
        disableNumberRounding = prefs.getBoolean(KEY_DISABLE_NUMBER_ROUNDING, false);
        askBeforeCall = prefs.getBoolean(KEY_ASK_BEFORE_CALL, true);
        openArchiveOnPull = prefs.getBoolean(KEY_OPEN_ARCHIVE_ON_PULL, false);
        preferIPv6 = prefs.getBoolean(KEY_PREFER_IPV6, false);

        // Chats
        ignoreBlocked = prefs.getBoolean(KEY_IGNORE_BLOCKED, false);
        hideKeyboardOnScroll = prefs.getBoolean(KEY_HIDE_KEYBOARD_ON_SCROLL, false);
        disableGreetingSticker = prefs.getBoolean(KEY_DISABLE_GREETING_STICKER, false);
        disableJumpToNext = prefs.getBoolean(KEY_DISABLE_JUMP_TO_NEXT, false);
        disableVoiceAutoplay = prefs.getBoolean(KEY_DISABLE_VOICE_AUTOPLAY, false);
        confirmAVMessage = prefs.getBoolean(KEY_CONFIRM_AV_MESSAGE, false);
        autoPauseVideo = prefs.getBoolean(KEY_AUTO_PAUSE_VIDEO, true);
        disableProximity = prefs.getBoolean(KEY_DISABLE_PROXIMITY, false);
        disableInstantCamera = prefs.getBoolean(KEY_DISABLE_INSTANT_CAMERA, false);
        unmuteWithVolume = prefs.getBoolean(KEY_UNMUTE_WITH_VOLUME, true);
        doubleTapAction = prefs.getInt(KEY_DOUBLE_TAP_ACTION, DOUBLE_TAP_REACTION);
        hideTimeOnSticker = prefs.getBoolean(KEY_HIDE_TIME_ON_STICKER, false);

        // Appearance
        darkAmoled = prefs.getBoolean(KEY_DARK_AMOLED, false);
        transparentStatusBar = prefs.getBoolean(KEY_TRANSPARENT_STATUS_BAR, false);
        tabletMode = prefs.getInt(KEY_TABLET_MODE, TABLET_AUTO);
        disableAppBarShadow = prefs.getBoolean(KEY_DISABLE_APPBAR_SHADOW, false);
        mediaPreview = prefs.getBoolean(KEY_MEDIA_PREVIEW, true);
        formatTimeWithSeconds = prefs.getBoolean(KEY_FORMAT_TIME_WITH_SECONDS, false);

        // Network & Experimental
        showNetworkSpeed = prefs.getBoolean(KEY_SHOW_NETWORK_SPEED, false);
        networkSpeedType = prefs.getInt(KEY_NETWORK_SPEED_TYPE, SPEED_TYPE_BOTH);
        downloadSpeedBoost = prefs.getBoolean(KEY_DOWNLOAD_SPEED_BOOST, false);
        showRPCError = prefs.getBoolean(KEY_SHOW_RPC_ERROR, false);
        mapDriftingFix = prefs.getBoolean(KEY_MAP_DRIFTING_FIX, false);

        loaded = true;
    }

    // ═══════════════════════════════════════════
    //  GENERAL
    // ═══════════════════════════════════════════

    public static boolean isUseSystemEmoji() {
        loadConfig();
        return useSystemEmoji;
    }

    public static void toggleUseSystemEmoji() {
        useSystemEmoji = !useSystemEmoji;
        getPrefs().edit().putBoolean(KEY_USE_SYSTEM_EMOJI, useSystemEmoji).apply();
    }

    public static boolean isDisableNumberRounding() {
        loadConfig();
        return disableNumberRounding;
    }

    public static void toggleDisableNumberRounding() {
        disableNumberRounding = !disableNumberRounding;
        getPrefs().edit().putBoolean(KEY_DISABLE_NUMBER_ROUNDING, disableNumberRounding).apply();
    }

    public static boolean isAskBeforeCall() {
        loadConfig();
        return askBeforeCall;
    }

    public static void toggleAskBeforeCall() {
        askBeforeCall = !askBeforeCall;
        getPrefs().edit().putBoolean(KEY_ASK_BEFORE_CALL, askBeforeCall).apply();
    }

    public static boolean isOpenArchiveOnPull() {
        loadConfig();
        return openArchiveOnPull;
    }

    public static void toggleOpenArchiveOnPull() {
        openArchiveOnPull = !openArchiveOnPull;
        getPrefs().edit().putBoolean(KEY_OPEN_ARCHIVE_ON_PULL, openArchiveOnPull).apply();
    }

    public static boolean isPreferIPv6() {
        loadConfig();
        return preferIPv6;
    }

    public static void togglePreferIPv6() {
        preferIPv6 = !preferIPv6;
        getPrefs().edit().putBoolean(KEY_PREFER_IPV6, preferIPv6).apply();
    }

    // ═══════════════════════════════════════════
    //  CHATS
    // ═══════════════════════════════════════════

    public static boolean isIgnoreBlocked() {
        loadConfig();
        return ignoreBlocked;
    }

    public static void toggleIgnoreBlocked() {
        ignoreBlocked = !ignoreBlocked;
        getPrefs().edit().putBoolean(KEY_IGNORE_BLOCKED, ignoreBlocked).apply();
    }

    public static boolean isHideKeyboardOnScroll() {
        loadConfig();
        return hideKeyboardOnScroll;
    }

    public static void toggleHideKeyboardOnScroll() {
        hideKeyboardOnScroll = !hideKeyboardOnScroll;
        getPrefs().edit().putBoolean(KEY_HIDE_KEYBOARD_ON_SCROLL, hideKeyboardOnScroll).apply();
    }

    public static boolean isDisableGreetingSticker() {
        loadConfig();
        return disableGreetingSticker;
    }

    public static void toggleDisableGreetingSticker() {
        disableGreetingSticker = !disableGreetingSticker;
        getPrefs().edit().putBoolean(KEY_DISABLE_GREETING_STICKER, disableGreetingSticker).apply();
    }

    public static boolean isDisableJumpToNext() {
        loadConfig();
        return disableJumpToNext;
    }

    public static void toggleDisableJumpToNext() {
        disableJumpToNext = !disableJumpToNext;
        getPrefs().edit().putBoolean(KEY_DISABLE_JUMP_TO_NEXT, disableJumpToNext).apply();
    }

    public static boolean isDisableVoiceAutoplay() {
        loadConfig();
        return disableVoiceAutoplay;
    }

    public static void toggleDisableVoiceAutoplay() {
        disableVoiceAutoplay = !disableVoiceAutoplay;
        getPrefs().edit().putBoolean(KEY_DISABLE_VOICE_AUTOPLAY, disableVoiceAutoplay).apply();
    }

    public static boolean isConfirmAVMessage() {
        loadConfig();
        return confirmAVMessage;
    }

    public static void toggleConfirmAVMessage() {
        confirmAVMessage = !confirmAVMessage;
        getPrefs().edit().putBoolean(KEY_CONFIRM_AV_MESSAGE, confirmAVMessage).apply();
    }

    public static boolean isAutoPauseVideo() {
        loadConfig();
        return autoPauseVideo;
    }

    public static void toggleAutoPauseVideo() {
        autoPauseVideo = !autoPauseVideo;
        getPrefs().edit().putBoolean(KEY_AUTO_PAUSE_VIDEO, autoPauseVideo).apply();
    }

    public static boolean isDisableProximity() {
        loadConfig();
        return disableProximity;
    }

    public static void toggleDisableProximity() {
        disableProximity = !disableProximity;
        getPrefs().edit().putBoolean(KEY_DISABLE_PROXIMITY, disableProximity).apply();
    }

    public static boolean isDisableInstantCamera() {
        loadConfig();
        return disableInstantCamera;
    }

    public static void toggleDisableInstantCamera() {
        disableInstantCamera = !disableInstantCamera;
        getPrefs().edit().putBoolean(KEY_DISABLE_INSTANT_CAMERA, disableInstantCamera).apply();
    }

    public static boolean isUnmuteWithVolume() {
        loadConfig();
        return unmuteWithVolume;
    }

    public static void toggleUnmuteWithVolume() {
        unmuteWithVolume = !unmuteWithVolume;
        getPrefs().edit().putBoolean(KEY_UNMUTE_WITH_VOLUME, unmuteWithVolume).apply();
    }

    public static int getDoubleTapAction() {
        loadConfig();
        return doubleTapAction;
    }

    public static void setDoubleTapAction(int action) {
        doubleTapAction = action;
        getPrefs().edit().putInt(KEY_DOUBLE_TAP_ACTION, action).apply();
    }

    public static boolean isHideTimeOnSticker() {
        loadConfig();
        return hideTimeOnSticker;
    }

    public static void toggleHideTimeOnSticker() {
        hideTimeOnSticker = !hideTimeOnSticker;
        getPrefs().edit().putBoolean(KEY_HIDE_TIME_ON_STICKER, hideTimeOnSticker).apply();
    }

    // ═══════════════════════════════════════════
    //  APPEARANCE
    // ═══════════════════════════════════════════

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

    public static boolean isTransparentStatusBar() {
        loadConfig();
        return transparentStatusBar;
    }

    public static void toggleTransparentStatusBar() {
        transparentStatusBar = !transparentStatusBar;
        getPrefs().edit().putBoolean(KEY_TRANSPARENT_STATUS_BAR, transparentStatusBar).apply();
    }

    public static int getTabletMode() {
        loadConfig();
        return tabletMode;
    }

    public static void setTabletMode(int mode) {
        tabletMode = mode;
        getPrefs().edit().putInt(KEY_TABLET_MODE, mode).apply();
    }

    public static boolean isDisableAppBarShadow() {
        loadConfig();
        return disableAppBarShadow;
    }

    public static void toggleDisableAppBarShadow() {
        disableAppBarShadow = !disableAppBarShadow;
        getPrefs().edit().putBoolean(KEY_DISABLE_APPBAR_SHADOW, disableAppBarShadow).apply();
    }

    public static boolean isMediaPreview() {
        loadConfig();
        return mediaPreview;
    }

    public static void toggleMediaPreview() {
        mediaPreview = !mediaPreview;
        getPrefs().edit().putBoolean(KEY_MEDIA_PREVIEW, mediaPreview).apply();
    }

    public static boolean isFormatTimeWithSeconds() {
        loadConfig();
        return formatTimeWithSeconds;
    }

    public static void toggleFormatTimeWithSeconds() {
        formatTimeWithSeconds = !formatTimeWithSeconds;
        getPrefs().edit().putBoolean(KEY_FORMAT_TIME_WITH_SECONDS, formatTimeWithSeconds).apply();
    }

    // ═══════════════════════════════════════════
    //  NETWORK & EXPERIMENTAL
    // ═══════════════════════════════════════════

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

    public static boolean isDownloadSpeedBoost() {
        loadConfig();
        return downloadSpeedBoost;
    }

    public static void toggleDownloadSpeedBoost() {
        downloadSpeedBoost = !downloadSpeedBoost;
        getPrefs().edit().putBoolean(KEY_DOWNLOAD_SPEED_BOOST, downloadSpeedBoost).apply();
    }

    public static void setDownloadSpeedBoost(boolean value) {
        downloadSpeedBoost = value;
        getPrefs().edit().putBoolean(KEY_DOWNLOAD_SPEED_BOOST, value).apply();
    }

    public static boolean isShowRPCError() {
        loadConfig();
        return showRPCError;
    }

    public static void toggleShowRPCError() {
        showRPCError = !showRPCError;
        getPrefs().edit().putBoolean(KEY_SHOW_RPC_ERROR, showRPCError).apply();
    }

    public static boolean isMapDriftingFix() {
        loadConfig();
        return mapDriftingFix;
    }

    public static void toggleMapDriftingFix() {
        mapDriftingFix = !mapDriftingFix;
        getPrefs().edit().putBoolean(KEY_MAP_DRIFTING_FIX, mapDriftingFix).apply();
    }
}
