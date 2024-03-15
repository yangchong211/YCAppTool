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
package com.yc.store.lru

import com.yc.store.ICacheable
import com.yc.store.lru.lib.SystemLruCache
import com.yc.store.config.CacheInitHelper

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time   : 2018/3/12
 *     desc   : LruCache内存存储实现类
 *     revise :
 *     GitHub: https://github.com/yangchong211
 * </pre>
 */
class LruMemoryCacheImpl : ICacheable {

    private var cache: SystemLruCache<String, Any>? = null

    init {
        val maxLruSize = CacheInitHelper.getMaxLruSize()
        cache = SystemLruCache(maxLruSize)
    }

    override fun saveInt(key: String, value: Int) {
        cache?.put(key, value)
    }

    override fun readInt(key: String, default: Int): Int {
        if (cache != null) {
            cache?.get(key) as Int
        }
        return default
    }

    override fun saveFloat(key: String, value: Float) {
        cache?.put(key, value)
    }

    override fun readFloat(key: String, default: Float): Float {
        if (cache != null) {
            cache?.get(key) as Float
        }
        return default
    }

    override fun saveDouble(key: String, value: Double) {
        cache?.put(key, value)
    }

    override fun readDouble(key: String, default: Double): Double {
        if (cache != null) {
            cache?.get(key) as Double
        }
        return default
    }

    override fun saveLong(key: String, value: Long) {
        cache?.put(key, value)
    }

    override fun readLong(key: String, default: Long): Long {
        if (cache != null) {
            cache?.get(key) as Long
        }
        return default
    }

    override fun saveString(key: String, value: String) {
        cache?.put(key, value)
    }

    override fun readString(key: String, default: String): String {
        if (cache != null) {
            cache?.get(key) as String
        }
        return default
    }

    override fun saveBoolean(key: String, value: Boolean) {
        cache?.put(key, value)
    }

    override fun readBoolean(key: String, default: Boolean): Boolean {
        if (cache != null) {
            cache?.get(key) as Boolean
        }
        return default
    }

    override fun removeKey(key: String) {
        cache?.remove(key)
    }

    override fun totalSize(): Long {
        return cache?.size()?.toLong() ?: 0L
    }

    override fun clearData() {
        cache?.clear()
    }
}