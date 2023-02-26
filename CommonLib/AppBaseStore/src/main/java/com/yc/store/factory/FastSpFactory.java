package com.yc.store.factory;

import com.yc.store.fastsp.FastSpCacheImpl;
import com.yc.store.store.DataStoreCacheImpl;

public class FastSpFactory implements CacheFactory<FastSpCacheImpl>{


    public static FastSpFactory create() {
        return new FastSpFactory();
    }

    @Override
    public FastSpCacheImpl createCache() {
        FastSpCacheImpl.Builder builder = new FastSpCacheImpl.Builder();
        builder.setFileId("fast");
        FastSpCacheImpl fastSpCache = builder.build();
        return fastSpCache;
    }

}
