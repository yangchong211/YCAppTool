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
package com.yc.store.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.yc.appcontextlib.AppToolUtils
import com.yc.store.ICacheable
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time   : 2018/3/12
 *     desc   : DataStore存储实现类
 *     revise : DataStore 是SharedPreference（简称SP） 替代产品。
 *     GitHub: https://github.com/yangchong211
 * </pre>
 */
class DataStoreCacheImpl : ICacheable {

    private val Context.dataStoreCache: DataStore<Preferences> by preferencesDataStore(
        name = "dataStoreCache")

    private val cache by lazy {
        AppToolUtils.getApp().dataStoreCache
    }

    init {
        AppToolUtils.getApp().dataStoreCache
    }

    override fun saveInt(key: String, value: Int) {
        runBlocking {
            cache.edit { it[intPreferencesKey(key)] = value }
        }
    }

    override fun readInt(key: String, defValue: Int): Int {
        var value = defValue
        runBlocking {
            cache.data.first {
                try{
                    it[intPreferencesKey(key)]?.run {
                        value = this
                    }
                }catch (e: Exception){

                }
                true
            }
        }
        return value
    }

    override fun saveFloat(key: String, value: Float) {
        runBlocking {
            cache.edit { it[floatPreferencesKey(key)] = value }
        }
    }

    override fun readFloat(key: String, defValue: Float): Float {
        var value = defValue
        runBlocking {
            cache.data.first {
                try{
                    it[floatPreferencesKey(key)]?.run {
                        value = this
                    }
                }catch (e: Exception){

                }
                true
            }
        }
        return value
    }

    override fun saveDouble(key: String, value: Double) {
        runBlocking {
            cache.edit { it[doublePreferencesKey(key)] = value }
        }
    }

    override fun readDouble(key: String, default: Double): Double {
        var value = default
        runBlocking {
            cache.data.first {
                try{
                    it[doublePreferencesKey(key)]?.run {
                        value = this
                    }
                }catch (e: Exception){

                }
                true
            }
        }
        return value
    }

    override fun saveLong(key: String, value: Long) {
        runBlocking {
            cache.edit { it[longPreferencesKey(key)] = value }
        }
    }

    override fun readLong(key: String, defValue: Long): Long {
        var value = defValue
        runBlocking {
            cache.data.first {
                try{
                    it[longPreferencesKey(key)]?.run {
                        value = this
                    }
                }catch (e: Exception){

                }
                true
            }
        }
        return value
    }

    override fun saveString(key: String, value: String) {
        runBlocking {
            cache.edit { it[stringPreferencesKey(key)] = value }
        }
    }

    override fun readString(key: String, defValue: String): String {
        var value = defValue
        runBlocking {
            cache.data.first {
                try{
                    it[stringPreferencesKey(key)]?.run {
                        value = this
                    }
                }catch (e: Exception){

                }
                true
            }
        }
        return value
    }

    override fun saveBoolean(key: String, value: Boolean) {
        runBlocking {
            cache.edit { it[booleanPreferencesKey(key)] = value }
        }
    }

    override fun readBoolean(key: String, defValue: Boolean): Boolean {
        var value = defValue
        runBlocking {
            cache.data.first {
                try{
                    it[booleanPreferencesKey(key)]?.run {
                        value = this
                    }
                }catch (e: Exception){

                }
                true
            }
        }
        return value
    }

    override fun removeKey(key: String) {
        runBlocking {
            // 由于 DataStore 不支持 remove，所以这里都还原成默认值
            cache.edit {
                it[intPreferencesKey(key)] = 0
                it[floatPreferencesKey(key)] = 0F
                it[doublePreferencesKey(key)] = 0.0
                it[longPreferencesKey(key)] = 0L
                it[booleanPreferencesKey(key)] = false
                // （String 的默认值本应是 null，由于不能为 null，这里还原成 ""）
                it[stringPreferencesKey(key)] = ""
            }
        }
    }

    override fun totalSize(): Long {
        var value = 0L
        runBlocking {
            cache.data.count {
                try{
                    it.asMap().size.run {
                        value = this.toLong()
                    }
                }catch (e: Exception){
                }
                true
            }
        }
        return value
    }

    override fun clearData() {
        runBlocking {
            cache.edit {
                //将该datastore中的数据全部清除
                it.clear()
            }
        }
    }

    fun remove(key: String) {
        runBlocking {
            // 由于 DataStore 不支持 remove，所以这里都还原成默认值
            cache.edit {
                it[intPreferencesKey(key)] = 0
                it[floatPreferencesKey(key)] = 0F
                it[doublePreferencesKey(key)] = 0.0
                it[longPreferencesKey(key)] = 0L
                it[booleanPreferencesKey(key)] = false
                // （String 的默认值本应是 null，由于不能为 null，这里还原成 ""）
                it[stringPreferencesKey(key)] = ""
            }
        }
    }
}