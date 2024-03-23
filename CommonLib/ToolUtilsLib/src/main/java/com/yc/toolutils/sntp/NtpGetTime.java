package com.yc.toolutils.sntp;

import android.content.Context;
import android.os.SystemClock;

import com.yc.toolutils.system.AppLogUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/5/11
 *     desc   : ntp时间校验工具
 *     revise :
 * </pre>
 */
public final class NtpGetTime {

    private static final String TAG = NtpGetTime.class.getSimpleName();

    private static final NtpGetTime INSTANCE = new NtpGetTime();
    private static final DiskCacheClient DISK_CACHE_CLIENT = new DiskCacheClient();
    private static final NtpClientHelper NTP_CLIENT_HELPER = new NtpClientHelper();

    private static float rootDelayMax = 100;
    private static float rootDispersionMax = 100;
    private static int serverResponseDelayMax = 750;
    private static int udpSocketTimeoutInMillis = 30_000;
    /**
     * 中国科学技术大学NTP服务器
     */
    private String ntpHost = "time.ustc.edu.cn";

    public static Date now() {
        if (!isInitialized()) {
            throw new IllegalStateException("You need to call init() on TrueTime at least once.");
        }
        long cachedNtpTime = getCachedNtpTime();
        long cachedDeviceUptime = getCachedDeviceUptime();
        long deviceUptime = SystemClock.elapsedRealtime();
        long now = cachedNtpTime + (deviceUptime - cachedDeviceUptime);
        return new Date(now);
    }

    public static boolean isInitialized() {
        return NTP_CLIENT_HELPER.wasInitialized() || DISK_CACHE_CLIENT.isTrueTimeCachedFromAPreviousBoot();
    }

    public static NtpGetTime build() {
        return INSTANCE;
    }

    public void initialize() throws IOException {
        initialize(ntpHost);
    }

    /**
     * 在SharedPreferences中缓存NtpGetTime初始化信息，这可以帮助避免应用程序杀死时附加的NtpGetTime初始化
     */
    public synchronized NtpGetTime withSharedPreferencesCache(Context context) {
        DISK_CACHE_CLIENT.enableCacheInterface(new SpCacheImpl(context));
        return INSTANCE;
    }

    /**
     * 自定义的NtpGetTime缓存实现。
     */
    public synchronized NtpGetTime withCustomizedCache(CacheInterface cacheInterface) {
        DISK_CACHE_CLIENT.enableCacheInterface(cacheInterface);
        return INSTANCE;
    }

    /**
     * 清除设备重启时缓存的NtpGetTime信息
     */
    public static void clearCachedInfo() {
        DISK_CACHE_CLIENT.clearCachedInfo();
    }

    public synchronized NtpGetTime withConnectionTimeout(int timeoutInMillis) {
        udpSocketTimeoutInMillis = timeoutInMillis;
        return INSTANCE;
    }

    public synchronized NtpGetTime withRootDelayMax(float rootDelayMax) {
        if (rootDelayMax > NtpGetTime.rootDelayMax) {
          String log = String.format(Locale.getDefault(),
              "The recommended max rootDelay value is %f. You are setting it at %f",
                  NtpGetTime.rootDelayMax, rootDelayMax);
          AppLogUtils.w(TAG, log);
        }

        NtpGetTime.rootDelayMax = rootDelayMax;
        return INSTANCE;
    }

    public synchronized NtpGetTime withRootDispersionMax(float rootDispersionMax) {
      if (rootDispersionMax > NtpGetTime.rootDispersionMax) {
        String log = String.format(Locale.getDefault(),
            "The recommended max rootDispersion value is %f. You are setting it at %f",
                NtpGetTime.rootDispersionMax, rootDispersionMax);
        AppLogUtils.w(TAG, log);
      }

      NtpGetTime.rootDispersionMax = rootDispersionMax;
      return INSTANCE;
    }

    public synchronized NtpGetTime withServerResponseDelayMax(int serverResponseDelayInMillis) {
        serverResponseDelayMax = serverResponseDelayInMillis;
        return INSTANCE;
    }

    public synchronized NtpGetTime withNtpHost(String ntpHost) {
        this.ntpHost = ntpHost;
        return INSTANCE;
    }

    // -----------------------------------------------------------------------------------

    protected void initialize(String ntpHost) throws IOException {
        if (isInitialized()) {
            AppLogUtils.i(TAG, "---- TrueTime already initialized from previous boot/init");
            return;
        }

        requestTime(ntpHost);
        saveTrueTimeInfoToDisk();
    }

    void requestTime(String ntpHost) throws IOException {
        NTP_CLIENT_HELPER.requestTime(ntpHost, rootDelayMax, rootDispersionMax,
                serverResponseDelayMax, udpSocketTimeoutInMillis);
    }

    synchronized static void saveTrueTimeInfoToDisk() {
        if (!NTP_CLIENT_HELPER.wasInitialized()) {
            AppLogUtils.i(TAG, "ntp client not available. not caching TrueTime info in disk");
            return;
        }
        DISK_CACHE_CLIENT.cacheTrueTimeInfo(NTP_CLIENT_HELPER);
    }

    void cacheTrueTimeInfo(long[] response) {
        NTP_CLIENT_HELPER.cacheTrueTimeInfo(response);
    }

    private static long getCachedDeviceUptime() {
        long cachedDeviceUptime = NTP_CLIENT_HELPER.wasInitialized()
                ? NTP_CLIENT_HELPER.getCachedDeviceUptime() : DISK_CACHE_CLIENT.getCachedDeviceUptime();
        if (cachedDeviceUptime == 0L) {
            throw new RuntimeException("expected device time from last boot to be cached. couldn't find it.");
        }

        return cachedDeviceUptime;
    }

    private static long getCachedNtpTime() {
        long cachedNtpTime = NTP_CLIENT_HELPER.wasInitialized()
                ? NTP_CLIENT_HELPER.getCachedNtpTime() : DISK_CACHE_CLIENT.getCachedSntpTime();
        if (cachedNtpTime == 0L) {
            throw new RuntimeException("expected ntp time from last boot to be cached. couldn't find it.");
        }
        return cachedNtpTime;
    }

}
