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

    /**
     * 通过mmkvWithID创建指定路径的文件
     */
    private var mmkv: MMKV = MMKV.mmkvWithID(builder.fileName) ?: MMKV.defaultMMKV()!!
    private var fileName: String? = builder.fileName

    companion object {
        private var rootPath: String? = null
        fun initRootPath(path: String) {
            rootPath = MMKV.initialize(path)
        }
    }

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

    /**
     * MMKV的主要缺点就在于它不支持getAll
     * MMKV都是按字节进行存储的，实际写入文件把类型擦除了，这也是MMKV不支持getAll的原因
     *
     * 如何让MMKV支持getAll?
     * 既然MMKV不支持getAll的原因是因为类型被擦除了，那最简单的思路就是加上类型
     */
    private fun getAll(): Map<String, Any> {
        val keys = mmkv.allKeys()
        val map = mutableMapOf<String, Any>()
        keys?.forEach {
            if (it.contains("@")) {
                val typeList = it.split("@")
                when (typeList[typeList.size - 1]) {
                    String::class.simpleName -> map[it] = mmkv.getString(it, "") ?: ""
                    Int::class.simpleName -> map[it] = mmkv.getInt(it, 0)
                    Long::class.simpleName -> map[it] = mmkv.getLong(it, 0L)
                    Float::class.simpleName -> map[it] = mmkv.getFloat(it, 0f)
                    Boolean::class.simpleName -> map[it] = mmkv.getBoolean(it, false)
                }
            }
        }
        return map
    }

    override fun totalSize(): Long {
        return mmkv.totalSize()
    }

    override fun clearData() {
        fileName?.let {
            File(rootPath, it).apply {
                if (exists()) {
                    Log.d("DiskCacheImpl","before fileSize:" +
                            "${this.length() / 1024}K,path:${this.absolutePath}")
                }
            }
        }
        mmkv.clearAll()
        fileName?.let {
            File(rootPath, it).apply {
                if (exists()) {
                    Log.d("DiskCacheImpl","after fileSize:" +
                            "${this.length() / 1024}K,path:${this.absolutePath}")
                }
            }
        }
    }
}