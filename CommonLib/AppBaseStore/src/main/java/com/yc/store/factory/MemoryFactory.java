package com.yc.store.factory;

import android.content.Context;

import com.yc.store.lru.LruMemoryCacheImpl;
import com.yc.store.memory.MemoryCacheImpl;

public class MemoryFactory implements CacheFactory<MemoryCacheImpl>{

    public static MemoryFactory create() {
        return new MemoryFactory();
    }

    @Override
    public MemoryCacheImpl createCache() {
        MemoryCacheImpl memoryCache = new MemoryCacheImpl();
        return memoryCache;
    }
}
