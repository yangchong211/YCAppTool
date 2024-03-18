package com.yc.toolutils.sntp;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;
/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/5/11
 *     desc   : sp缓存接口实现类
 *     revise :
 * </pre>
 */
public class SpCacheImpl implements CacheInterface {

    private static final String KEY_CACHED_SHARED_PREFS = "com.yc.ntptime.shared_preferences";
    private final SharedPreferences sharedPreferences;

    public SpCacheImpl(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY_CACHED_SHARED_PREFS, MODE_PRIVATE);
    }

    @Override
    public void put(String key, long value) {
        sharedPreferences.edit().putLong(key, value).apply();
    }

    @Override
    public long get(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    @Override
    public void clear() {
        remove(CacheInterface.KEY_CACHED_BOOT_TIME);
        remove(CacheInterface.KEY_CACHED_DEVICE_UPTIME);
        remove(CacheInterface.KEY_CACHED_NTP_TIME);
    }

    private void remove(String keyCachedBootTime) {
        sharedPreferences.edit().remove(keyCachedBootTime).apply();
    }
}
