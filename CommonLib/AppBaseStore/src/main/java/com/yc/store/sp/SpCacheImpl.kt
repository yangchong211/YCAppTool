package com.yc.store.sp

import android.content.Context
import android.content.SharedPreferences
import com.yc.store.ICacheable
import com.yc.toolutils.AppToolUtils

class SpCacheImpl : ICacheable {

    private var sp: SharedPreferences? = null

    init {
        sp = AppToolUtils.getApp()?.getSharedPreferences("sp", Context.MODE_PRIVATE)
    }

    override fun saveInt(key: String, value: Int) {
        sp?.edit()?.putInt(key, value)?.apply()
    }

    override fun readInt(key: String, default: Int): Int {
        return sp?.getInt(key, default) ?: 0
    }

    override fun saveFloat(key: String, value: Float) {
        sp?.edit()?.putFloat(key, value)?.apply()
    }

    override fun readFloat(key: String, default: Float): Float {
        return sp?.getFloat(key, default) ?: 0f
    }

    override fun saveLong(key: String, value: Long) {
        sp?.edit()?.putLong(key, value)?.apply()
    }

    override fun readLong(key: String, default: Long): Long {
        return sp?.getLong(key, default) ?: 0L
    }

    override fun saveString(key: String, value: String) {
        sp?.edit()?.putString(key, value)?.apply()
    }

    override fun readString(key: String, default: String): String {
        return sp?.getString(key, default) ?: ""
    }

    override fun saveBoolean(key: String, value: Boolean) {
        sp?.edit()?.putBoolean(key, value)?.apply()
    }

    override fun readBoolean(key: String, default: Boolean): Boolean {
        return sp?.getBoolean(key, default) ?: false
    }

    override fun removeKey(key: String) {
        sp?.edit()?.remove(key)?.apply()
    }

    override fun totalSize(): Long {
        return sp?.all?.size?.toLong() ?: 0L
    }

    override fun clearData() {
        sp?.edit()?.clear()?.apply()
    }
}