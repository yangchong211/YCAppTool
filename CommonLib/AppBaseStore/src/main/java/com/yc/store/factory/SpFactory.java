package com.yc.store.factory;

import android.content.Context;

import com.yc.store.memory.MemoryCacheImpl;
import com.yc.store.sp.SpCacheImpl;

public class SpFactory implements CacheFactory<SpCacheImpl>{

    public static SpFactory create() {
        return new SpFactory();
    }


    @Override
    public SpCacheImpl createCache() {
        SpCacheImpl.Builder builder = new SpCacheImpl.Builder();
        builder.setFileName("ycSp");
        SpCacheImpl spCache = builder.build();
        return spCache;
    }
    
}
