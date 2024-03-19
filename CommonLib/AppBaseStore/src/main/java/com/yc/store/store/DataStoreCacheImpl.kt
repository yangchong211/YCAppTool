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
import com.yc.toolutils.context.AppToolUtils
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
class DataStoreCacheImpl(val context: Context?= null, name: String? = "dataStoreCache") : ICacheable {

    /**
     * 文件名称是：dataStoreCache
     * 文件路径：/data/data/包名/files/datastore/dataStoreCache.preferences_pb
     */
    private val Context.dataStoreCache: DataStore<Preferences> by preferencesDataStore(
        name = name ?: "dataStoreCache",
        //将SP迁移到Preference DataStore中
        /*produceMigrations = { context ->
            listOf(SharedPreferencesMigration(context, ""))
        }*/
    )

    private val cache by lazy {
        context?.dataStoreCache ?: AppToolUtils.getApp().dataStoreCache
    }

    init {
        AppToolUtils.getApp().dataStoreCache
    }

    /**
     * DataStore 的主要优势之一是异步API，所以本身并未提供同步API调用，但实际上可能不一定始终能将周围的代码更改为异步代码。
     * 如果您使用的现有代码库采用同步磁盘 I/O，或者您的依赖项不提供异步API，就可能出现这种情况。
     * 可以采用协程本身提供 runBlocking()以帮助消除同步与异步代码之间的差异。可以使用 runBlocking() 从 DataStore 同步读取数据。
     *
     * runBlocking()会运行一个新的协程并阻塞当前线程直到内部逻辑完成，所以尽量避免在UI线程调用。
     * 而且还需要注意的一点是，不用在初始读取时调用runBlocking，会阻塞当前执行的线程，因为初始读取会有较多的IO操作，耗时较长。
     */
    override fun saveInt(key: String, value: Int) {
        runBlocking {
            saveIntData(key,value)
        }
    }

    override fun readInt(key: String, defValue: Int): Int {
        var value = defValue
        //Kotlin 协程提供 runBlocking() 协程构建器，以帮助消除同步与异步代码之间的差异。
        runBlocking {
            cache.data.first {
                try {
                    //获取key
                    val intPreferencesKey = intPreferencesKey(key)
                    //通过key获取value
                    it[intPreferencesKey]?.run {
                        value = this
                    }
                } catch (e: Exception) {

                }
                true
            }
        }
        return value
    }

    override fun saveFloat(key: String, value: Float) {
        runBlocking {
            saveFloatData(key, value)
        }
    }

    override fun readFloat(key: String, defValue: Float): Float {
        var value = defValue
        runBlocking {
            cache.data.first {
                try {
                    val floatPreferencesKey = floatPreferencesKey(key)
                    it[floatPreferencesKey]?.run {
                        value = this
                    }
                } catch (e: Exception) {

                }
                true
            }
        }
        return value
    }

    override fun saveDouble(key: String, value: Double) {
        runBlocking {
            saveDoubleData(key, value)
        }
    }

    override fun readDouble(key: String, default: Double): Double {
        var value = default
        runBlocking {
            cache.data.first {
                try {
                    val doublePreferencesKey = doublePreferencesKey(key)
                    it[doublePreferencesKey]?.run {
                        value = this
                    }
                } catch (e: Exception) {

                }
                true
            }
        }
        return value
    }

    override fun saveLong(key: String, value: Long) {
        runBlocking {
            saveLongData(key, value)
        }
    }

    override fun readLong(key: String, defValue: Long): Long {
        var value = defValue
        runBlocking {
            cache.data.first {
                try {
                    val longPreferencesKey = longPreferencesKey(key)
                    it[longPreferencesKey]?.run {
                        value = this
                    }
                } catch (e: Exception) {

                }
                true
            }
        }
        return value
    }

    override fun saveString(key: String, value: String) {
        runBlocking {
            saveStringData(key, value)
        }
    }

    override fun readString(key: String, defValue: String): String {
        var value = defValue
        runBlocking {
            cache.data.first {
                try {
                    val stringPreferencesKey = stringPreferencesKey(key)
                    it[stringPreferencesKey]?.run {
                        value = this
                    }
                } catch (e: Exception) {

                }
                true
            }
        }
        return value
    }

    override fun saveBoolean(key: String, value: Boolean) {
        runBlocking {
            saveBooleanData(key, value)
        }
    }

    override fun readBoolean(key: String, defValue: Boolean): Boolean {
        var value = defValue
        runBlocking {
            cache.data.first {
                try {
                    val booleanPreferencesKey = booleanPreferencesKey(key)
                    it[booleanPreferencesKey]?.run {
                        value = this
                    }
                } catch (e: Exception) {

                }
                true
            }
        }
        return value
    }

    suspend fun <U> putData(key: String, value: U) {
        when (value) {
            is Long -> saveLongData(key, value)
            is String -> saveStringData(key, value)
            is Int -> saveIntData(key, value)
            is Boolean -> saveBooleanData(key, value)
            is Float -> saveFloatData(key, value)
            is Double -> saveDoubleData(key, value)
            else -> throw IllegalArgumentException("This type can be saved into DataStore")
        }
    }

    private suspend fun saveBooleanData(key: String, value: Boolean) {
        cache.edit { mutablePreferences ->
            mutablePreferences[booleanPreferencesKey(key)] = value
        }
    }

    private suspend fun saveIntData(key: String, value: Int) {
        cache.edit { mutablePreferences ->
            mutablePreferences[intPreferencesKey(key)] = value
        }
    }

    private suspend fun saveStringData(key: String, value: String) {
        cache.edit { mutablePreferences ->
            mutablePreferences[stringPreferencesKey(key)] = value
        }
    }

    private suspend fun saveFloatData(key: String, value: Float) {
        cache.edit { mutablePreferences ->
            mutablePreferences[floatPreferencesKey(key)] = value
        }
    }

    private suspend fun saveLongData(key: String, value: Long) {
        cache.edit { mutablePreferences ->
            mutablePreferences[longPreferencesKey(key)] = value
        }
    }

    suspend fun saveDoubleData(key: String, value: Double) {
        cache.edit { mutablePreferences ->
            mutablePreferences[doublePreferencesKey(key)] = value
        }
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
                try {
                    it.asMap().size.run {
                        value = this.toLong()
                    }
                } catch (e: Exception) {
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