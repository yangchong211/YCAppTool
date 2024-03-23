package com.yc.toolutils.sntp;

import android.os.SystemClock;

import com.yc.toolutils.system.AppLogUtils;

import static com.yc.toolutils.sntp.CacheInterface.KEY_CACHED_BOOT_TIME;
import static com.yc.toolutils.sntp.CacheInterface.KEY_CACHED_DEVICE_UPTIME;
import static com.yc.toolutils.sntp.CacheInterface.KEY_CACHED_NTP_TIME;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/5/11
 *     desc   : 广播
 *     revise :
 * </pre>
 */
public class DiskCacheClient {

    private static final String TAG = DiskCacheClient.class.getSimpleName();
    private CacheInterface cacheInterface = null;

    void enableCacheInterface(CacheInterface cacheInterface) {
        this.cacheInterface = cacheInterface;
    }

    void clearCachedInfo() {
        clearCachedInfo(this.cacheInterface);
    }

    void clearCachedInfo(CacheInterface cacheInterface) {
        if (cacheInterface != null) {
            cacheInterface.clear();
        }
    }

    void cacheTrueTimeInfo(NtpClientHelper sntpClient) {
        if (cacheUnavailable()) {
            return;
        }

        long cachedSntpTime = sntpClient.getCachedNtpTime();
        long cachedDeviceUptime = sntpClient.getCachedDeviceUptime();
        long bootTime = cachedSntpTime - cachedDeviceUptime;

        AppLogUtils.d(TAG,
                String.format("Caching true time info to disk sntp [%s] device [%s] boot [%s]",
                        cachedSntpTime,
                        cachedDeviceUptime,
                        bootTime));

        cacheInterface.put(KEY_CACHED_BOOT_TIME, bootTime);
        cacheInterface.put(KEY_CACHED_DEVICE_UPTIME, cachedDeviceUptime);
        cacheInterface.put(KEY_CACHED_NTP_TIME, cachedSntpTime);

    }

    boolean isTrueTimeCachedFromAPreviousBoot() {
        if (cacheUnavailable()) {
            return false;
        }

        long cachedBootTime = cacheInterface.get(KEY_CACHED_BOOT_TIME, 0L);
        if (cachedBootTime == 0) {
            return false;
        }
        // has boot time changed (simple check)
        boolean bootTimeChanged = SystemClock.elapsedRealtime() < getCachedDeviceUptime();
        AppLogUtils.i(TAG, "---- boot time changed " + bootTimeChanged);
        return !bootTimeChanged;
    }

    long getCachedDeviceUptime() {
        if (cacheUnavailable()) {
            return 0L;
        }

        return cacheInterface.get(KEY_CACHED_DEVICE_UPTIME, 0L);
    }

    long getCachedSntpTime() {
        if (cacheUnavailable()) {
            return 0L;
        }

        return cacheInterface.get(KEY_CACHED_NTP_TIME, 0L);
    }

    private boolean cacheUnavailable() {
        if (cacheInterface == null) {
            AppLogUtils.w(TAG, "Cannot use disk caching strategy for TrueTime. CacheInterface unavailable");
            return true;
        }
        return false;
    }
}
