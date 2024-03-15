package com.yc.store.fastsp.lib;

import android.util.Log;

import com.yc.store.lru.lib.SystemLruCache;

public class FspCache extends SystemLruCache<String, FastSharedPreferences> {

    private static final String TAG = "FspCache";
    private static final int DEFAULT_MAX_SIZE =
            (int) (Runtime.getRuntime().maxMemory() / 1024 / 16);

    public FspCache() {
        this(DEFAULT_MAX_SIZE);
    }

    public FspCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(String key, FastSharedPreferences value) {
        int size = 0;
        if (value != null) {
            size = value.sizeOf();
        }
        Log.d(TAG, "FspCache sizeOf " + key + " is: " + size);
        return size;
    }

    @Override
    protected FastSharedPreferences create(String key) {
        return new FastSharedPreferences(key);
    }

    @Override
    protected void entryRemoved(boolean evicted, String key,
                                FastSharedPreferences oldValue,
                                FastSharedPreferences newValue) {
        Log.d(TAG, "FspCache entryRemoved: " + key);
    }

}
