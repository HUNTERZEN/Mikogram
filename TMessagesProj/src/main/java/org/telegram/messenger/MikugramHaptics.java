package org.telegram.messenger;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.view.HapticFeedbackConstants;
import android.view.View;

/**
 * Haptic feedback utility for Mikogram.
 * Uses Android's system haptic APIs with intensity scaling from MikugramConfig.
 */
public class MikugramHaptics {

    /**
     * Perform a light haptic tick for toggle interactions.
     * Respects the user's vibration enabled/intensity preferences.
     */
    public static void performHapticFeedback(View view) {
        if (!MikugramConfig.isVibrationEnabled() || view == null) return;

        int intensity = MikugramConfig.getVibrationIntensity();
        if (intensity <= 0) return;

        boolean performed = false;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                performed = view.performHapticFeedback(HapticFeedbackConstants.CONFIRM, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
            } else {
                performed = view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
            }
        } catch (Exception ignored) {}

        if (!performed || intensity < 100) {
            vibrateWithIntensity(view.getContext(), intensity);
        }
    }

    /**
     * Perform a subtle tick haptic, useful for slider changes, list clicks, and tabs.
     */
    public static void performTickFeedback(View view) {
        if (!MikugramConfig.isVibrationEnabled() || view == null) return;
        int intensity = MikugramConfig.getVibrationIntensity();
        if (intensity <= 0) return;

        boolean performed = false;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                performed = view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
            } else {
                performed = view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
            }
        } catch (Exception ignored) {}

        if (!performed) {
            vibrateWithIntensity(view.getContext(), Math.min(intensity, 40));
        }
    }

    /**
     * Vibrate with scaled intensity using VibrationEffect on API 26+.
     */
    private static void vibrateWithIntensity(Context context, int intensityPercent) {
        if (context == null) return;

        Vibrator vibrator;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibratorManager manager = (VibratorManager) context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
            if (manager == null) return;
            vibrator = manager.getDefaultVibrator();
        } else {
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }

        if (vibrator == null || !vibrator.hasVibrator()) return;

        int amplitude = Math.max(1, (int) (255 * (intensityPercent / 100f)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(20, amplitude));
        } else {
            vibrator.vibrate(20);
        }
    }
}
