package com.yc.store.store;


import androidx.collection.LruCache;

class StoreCache<T> {

    /**
     * 不过期
     */
    public static final int NON_EXPIRE = -1;

    /**
     * 总是过期
     */
    public static final int ALWAYS_EXPIRE = -2;

    /**
     * 最大缓存的项个数
     */
    private static final int MAX_ENTRY_SIZE = 32;

    /**
     * cache
     */
    private LruCache<String, CacheItem> mCache;

    public StoreCache() {
        mCache = new LruCache<String, CacheItem>(MAX_ENTRY_SIZE);
    }

    /**
     * 加入缓存
     *
     * @param key
     * @param value
     * @param duration {@link #NON_EXPIRE}表示不过期
     */
    public void put(String key, T value, long duration) {
        CacheItem item = new CacheItem(value, duration);
        synchronized (mCache) {
            mCache.put(key, item);
        }
    }

    /**
     * 移除缓存项
     *
     * @param key
     */
    public void remove(String key) {
        synchronized (mCache) {
            mCache.remove(key);
        }
    }

    /**
     * 获取缓存项
     *
     * @param key
     * @return
     */
    public T get(String key) {
        CacheItem item = getCacheItem(key);
        return item == null ? null : item.getValue();
    }

    /**
     * 检测缓存项是否过期了 用输入的缓存持续时间
     *
     * @param key
     * @return
     */
    public boolean isExpired(String key) {
        CacheItem item = getCacheItem(key);

        // 如果找不到缓存项 认为是过期了
        if (item == null) {
            return true;
        }

        return item.isExpired();
    }

    /**
     * 得到缓存项
     *
     * @param key
     * @return
     */
    private CacheItem getCacheItem(String key) {
        synchronized (mCache) {
            return mCache.get(key);
        }
    }

    /**
     * 缓存项定义
     */
    private class CacheItem {

        /**
         * 缓存项起始时间戳
         */
        private long mStartTimestamp;

        /**
         * 有效时长
         */
        private long mDuration;

        /**
         * 缓存的对象
         */
        private T mValue;

        /**
         * 创建缓存 并开始计时
         *
         * @param value 缓存的内容
         */
        public CacheItem(T value, long duration) {
            this.mValue = value;
            this.mDuration = duration;
            this.mStartTimestamp = System.currentTimeMillis();
        }

        /**
         * 获得缓存对象
         *
         * @return
         */
        public T getValue() {
            return mValue;
        }

        /**
         * 是否已过期
         *
         * @return
         */
        public boolean isExpired() {
            // 永久不过期
            if (mDuration == NON_EXPIRE) {
                return false;
            }

            // 总是过期
            if (mDuration == ALWAYS_EXPIRE) {
                return true;
            }

            return System.currentTimeMillis() - mStartTimestamp > mDuration;
        }
    }
}
