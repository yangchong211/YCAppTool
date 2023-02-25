package com.yc.store.factory;

import android.content.Context;

import com.yc.store.disk.LruDiskCacheImpl;

public class DiskFactory implements CacheFactory<LruDiskCacheImpl>{

    public static DiskFactory create() {
        return new DiskFactory();
    }

    @Override
    public LruDiskCacheImpl createCache() {
        LruDiskCacheImpl lruDiskCache = new LruDiskCacheImpl();
        return lruDiskCache;
    }
}
