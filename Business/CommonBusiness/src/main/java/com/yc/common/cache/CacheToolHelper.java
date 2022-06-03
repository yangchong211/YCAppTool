package com.yc.common.cache;

import com.yc.store.BaseDataCache;
import com.yc.store.disk.LruDiskCacheImpl;
import com.yc.store.fastsp.FastSpCacheImpl;
import com.yc.store.lru.LruMemoryCacheImpl;
import com.yc.store.memory.MemoryCacheImpl;
import com.yc.store.mmkv.MmkvCacheImpl;
import com.yc.store.sp.SpCacheImpl;

public class CacheToolHelper {

    private static CacheToolHelper INSTANCE;
    private BaseDataCache spCache;
    private BaseDataCache mmkvCache;
    private BaseDataCache memoryCache;
    private BaseDataCache lruMemoryCache;
    private BaseDataCache lruDiskCache;
    private BaseDataCache fastSpCache;

    /**
     * 获取NetworkTool实例 ,单例模式
     */
    public static CacheToolHelper getInstance() {
        if (INSTANCE == null) {
            synchronized (CacheToolHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CacheToolHelper();
                }
            }
        }
        return INSTANCE;
    }

    public BaseDataCache getSpCache(){
        if (spCache == null){
            spCache = new BaseDataCache();
            spCache.setCacheImpl(new SpCacheImpl());
        }
        return spCache;
    }

    public BaseDataCache getMkkvDiskCache(){
        if (mmkvCache == null){
            mmkvCache = new BaseDataCache();
            MmkvCacheImpl.Builder builder = new MmkvCacheImpl.Builder();
            builder.setFileName("ycMmkv");
            MmkvCacheImpl diskCacheImpl = builder.build();
            mmkvCache.setCacheImpl(diskCacheImpl);
        }
        return mmkvCache;
    }

    public BaseDataCache getMemoryCache(){
        if (memoryCache == null){
            memoryCache = new BaseDataCache();
            memoryCache.setCacheImpl(new MemoryCacheImpl());
        }
        return memoryCache;
    }

    public BaseDataCache getLruMemoryCache(){
        if (lruMemoryCache == null){
            lruMemoryCache = new BaseDataCache();
            lruMemoryCache.setCacheImpl(new LruMemoryCacheImpl());
        }
        return lruMemoryCache;
    }

    public BaseDataCache getLruDiskCache(){
        if (lruDiskCache == null){
            lruDiskCache = new BaseDataCache();
            lruDiskCache.setCacheImpl(new LruDiskCacheImpl());
        }
        return lruDiskCache;
    }

    public BaseDataCache getFastSpCache(){
        if (fastSpCache == null){
            fastSpCache = new BaseDataCache();
            fastSpCache.setCacheImpl(new FastSpCacheImpl());
        }
        return fastSpCache;
    }

}
