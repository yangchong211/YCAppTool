package com.yc.store.factory;

import android.content.Context;

import com.yc.store.memory.MemoryCacheImpl;
import com.yc.store.store.DataStoreCacheImpl;

public class StoreFactory implements CacheFactory<DataStoreCacheImpl>{


    public static StoreFactory create() {
        return new StoreFactory();
    }

    @Override
    public DataStoreCacheImpl createCache() {
        DataStoreCacheImpl dataStoreCache = new DataStoreCacheImpl();
        return dataStoreCache;
    }

}
