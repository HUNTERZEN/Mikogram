package org.telegram.messenger;

import android.net.TrafficStats;
import android.os.Handler;
import android.os.Looper;

import java.util.Locale;

public class NetworkSpeedMonitor {

    private static volatile NetworkSpeedMonitor instance;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private long lastTotalRxBytes;
    private long lastTotalTxBytes;
    private long lastTimestamp;
    private long downloadSpeed; // bytes per second
    private long uploadSpeed;   // bytes per second
    private boolean running;

    private static final int UPDATE_INTERVAL_MS = 1000;

    public static NetworkSpeedMonitor getInstance() {
        if (instance == null) {
            synchronized (NetworkSpeedMonitor.class) {
                if (instance == null) {
                    instance = new NetworkSpeedMonitor();
                }
            }
        }
        return instance;
    }

    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            if (!running) return;

            long currentRxBytes = TrafficStats.getUidRxBytes(android.os.Process.myUid());
            long currentTxBytes = TrafficStats.getUidTxBytes(android.os.Process.myUid());
            long currentTimestamp = System.currentTimeMillis();

            if (currentRxBytes == TrafficStats.UNSUPPORTED || currentTxBytes == TrafficStats.UNSUPPORTED) {
                downloadSpeed = 0;
                uploadSpeed = 0;
            } else if (lastTimestamp > 0) {
                long timeDelta = currentTimestamp - lastTimestamp;
                if (timeDelta > 0) {
                    downloadSpeed = (currentRxBytes - lastTotalRxBytes) * 1000 / timeDelta;
                    uploadSpeed = (currentTxBytes - lastTotalTxBytes) * 1000 / timeDelta;
                    if (downloadSpeed < 0) downloadSpeed = 0;
                    if (uploadSpeed < 0) uploadSpeed = 0;
                }
            }

            lastTotalRxBytes = currentRxBytes;
            lastTotalTxBytes = currentTxBytes;
            lastTimestamp = currentTimestamp;

            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.mikugramNetworkSpeedUpdated);

            handler.postDelayed(this, UPDATE_INTERVAL_MS);
        }
    };

    public void start() {
        if (running) return;
        running = true;
        lastTotalRxBytes = TrafficStats.getUidRxBytes(android.os.Process.myUid());
        lastTotalTxBytes = TrafficStats.getUidTxBytes(android.os.Process.myUid());
        lastTimestamp = System.currentTimeMillis();
        handler.postDelayed(updateRunnable, UPDATE_INTERVAL_MS);
    }

    public void stop() {
        running = false;
        handler.removeCallbacks(updateRunnable);
        downloadSpeed = 0;
        uploadSpeed = 0;
    }

    public long getDownloadSpeed() {
        return downloadSpeed;
    }

    public long getUploadSpeed() {
        return uploadSpeed;
    }

    public String getFormattedDownloadSpeed() {
        return formatSpeed(downloadSpeed);
    }

    public String getFormattedUploadSpeed() {
        return formatSpeed(uploadSpeed);
    }

    public String getFormattedSpeed() {
        int type = MikugramConfig.getNetworkSpeedType();
        switch (type) {
            case MikugramConfig.SPEED_TYPE_DOWNLOAD:
                return "↓ " + formatSpeed(downloadSpeed);
            case MikugramConfig.SPEED_TYPE_UPLOAD:
                return "↑ " + formatSpeed(uploadSpeed);
            default:
                return "↓ " + formatSpeed(downloadSpeed) + "  ↑ " + formatSpeed(uploadSpeed);
        }
    }

    public static String formatSpeed(long bytesPerSecond) {
        if (bytesPerSecond < 1024) {
            return bytesPerSecond + " B/s";
        } else if (bytesPerSecond < 1024 * 1024) {
            return String.format(Locale.US, "%.1f KB/s", bytesPerSecond / 1024.0);
        } else {
            return String.format(Locale.US, "%.2f MB/s", bytesPerSecond / (1024.0 * 1024.0));
        }
    }

    public boolean hasSpeed() {
        return downloadSpeed > 0 || uploadSpeed > 0;
    }
}
