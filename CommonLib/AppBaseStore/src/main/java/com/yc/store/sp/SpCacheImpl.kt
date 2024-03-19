/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.yc.store.sp

import android.content.Context
import android.content.SharedPreferences
import com.yc.store.ICacheable
import com.yc.toolutils.context.AppToolUtils

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time   : 2018/3/12
 *     desc   : Sp存储实现类
 *     revise :
 *     GitHub: https://github.com/yangchong211
 * </pre>
 */
class SpCacheImpl(builder: Builder) : ICacheable {

    private var sp: SharedPreferences? = null

    init {
        sp = AppToolUtils.getApp()?.getSharedPreferences(builder.fileName, Context.MODE_PRIVATE)
    }

    class Builder {
        var fileName: String? = null
        fun setFileId(name: String): Builder {
            fileName = name
            return this
        }

        fun build(): SpCacheImpl {
            return SpCacheImpl(this)
        }
    }

    /**
     * apply
     * commit
     */
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

    @Deprecated("may be error")
    override fun saveDouble(key: String, value: Double) {
        val data: Float = value.toFloat()
        sp?.edit()?.putFloat(key, data)?.apply()
    }

    /**
     * 注意：sp由于没有存储double的api，因此这里有double转化为float存储和读取。可能会有误差
     * todo 如何解决误差
     * 存储：5.20
     * 获取：5.199999809265137
     */
    @Deprecated("may be error")
    override fun readDouble(key: String, default: Double): Double {
        //需要去显式的转换，否则直接转化会抛出异常
        //val data = default.toFloat()
        val data: Float = default.toFloat()
        val float = sp?.getFloat(key, data)
        val toDouble = float?.toDouble()
        return toDouble ?: 0.0
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