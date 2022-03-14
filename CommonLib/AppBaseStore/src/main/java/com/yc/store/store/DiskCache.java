package com.yc.store.store;

import android.content.Context;

import com.yc.store.cache.Cache;
import com.yc.store.cache.DiskBasedCache;


import java.io.File;
import java.io.IOException;

/**
 * Created by tomchen on 2015/8/21.
 *
 * @author tom
 * <p/>
 * 支持本地加密持久化
 */
public class DiskCache implements Cache {

    /**
     * Default maximum disk usage in bytes.
     */
    private static final int DEFAULT_DISK_USAGE_BYTES = 2 * 1024 * 1024;

    private DiskBasedCache mCache;


    private File mCacheDir;

    private boolean isLazyInit;

    public DiskCache(Context context, String category) {
        mCacheDir = new File(context.getFilesDir().getAbsolutePath(), category);
        if (mCacheDir.exists()) {
            isLazyInit = true;
            mCache = new DiskBasedCache(mCacheDir, DEFAULT_DISK_USAGE_BYTES);
            initialize();
        }
    }

    private void doLazyInit() {
        isLazyInit = true;
        mCacheDir.mkdirs();
        mCache = new DiskBasedCache(mCacheDir, DEFAULT_DISK_USAGE_BYTES);
        initialize();
    }

    private void lazyInit() {
        if (!isLazyInit) {
            doLazyInit();
        }
    }

    @Override
    public synchronized Entry get(String key) {
        lazyInit();
        if (key == null) {
            return null;
        }
        Entry entry = mCache.get(key);
        return entry;
    }

    @Override
    public synchronized void put(String key, Entry entry) {
        lazyInit();
        if (key == null || entry == null) {
            return;
        }
        mCache.put(key, entry);
    }

    @Override
    public synchronized void initialize() {
        lazyInit();
        mCache.initialize();
    }

    @Override
    public synchronized void invalidate(String key, boolean fullExpire) {
        lazyInit();
        mCache.invalidate(key, fullExpire);
    }

    @Override
    public synchronized void remove(String key) {
        lazyInit();
        mCache.remove(key);
    }

    @Override
    public synchronized void clear() {
        lazyInit();
        mCache.clear();
    }


    public static class DEntry extends Entry {
        @Override
        public boolean isExpired() {
            return false;
        }

        @Override
        public boolean refreshNeeded() {
            return false;
        }
    }
}
