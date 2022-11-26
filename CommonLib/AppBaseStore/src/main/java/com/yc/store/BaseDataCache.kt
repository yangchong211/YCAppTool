package com.yc.store


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time   : 2018/3/12
 *     desc   : 策略类
 *     revise :
 *     GitHub: https://github.com/yangchong211
 * </pre>
 */
open class BaseDataCache : ICacheable {

    private lateinit var cacheImpl: ICacheable

    fun setCacheImpl(impl: ICacheable) {
        cacheImpl = impl
    }

    override fun saveInt(key: String, value: Int) {
        cacheImpl.saveInt(key, value)
    }

    override fun readInt(key: String, default: Int): Int {
        return cacheImpl.readInt(key, default)
    }

    override fun saveFloat(key: String, value: Float) {
        cacheImpl.saveFloat(key, value)
    }

    override fun readFloat(key: String, default: Float): Float {
        return cacheImpl.readFloat(key, default)
    }

    override fun saveDouble(key: String, value: Double) {
        cacheImpl.saveDouble(key, value)
    }

    override fun readDouble(key: String, default: Double): Double {
        return cacheImpl.readDouble(key, default)
    }

    override fun saveLong(key: String, value: Long) {
        cacheImpl.saveLong(key, value)
    }

    override fun readLong(key: String, default: Long): Long {
        return cacheImpl.readLong(key, default)
    }

    override fun readBoolean(key: String, default: Boolean): Boolean {
        return cacheImpl.readBoolean(key, default)
    }

    override fun saveBoolean(key: String, value: Boolean) {
        cacheImpl.saveBoolean(key, value)
    }

    override fun saveString(key: String, value: String) {
        cacheImpl.saveString(key, value)
    }

    override fun readString(key: String, default: String): String {
        return cacheImpl.readString(key, default)
    }

    override fun removeKey(key: String) {
        cacheImpl.removeKey(key)
    }

    override fun totalSize(): Long {
        return cacheImpl.totalSize()
    }

    override fun clearData() {
        cacheImpl.clearData()
    }


}