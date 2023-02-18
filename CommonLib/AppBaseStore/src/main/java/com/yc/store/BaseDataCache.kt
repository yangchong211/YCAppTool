package com.yc.store

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException


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


    /**
     * 下面这些是存储对象数据
     */

    fun <T : Any?> saveKey(key: String, value: T?) {
        if (value == null) {
            cacheImpl.removeKey(key)
        } else {
            val jsonStr = Gson().toJson(value)
            saveString(key, jsonStr)
        }
    }


    inline fun <reified T : Any?> readKey(key: String): T? {
        val str = readString(key, "")
        return try {
            val gson = Gson()
            gson.fromJson(str, T::class.java)
        } catch (jse: JsonSyntaxException) {
            null
        }
    }

    inline fun <reified T : Any> readKeyList(key: String): List<T> {
        val str = readString(key, "")
        val list: MutableList<T> = ArrayList()
        try {
            if (str.isNotEmpty()) {
                val gson = Gson()
                val arrayResult: JsonArray = JsonParser().parse(str).asJsonArray
                for (jsonElement in arrayResult) {
                    list.add(gson.fromJson(jsonElement, T::class.java))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list.toList()
    }

    inline fun <reified T : Any?> readKeyWithType(key: String, type: Class<T>): T? {
        val str = readString(key, "")
        return try {
            Gson().fromJson(str, T::class.java)
        } catch (jse: JsonSyntaxException) {
            null
        }
    }

    inline fun <reified T : Any> readKeyWithDefault(key: String, default: T?): T? {
        val str = readString(key, "")
        return Gson().fromJson(str, T::class.java)
    }

}