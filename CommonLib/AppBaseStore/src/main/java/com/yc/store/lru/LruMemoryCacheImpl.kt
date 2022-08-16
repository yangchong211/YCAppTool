package com.yc.store.lru

import com.yc.store.ICacheable
import com.yc.applrucache.SystemLruCache

class LruMemoryCacheImpl : ICacheable {

    private var cache: SystemLruCache<String, Any>? = null

    init {
        cache = SystemLruCache(1000)
    }

    override fun saveInt(key: String, value: Int) {
        cache?.put(key, value)
    }

    override fun readInt(key: String, default: Int): Int {
        if (cache != null) {
            cache?.get(key) as Int
        }
        return default
    }

    override fun saveFloat(key: String, value: Float) {
        cache?.put(key, value)
    }

    override fun readFloat(key: String, default: Float): Float {
        if (cache != null) {
            cache?.get(key) as Float
        }
        return default
    }

    override fun saveDouble(key: String, value: Double) {
        cache?.put(key, value)
    }

    override fun readDouble(key: String, default: Double): Double {
        if (cache != null) {
            cache?.get(key) as Double
        }
        return default
    }

    override fun saveLong(key: String, value: Long) {
        cache?.put(key, value)
    }

    override fun readLong(key: String, default: Long): Long {
        if (cache != null) {
            cache?.get(key) as Long
        }
        return default
    }

    override fun saveString(key: String, value: String) {
        cache?.put(key, value)
    }

    override fun readString(key: String, default: String): String {
        if (cache != null) {
            cache?.get(key) as String
        }
        return default
    }

    override fun saveBoolean(key: String, value: Boolean) {
        cache?.put(key, value)
    }

    override fun readBoolean(key: String, default: Boolean): Boolean {
        if (cache != null) {
            cache?.get(key) as Boolean
        }
        return default
    }

    override fun removeKey(key: String) {
        cache?.remove(key)
    }

    override fun totalSize(): Long {
        return cache?.size()?.toLong() ?: 0L
    }

    override fun clearData() {
        cache?.clear()
    }
}