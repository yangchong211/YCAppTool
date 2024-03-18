package com.yc.toolutils.sntp;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/5/11
 *     desc   : 缓存接口
 *     revise :
 * </pre>
 */
public interface CacheInterface {

    String KEY_CACHED_BOOT_TIME = "com.yc.ntptime.cached_boot_time";
    String KEY_CACHED_DEVICE_UPTIME = "com.yc.ntptime.cached_device_uptime";
    String KEY_CACHED_NTP_TIME = "com.yc.ntptime.cached_ntp_time";

    void put(String key, long value);

    long get(String key, long defaultValue);

    void clear();


}
