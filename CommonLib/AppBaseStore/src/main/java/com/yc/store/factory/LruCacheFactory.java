package com.yc.store.factory;

import android.content.Context;

import com.yc.store.disk.LruDiskCacheImpl;
import com.yc.store.lru.LruMemoryCacheImpl;

public class LruCacheFactory implements CacheFactory<LruMemoryCacheImpl>{


    public static LruCacheFactory create() {
        return new LruCacheFactory();
    }

    @Override
    public LruMemoryCacheImpl createCache(Context context) {
        LruMemoryCacheImpl lruMemoryCache = new LruMemoryCacheImpl();
        return lruMemoryCache;
    }
}
