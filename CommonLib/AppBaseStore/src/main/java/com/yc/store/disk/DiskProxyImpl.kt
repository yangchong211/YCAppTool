package com.yc.store.disk

import android.content.SharedPreferences
import com.tencent.mmkv.MMKV
import com.yc.store.ext.getValue

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time   : 2018/3/12
 *     desc   : 支持一键让sp迁移到disk存储
 *     revise :
 *     GitHub: https://github.com/yangchong211
 * </pre>
 */
class DiskProxyImpl(private val diskCacheImpl: LruDiskCacheImpl?) : SharedPreferences, SharedPreferences.Editor {

    override fun getAll(): MutableMap<String, *> {
        TODO("Not yet implemented")
    }

    override fun getString(key: String?, defValue: String?): String? {
        TODO("Not yet implemented")
    }

    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String> {
        TODO("Not yet implemented")
    }

    override fun getInt(key: String?, defValue: Int): Int {
        TODO("Not yet implemented")
    }

    override fun getLong(key: String?, defValue: Long): Long {
        TODO("Not yet implemented")
    }

    override fun getFloat(key: String?, defValue: Float): Float {
        TODO("Not yet implemented")
    }

    override fun getBoolean(key: String?, defValue: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override fun contains(key: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun edit(): SharedPreferences.Editor {
        TODO("Not yet implemented")
    }

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        TODO("Not yet implemented")
    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        TODO("Not yet implemented")
    }

    override fun putString(key: String?, value: String?): SharedPreferences.Editor {
        TODO("Not yet implemented")
    }

    override fun putStringSet(key: String?, values: MutableSet<String>?): SharedPreferences.Editor {
        TODO("Not yet implemented")
    }

    override fun putInt(key: String?, value: Int): SharedPreferences.Editor {
        TODO("Not yet implemented")
    }

    override fun putLong(key: String?, value: Long): SharedPreferences.Editor {
        TODO("Not yet implemented")
    }

    override fun putFloat(key: String?, value: Float): SharedPreferences.Editor {
        TODO("Not yet implemented")
    }

    override fun putBoolean(key: String?, value: Boolean): SharedPreferences.Editor {
        TODO("Not yet implemented")
    }

    override fun remove(key: String?): SharedPreferences.Editor {
        TODO("Not yet implemented")
    }

    override fun clear(): SharedPreferences.Editor {
        TODO("Not yet implemented")
    }

    override fun commit(): Boolean {
        TODO("Not yet implemented")
    }

    override fun apply() {
        TODO("Not yet implemented")
    }

}
