package com.yc.store.disk

import com.yc.store.ICacheable
import com.yc.applrudisk.DiskLruCacheHelper
import com.yc.toolutils.AppNumberUtils

class LruDiskCacheImpl : ICacheable {

    var diskLruCacheHelper = DiskLruCacheHelper()

    override fun saveInt(key: String, value: Int) {
        diskLruCacheHelper.put(key, value)
    }

    override fun readInt(key: String, default: Int): Int {
        val value = diskLruCacheHelper.get(key)
        return AppNumberUtils.convertToInt(value, default)
    }

    override fun saveFloat(key: String, value: Float) {
        diskLruCacheHelper.put(key, value)
    }

    override fun readFloat(key: String, default: Float): Float {
        val value = diskLruCacheHelper.get(key)
        return AppNumberUtils.convertToFloat(value, default)
    }

    override fun saveDouble(key: String, value: Double) {
        diskLruCacheHelper.put(key, value)
    }

    override fun readDouble(key: String, default: Double): Double {
        val value = diskLruCacheHelper.get(key)
        return AppNumberUtils.convertToDouble(value, default)
    }

    override fun saveLong(key: String, value: Long) {
        diskLruCacheHelper.put(key, value)
    }

    override fun readLong(key: String, default: Long): Long {
        val value = diskLruCacheHelper.get(key)
        return AppNumberUtils.convertToLong(value, default)
    }

    override fun saveString(key: String, value: String) {
        diskLruCacheHelper.put(key, value)
    }

    override fun readString(key: String, default: String): String {
        val value = diskLruCacheHelper.get(key)
        return value ?: default
    }

    override fun saveBoolean(key: String, value: Boolean) {
        diskLruCacheHelper.put(key, value)
    }

    override fun readBoolean(key: String, default: Boolean): Boolean {
        val value = diskLruCacheHelper.get(key)
        if (value != null && value.isNotEmpty()) {
            if (value.equals("1")) {
                return true
            } else if (value.equals("0")) {
                return false
            }
        }
        return default
    }

    override fun removeKey(key: String) {
        diskLruCacheHelper.remove(key)
    }

    override fun totalSize(): Long {
        return diskLruCacheHelper.size
    }

    override fun clearData() {
        diskLruCacheHelper.clear()
    }
}