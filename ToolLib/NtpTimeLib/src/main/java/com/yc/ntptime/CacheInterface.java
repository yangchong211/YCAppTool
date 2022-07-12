package com.yc.ntptime;

public interface CacheInterface {

    String KEY_CACHED_BOOT_TIME = "com.yc.ntptime.cached_boot_time";
    String KEY_CACHED_DEVICE_UPTIME = "com.yc.ntptime.cached_device_uptime";
    String KEY_CACHED_SNTP_TIME = "com.yc.ntptime.cached_sntp_time";

    void put(String key, long value);

    long get(String key, long defaultValue);

    void clear();


}
