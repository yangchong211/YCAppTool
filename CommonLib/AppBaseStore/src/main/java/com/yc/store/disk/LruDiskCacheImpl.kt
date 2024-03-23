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
package com.yc.store.disk

import com.yc.store.ICacheable
import com.yc.store.disk.lib.DiskLruCacheHelper
import com.yc.toolutils.size.AppNumberUtils

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time   : 2018/3/12
 *     desc   : DiskLruCache内存存储实现类
 *     revise :
 *     GitHub: https://github.com/yangchong211
 * </pre>
 */
class LruDiskCacheImpl : ICacheable {

    var diskLruCacheHelper = DiskLruCacheHelper()

    init {

    }

    override fun saveInt(key: String, value: Int) {
        diskLruCacheHelper.put(key, value)
    }

    override fun readInt(key: String, default: Int): Int {
        val value = diskLruCacheHelper.get(key)
        return AppNumberUtils.convertToInt(value, default)
    }

    override fun saveFloat(key: String, value: Float) {
        diskLruCacheHelper.put(key, value)
    }

    override fun readFloat(key: String, default: Float): Float {
        val value = diskLruCacheHelper.get(key)
        return AppNumberUtils.convertToFloat(value, default)
    }

    override fun saveDouble(key: String, value: Double) {
        diskLruCacheHelper.put(key, value)
    }

    override fun readDouble(key: String, default: Double): Double {
        val value = diskLruCacheHelper.get(key)
        return AppNumberUtils.convertToDouble(value, default)
    }

    override fun saveLong(key: String, value: Long) {
        diskLruCacheHelper.put(key, value)
    }

    override fun readLong(key: String, default: Long): Long {
        val value = diskLruCacheHelper.get(key)
        return AppNumberUtils.convertToLong(value, default)
    }

    override fun saveString(key: String, value: String) {
        diskLruCacheHelper.put(key, value)
    }

    override fun readString(key: String, default: String): String {
        val value = diskLruCacheHelper.get(key)
        return value ?: default
    }

    override fun saveBoolean(key: String, value: Boolean) {
        diskLruCacheHelper.put(key, value)
    }

    override fun readBoolean(key: String, default: Boolean): Boolean {
        val value = diskLruCacheHelper.get(key)
        if (value != null && value.isNotEmpty()) {
            if (value.equals("1")) {
                return true
            } else if (value.equals("0")) {
                return false
            }
        }
        return default
    }

    override fun removeKey(key: String) {
        diskLruCacheHelper.remove(key)
    }

    override fun totalSize(): Long {
        return diskLruCacheHelper.size
    }

    override fun clearData() {
        diskLruCacheHelper.clear()
    }
}