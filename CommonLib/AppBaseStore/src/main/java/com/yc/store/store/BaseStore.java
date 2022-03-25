package com.yc.store.store;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.yc.store.cache.Cache;
import com.yc.store.model.Entry;
import com.yc.store.store.util.ByteUtils;
import com.yc.store.store.util.ParcelableUtil;

/**
 * 所有Store的基类
 * 提供缓存以及缓存失效的机制,让各个store具有缓存能力
 */
public abstract class BaseStore {

//    /**
//     * 全局上下文
//     */
//    static Context sContext;

    /**
     * cache
     */
    private StoreCache mCache;

    /**
     * 支持加密持久化
     */
    private DiskCache mDiskCache;

    private String cacheDirName;

    public BaseStore(@NonNull String cacheDirName) {
        this.cacheDirName = cacheDirName;
        mCache = new StoreCache();
    }


    /**
     * 加入磁盘缓存
     *
     * @param context
     * @param key     缓存key
     * @param bytes   缓存value
     */
    protected void save(Context context, String key, byte[] bytes) {
        DiskCache.DEntry entry = new DiskCache.DEntry();
        entry.data = bytes;
        save(context, key, entry);
    }

    /**
     * 惰性初始化 DiskCache
     *
     * @param context
     */
    private void initDiskCacheLazy(Context context) {
        if (mDiskCache != null) {
            return;
        }

        synchronized (BaseStore.class) {
            if (mDiskCache == null) {
                mDiskCache = new DiskCache(context, cacheDirName);
                mDiskCache.initialize();
            }
        }
    }


    /**
     * 加入磁盘缓存
     *
     * @param context
     * @param key     缓存key
     * @param entry   缓存value
     */
    protected void save(Context context, final String key, final DiskCache.DEntry entry) {
        initDiskCacheLazy(context);

        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                mDiskCache.put(key, entry);
            }
        });
    }

    /**
     * 从磁盘缓存中读取
     *
     * @param key 缓存key
     * @return 缓存value
     */
    protected DiskCache.DEntry load(Context context, String key) {
        initDiskCacheLazy(context);
        if (mDiskCache == null) {
            return new DiskCache.DEntry();
        }
        DiskCache.DEntry dentry = new DiskCache.DEntry();
        Object cache = get(key);

        if (cache instanceof Long) {
            dentry.data = ByteUtils.longToBytes((Long) cache);
            return dentry;
        }
        if (cache instanceof String) {
            dentry.data = ((String) cache).getBytes();
            return dentry;
        }
        if (cache instanceof Parcelable) {
            dentry.data = ParcelableUtil.marshall((Parcelable) cache);
            return dentry;
        }

        Entry entry = mDiskCache.get(key);
        if (entry != null) {
            dentry.data = entry.data;
        }
        return dentry;
    }

    /**
     * 加入缓存
     *
     * @param key      缓存key
     * @param value    缓存value
     * @param duration 缓存持续时间 {@link StoreCache#ALWAYS_EXPIRE StoreCache#NON_EXPIRE}
     */
    protected void put(String key, Object value, long duration) {
        mCache.put(key, value, duration);
    }

    /**
     * 加入缓存 默认缓存项每次都过期
     *
     * @param key   缓存key
     * @param value 缓存value
     */
    protected void put(String key, Object value) {
        put(key, value, StoreCache.ALWAYS_EXPIRE);
    }

    /**
     * 移除缓存项
     *
     * @param key
     */
    protected void remove(String key) {
        mCache.remove(key);
    }

    /**
     * 擦除磁盘缓存项
     *
     * @param key
     */
    protected void wipe(String key) {
        if (mDiskCache != null) {
            mDiskCache.remove(key);
        }
    }

    /**
     * 获取缓存项
     * <p/>
     * 在获取缓存项建议对缓存是否过期进行判断{@link #isExpired(String)}
     *
     * @param key
     * @return
     */
    protected Object get(String key) {
        return mCache.get(key);
    }

    /**
     * 有效获取缓存项
     *
     * @param key
     * @return
     */
    public Object getInner(Context context, String key) {
        Object value = get(key);
        if (value != null) {
            return value;
        }
        DiskCache.DEntry entry = load(context, key);
        if (entry != null && entry.data != null) {
            value = entry.data;
        }
        return value;
    }

    /**
     * 缓存
     *
     * @param context
     * @param key
     * @param value
     */
    public void putAndSave(Context context, String key, long value) {
        if (key != null) {
            put(key, value);
            DiskCache.DEntry entry = new DiskCache.DEntry();
            entry.data = ByteUtils.longToBytes(value);
            save(context, key, entry);
        }
    }

    /**
     * 缓存
     *
     * @param context
     * @param key
     * @param value
     */
    public void putAndSave(Context context, String key, String value) {
        if (key != null && value != null) {
            put(key, value);
            DiskCache.DEntry entry = new DiskCache.DEntry();
            entry.data = value.getBytes();
            save(context, key, entry);
        }
    }

    /**
     * 缓存
     *
     * @param context
     * @param key
     * @param value
     */
    public void putAndSave(Context context, String key, Parcelable value) {
        if (key != null && value != null) {
            put(key, value);
            DiskCache.DEntry entry = new DiskCache.DEntry();
            entry.data = ParcelableUtil.marshall(value);
            save(context, key, entry);
        }
    }

    /**
     * 清除
     *
     * @param key
     */
    public void clearAll(String key) {
        remove(key);
        wipe(key);
    }

    /**
     * 缓存是否过期
     *
     * @param key
     * @return
     */
    protected boolean isExpired(String key) {
        return mCache.isExpired(key);
    }



}
