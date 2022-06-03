package com.yc.store

import android.app.Application
import com.yc.store.disk.LruDiskCacheImpl
import com.yc.store.fastsp.FastSpCacheImpl
import com.yc.store.lru.LruMemoryCacheImpl
import com.yc.store.memory.MemoryCacheImpl
import com.yc.store.mmkv.MmkvCacheImpl
import com.yc.store.sp.SpCacheImpl

class StoreToolHelper {

    private var sApplication: Application? = null
    private var spCache: BaseDataCache? = null
    private var mmkvCache: BaseDataCache? = null
    private var memoryCache: BaseDataCache? = null
    private var lruMemoryCache: BaseDataCache? = null
    private var lruDiskCache: BaseDataCache? = null
    private var fastSpCache: BaseDataCache? = null


    companion object {

        private var INSTANCE: StoreToolHelper? = null

        /**
         * 获取单例模式对象
         */
        @JvmStatic
        val instance: StoreToolHelper
            get() {
                if (INSTANCE == null) {
                    synchronized(StoreToolHelper::class.java) {
                        if (INSTANCE == null) {
                            INSTANCE = StoreToolHelper()
                        }
                    }
                }
                return INSTANCE!!
            }
    }

    fun setApp(app: Application?) {
        if (app == null) {
            throw NullPointerException("Argument 'app' of type Application (#0 out of 1, zero-based) is marked by @android.support.annotation.NonNull but got null for it")
        } else {
            sApplication = app
        }
    }

    val app: Application?
        get() = if (sApplication != null) {
            sApplication
        } else {
            throw NullPointerException("u should init first")
        }

    fun getSpCache(): BaseDataCache {
        if (spCache == null) {
            spCache = BaseDataCache()
            spCache?.setCacheImpl(SpCacheImpl())
        }
        return spCache as BaseDataCache
    }

    fun getMmkvDiskCache(): BaseDataCache{
        if (mmkvCache == null) {
            mmkvCache = BaseDataCache()
            val builder = MmkvCacheImpl.Builder()
            builder.fileName = "ycMmkv"
            val diskCacheImpl = builder.build()
            mmkvCache?.setCacheImpl(diskCacheImpl)
        }
        return mmkvCache as BaseDataCache
    }

    fun getMemoryCache(): BaseDataCache {
        if (memoryCache == null) {
            memoryCache = BaseDataCache()
            memoryCache?.setCacheImpl(MemoryCacheImpl())
        }
        return memoryCache as BaseDataCache
    }

    fun getLruMemoryCache(): BaseDataCache {
        if (lruMemoryCache == null) {
            lruMemoryCache = BaseDataCache()
            lruMemoryCache?.setCacheImpl(LruMemoryCacheImpl())
        }
        return lruMemoryCache as BaseDataCache
    }

    fun getLruDiskCache(): BaseDataCache {
        if (lruDiskCache == null) {
            lruDiskCache = BaseDataCache()
            lruDiskCache?.setCacheImpl(LruDiskCacheImpl())
        }
        return lruDiskCache as BaseDataCache
    }

    fun getFastSpCache(): BaseDataCache {
        if (fastSpCache == null) {
            fastSpCache = BaseDataCache()
            fastSpCache?.setCacheImpl(FastSpCacheImpl())
        }
        return fastSpCache as BaseDataCache
    }

}