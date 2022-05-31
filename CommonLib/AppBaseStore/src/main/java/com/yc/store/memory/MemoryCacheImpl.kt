package com.yc.store.memory

import com.yc.store.ICacheable
import kotlin.collections.HashMap

class MemoryCacheImpl : ICacheable {

    private var dataSource: HashMap<String, Any> = HashMap()

    override fun saveInt(key: String, value: Int) {
        dataSource[key] = value
    }

    override fun readInt(key: String, default: Int): Int {
        return dataSource[key] as Int
    }

    override fun saveFloat(key: String, value: Float) {
        dataSource[key] = value
    }

    override fun readFloat(key: String, default: Float): Float {
        return dataSource[key] as Float
    }

    override fun saveLong(key: String, value: Long) {
        dataSource[key] = value
    }

    override fun readLong(key: String, default: Long): Long {
        return dataSource[key] as Long
    }

    override fun saveString(key: String, value: String) {
        dataSource[key] = value
    }

    override fun readString(key: String, default: String): String {
        return dataSource[key] as String
    }

    override fun saveBoolean(key: String, value: Boolean) {
        dataSource[key] = value
    }

    override fun readBoolean(key: String, default: Boolean): Boolean {
        return dataSource[key] as Boolean
    }

    override fun removeKey(key: String) {
        dataSource.remove(key)
    }

    override fun totalSize(): Long {
        return dataSource.size.toLong()
    }

    override fun clearData() {
        dataSource.clear()
    }

}