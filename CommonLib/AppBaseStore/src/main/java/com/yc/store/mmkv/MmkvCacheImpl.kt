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
package com.yc.store.mmkv

import android.util.Log
import com.tencent.mmkv.MMKV
import com.yc.store.ICacheable
import com.yc.store.config.CacheInitHelper
import java.io.File

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time   : 2018/3/12
 *     desc   : Mmkv存储实现类
 *     revise :
 *     GitHub: https://github.com/yangchong211
 * </pre>
 */
class MmkvCacheImpl(builder: Builder) : ICacheable {

    private var mmkv: MMKV = MMKV.mmkvWithID(builder.fileName) ?: MMKV.defaultMMKV()!!
    private var fileName: String? = builder.fileName

    class Builder {
        var fileName: String? = null
        fun setFileId(name: String): Builder {
            fileName = name
            return this
        }

        fun build(): MmkvCacheImpl {
            return MmkvCacheImpl(this)
        }
    }


    override fun saveInt(key: String, value: Int) {
        mmkv.encode(key, value)
    }

    override fun readInt(key: String, default: Int): Int {
        return mmkv.decodeInt(key, default)
    }

    override fun saveFloat(key: String, value: Float) {
        mmkv.encode(key, value)
    }

    override fun readFloat(key: String, default: Float): Float {
        return mmkv.decodeFloat(key, default)
    }

    override fun saveDouble(key: String, value: Double) {
        mmkv.encode(key, value)
    }

    override fun readDouble(key: String, default: Double): Double {
        return mmkv.decodeDouble(key, default)
    }

    override fun saveLong(key: String, value: Long) {
        mmkv.encode(key, value)
    }

    override fun readLong(key: String, default: Long): Long {
        return mmkv.decodeLong(key, default)
    }

    override fun saveString(key: String, value: String) {
        mmkv.encode(key, value)
    }

    override fun readString(key: String, default: String): String {
        return mmkv.decodeString(key, default) ?: default
    }

    override fun saveBoolean(key: String, value: Boolean) {
        mmkv.encode(key, value)
    }

    override fun readBoolean(key: String, default: Boolean): Boolean {
        return mmkv.decodeBool(key, default)
    }

    override fun removeKey(key: String) {
        mmkv.removeValueForKey(key)
    }

    override fun totalSize(): Long {
        return mmkv.totalSize()
    }

    override fun clearData() {
        File(CacheInitHelper.getMmkvPath(), fileName).apply {
            if (exists()) {
                Log.d("DiskCacheImpl","before fileSize:" +
                        "${this.length() / 1024}K,path:${this.absolutePath}")
            }
        }
        mmkv.clearAll()
        File(CacheInitHelper.getMmkvPath(), fileName).apply {
            if (exists()) {
                Log.d("DiskCacheImpl","after fileSize:" +
                        "${this.length() / 1024}K,path:${this.absolutePath}")
            }
        }
    }
}