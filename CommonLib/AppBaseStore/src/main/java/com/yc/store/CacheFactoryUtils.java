package com.yc.store;

import android.content.Context;

import com.yc.store.config.CacheConstants;
import com.yc.store.config.CacheInitHelper;
import com.yc.store.factory.CacheFactory;
import com.yc.store.factory.DiskFactory;
import com.yc.store.factory.FastSpFactory;
import com.yc.store.factory.LruCacheFactory;
import com.yc.store.factory.MemoryFactory;
import com.yc.store.factory.MmkvFactory;
import com.yc.store.factory.SpFactory;
import com.yc.store.factory.StoreFactory;


public final class CacheFactoryUtils {

    public static CacheFactory getPlayer(@CacheConstants.CacheType int type) {
        if (CacheInitHelper.INSTANCE.isToggleOpen()){
            //如果是降级，则默认使用sp
            return SpFactory.create();
        }
        if (type == CacheConstants.CacheType.TYPE_DISK) {
            return DiskFactory.create();
        } else if (type == CacheConstants.CacheType.TYPE_LRU) {
            return LruCacheFactory.create();
        } else if (type == CacheConstants.CacheType.TYPE_MEMORY) {
            return MemoryFactory.create();
        } else if (type == CacheConstants.CacheType.TYPE_MMKV) {
            return MmkvFactory.create();
        } else if (type == CacheConstants.CacheType.TYPE_SP) {
            return SpFactory.create();
        } else if (type == CacheConstants.CacheType.TYPE_STORE) {
            return StoreFactory.create();
        } else {
            return MmkvFactory.create();
        }
    }

    public static ICacheable getCacheImpl(@CacheConstants.CacheType int type) {
        if (CacheInitHelper.INSTANCE.isToggleOpen()){
            //如果是降级，则默认使用sp
            return SpFactory.create().createCache();
        }
        if (type == CacheConstants.CacheType.TYPE_DISK) {
            return DiskFactory.create().createCache();
        } else if (type == CacheConstants.CacheType.TYPE_LRU) {
            return LruCacheFactory.create().createCache();
        } else if (type == CacheConstants.CacheType.TYPE_MEMORY) {
            return MemoryFactory.create().createCache();
        } else if (type == CacheConstants.CacheType.TYPE_MMKV) {
            return MmkvFactory.create().createCache();
        } else if (type == CacheConstants.CacheType.TYPE_SP) {
            return SpFactory.create().createCache();
        } else if (type == CacheConstants.CacheType.TYPE_STORE) {
            return StoreFactory.create().createCache();
        }  else if (type == CacheConstants.CacheType.TYPE_FAST) {
            return FastSpFactory.create().createCache();
        } else {
            return MmkvFactory.create().createCache();
        }
    }
    
}
