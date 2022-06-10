package com.yc.store.acache

import androidx.core.content.res.ConfigurationHelper
import com.yc.store.ICacheable
import com.yc.store.acache.cache.ACache
import com.yc.store.fastsp.sp.FastSharedPreferences
import com.yc.store.sp.SpCacheImpl
import com.yc.toolutils.AppToolUtils
import com.yc.toolutils.file.AppFileUtils
import java.io.File

class ACacheImpl (builder: Builder) : ICacheable {

    private var cache: ACache? = null

    init {
        if (builder.fileName!=null){
            val fileName = builder.fileName ?: AppFileUtils.getAppCachePath(AppToolUtils.getApp())
            cache = ACache.get(File(fileName))
        } else {
            cache = ACache.get(AppToolUtils.getApp())
        }

    }

    class Builder {
        var fileName: String? = null
        fun setFileId(name: String): Builder {
            fileName = name
            return this
        }

        fun build(): ACacheImpl {
            return ACacheImpl(this)
        }
    }

    override fun saveInt(key: String, value: Int) {

    }

    override fun readInt(key: String, default: Int): Int {
       return 0
    }

    override fun saveFloat(key: String, value: Float) {
    }

    override fun readFloat(key: String, default: Float): Float {
        return 0f
    }

    override fun saveDouble(key: String, value: Double) {
    }

    override fun readDouble(key: String, default: Double): Double {
        return 0.0
    }

    override fun saveLong(key: String, value: Long) {
    }

    override fun readLong(key: String, default: Long): Long {
        return 0L
    }

    override fun saveString(key: String, value: String) {
    }

    override fun readString(key: String, default: String): String {
        return ""
    }

    override fun saveBoolean(key: String, value: Boolean) {
    }

    override fun readBoolean(key: String, default: Boolean): Boolean {
        return false
    }

    override fun removeKey(key: String) {
    }

    override fun totalSize(): Long {
        return 0L
    }

    override fun clearData() {
    }
}