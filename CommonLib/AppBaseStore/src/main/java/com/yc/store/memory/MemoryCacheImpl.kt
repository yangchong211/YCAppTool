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
package com.yc.store.memory

import com.yc.store.ICacheable
import kotlin.collections.HashMap

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time   : 2018/3/12
 *     desc   : HashMap内存存储实现类
 *     revise :
 *     GitHub: https://github.com/yangchong211
 * </pre>
 */
class MemoryCacheImpl : ICacheable {

    private var dataSource: HashMap<String, Any> = HashMap()

    override fun saveInt(key: String, value: Int) {
        dataSource[key] = value
    }

    override fun readInt(key: String, default: Int): Int {
        return dataSource[key] as Int
    }

    override fun saveFloat(key: String, value: Float) {
        dataSource[key] = value
    }

    override fun readFloat(key: String, default: Float): Float {
        return dataSource[key] as Float
    }

    override fun saveDouble(key: String, value: Double) {
        dataSource[key] = value
    }

    override fun readDouble(key: String, default: Double): Double {
        return dataSource[key] as Double
    }

    override fun saveLong(key: String, value: Long) {
        dataSource[key] = value
    }

    override fun readLong(key: String, default: Long): Long {
        return dataSource[key] as Long
    }

    override fun saveString(key: String, value: String) {
        dataSource[key] = value
    }

    override fun readString(key: String, default: String): String {
        return dataSource[key] as String
    }

    override fun saveBoolean(key: String, value: Boolean) {
        dataSource[key] = value
    }

    override fun readBoolean(key: String, default: Boolean): Boolean {
        return dataSource[key] as Boolean
    }

    override fun removeKey(key: String) {
        dataSource.remove(key)
    }

    override fun totalSize(): Long {
        return dataSource.size.toLong()
    }

    override fun clearData() {
        dataSource.clear()
    }

}