package com.yc.store

import android.app.Application
import com.yc.store.config.CacheConstants

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time   : 2018/3/12
 *     desc   : 存储帮助工具类
 *     revise :
 *     GitHub: https://github.com/yangchong211
 * </pre>
 */
class StoreToolHelper {

    private var sApplication: Application? = null
    private var spCache: BaseDataCache? = null
    private var storeCache: BaseDataCache? = null
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
            throw NullPointerException("Argument 'app' of type Application (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it")
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

    /**
     * 获取sp对象
     */
    fun getSpCache(): BaseDataCache {
        if (spCache == null) {
            spCache = BaseDataCache()
            val cacheImpl = CacheFactoryUtils.getCacheImpl(CacheConstants.CacheType.TYPE_SP)
            spCache?.setCacheImpl(cacheImpl)
        }
        return spCache as BaseDataCache
    }

    /**
     * 获取DataStore单例模式对象
     */
    fun getStoreCache(): BaseDataCache {
        if (storeCache == null) {
            storeCache = BaseDataCache()
            val cacheImpl = CacheFactoryUtils.getCacheImpl(CacheConstants.CacheType.TYPE_STORE)
            storeCache?.setCacheImpl(cacheImpl)
        }
        return storeCache as BaseDataCache
    }

    fun getMmkvDiskCache(): BaseDataCache{
        if (mmkvCache == null) {
            mmkvCache = BaseDataCache()
            val cacheImpl = CacheFactoryUtils.getCacheImpl(CacheConstants.CacheType.TYPE_MMKV)
            mmkvCache?.setCacheImpl(cacheImpl)
        }
        return mmkvCache as BaseDataCache
    }

    fun getMemoryCache(): BaseDataCache {
        if (memoryCache == null) {
            memoryCache = BaseDataCache()
            val cacheImpl = CacheFactoryUtils.getCacheImpl(CacheConstants.CacheType.TYPE_MEMORY)
            memoryCache?.setCacheImpl(cacheImpl)
        }
        return memoryCache as BaseDataCache
    }

    fun getLruMemoryCache(): BaseDataCache {
        if (lruMemoryCache == null) {
            lruMemoryCache = BaseDataCache()
            val cacheImpl = CacheFactoryUtils.getCacheImpl(CacheConstants.CacheType.TYPE_LRU)
            lruMemoryCache?.setCacheImpl(cacheImpl)
        }
        return lruMemoryCache as BaseDataCache
    }

    fun getLruDiskCache(): BaseDataCache {
        if (lruDiskCache == null) {
            lruDiskCache = BaseDataCache()
            val cacheImpl = CacheFactoryUtils.getCacheImpl(CacheConstants.CacheType.TYPE_DISK)
            lruDiskCache?.setCacheImpl(cacheImpl)
        }
        return lruDiskCache as BaseDataCache
    }

    fun getFastSpCache(): BaseDataCache {
        if (fastSpCache == null) {
            fastSpCache = BaseDataCache()
            val cacheImpl = CacheFactoryUtils.getCacheImpl(CacheConstants.CacheType.TYPE_FAST)
            fastSpCache?.setCacheImpl(cacheImpl)
        }
        return fastSpCache as BaseDataCache
    }


}