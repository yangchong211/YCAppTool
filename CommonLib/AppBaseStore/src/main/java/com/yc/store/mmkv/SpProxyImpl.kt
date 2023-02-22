package com.yc.store.mmkv

import android.content.SharedPreferences
import com.tencent.mmkv.MMKV

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time   : 2018/3/12
 *     desc   : 让MMKV支持getAll操作
 *     revise :
 *     GitHub: https://github.com/yangchong211
 * </pre>
 */
class SpProxyImpl(private val mmkv: MMKV?) : SharedPreferences, SharedPreferences.Editor {

    override fun getAll(): MutableMap<String, *> {
        val keys = mmkv?.allKeys()
        val map = mutableMapOf<String, Any>()
        keys?.forEach {
            if (it.contains("@")) {
                val typeList = it.split("@")
                when (typeList[typeList.size - 1]) {
                    String::class.simpleName -> map[it] = getString(it, "") ?: ""
                    Int::class.simpleName -> map[it] = getInt(it, 0)
                    Long::class.simpleName -> map[it] = getLong(it, 0L)
                    Float::class.simpleName -> map[it] = getFloat(it, 0f)
                    Boolean::class.simpleName -> map[it] = getBoolean(it, false)
                }
            }
        }
        return map
    }

    override fun getString(key: String?, defValue: String?): String? {
        val typeKey = getTypeKey<String>(key)
        return mmkv?.getString(typeKey, defValue)
    }

    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String> {
        throw Exception("not support")
    }

    override fun getInt(key: String?, defValue: Int): Int {
        val typeKey = getTypeKey<Int>(key)
        return mmkv?.getInt(typeKey, defValue) ?: defValue
    }

    override fun getLong(key: String?, defValue: Long): Long {
        val typeKey = getTypeKey<Long>(key)
        return mmkv?.getLong(typeKey, defValue) ?: defValue
    }

    override fun getFloat(key: String?, defValue: Float): Float {
        val typeKey = getTypeKey<Float>(key)
        return mmkv?.getFloat(typeKey, defValue) ?: defValue
    }

    override fun getBoolean(key: String?, defValue: Boolean): Boolean {
        val typeKey = getTypeKey<Boolean>(key)
        return mmkv?.getBoolean(typeKey, defValue) ?: defValue
    }

    override fun contains(key: String?): Boolean {
        val realKey = getRealKey(key)
        return realKey.isNotEmpty()
    }

    override fun edit(): SharedPreferences.Editor? {
        return mmkv?.edit()
    }

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        mmkv?.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        mmkv?.unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun putString(key: String?, value: String?): SharedPreferences.Editor? {
        val typeKey = getTypeKey<String>(key)
        return mmkv?.putString(typeKey, value)
    }

    override fun putStringSet(key: String?, values: MutableSet<String>?): SharedPreferences.Editor? {
        throw Exception("no support")
    }

    override fun putInt(key: String?, value: Int): SharedPreferences.Editor? {
        val typeKey = getTypeKey<Int>(key)
        return mmkv?.putInt(typeKey, value)
    }

    override fun putLong(key: String?, value: Long): SharedPreferences.Editor? {
        val typeKey = getTypeKey<Long>(key)
        return mmkv?.putLong(typeKey, value)
    }

    override fun putFloat(key: String?, value: Float): SharedPreferences.Editor? {
        val typeKey = getTypeKey<Float>(key)
        return mmkv?.putFloat(typeKey, value)
    }

    override fun putBoolean(key: String?, value: Boolean): SharedPreferences.Editor? {
        val typeKey = getTypeKey<Boolean>(key)
        return mmkv?.putBoolean(typeKey, value)
    }

    override fun remove(key: String?): SharedPreferences.Editor? {
        val realKey = getRealKey(key)
        if (realKey.isNotEmpty()){
            return mmkv?.remove(realKey)
        }
        return null
    }

    override fun clear(): SharedPreferences.Editor? {
        return mmkv?.clear()
    }

    override fun commit(): Boolean {
        return mmkv?.commit()?:false
    }

    override fun apply() {
        mmkv?.apply()
    }

    private inline fun <reified T> getTypeKey(key: String?): String {
        val type = "@" + T::class.simpleName
        return if (key?.contains(type) == true) {
            type
        } else {
            key + type
        }
    }

    private fun getRealKey(key: String?):String{
        val typeKys = listOf(getTypeKey<String>(key),getTypeKey<Long>(key),
            getTypeKey<Float>(key),getTypeKey<Int>(key),getTypeKey<Boolean>(key))
        typeKys.forEach {
            if (mmkv?.containsKey(it)==true){
                return it
            }
        }
        return ""
    }
}
