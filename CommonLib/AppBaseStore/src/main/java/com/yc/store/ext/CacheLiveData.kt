package com.yc.store.ext

import androidx.lifecycle.MutableLiveData
import com.yc.store.ICacheable


abstract class CacheLiveData<T>(
    protected val cache: ICacheable,
    protected val key: String,
    private val defaultValue: T
) : MutableLiveData<T>() {

    internal abstract fun getValueFromCache(key: String, defValue: T): T
    internal abstract fun setValueInCache(value: T)

    override fun setValue(value: T?) {
        super.setValue(value)
        if (value != null) {
            setValueInCache(value)
        } else {
            cache.removeKey(key)
        }
    }

    override fun postValue(value: T) {
        throw UnsupportedOperationException("not support postValue")
    }

    override fun getValue(): T? {
        return if (hasActiveObservers()) {
            super.getValue()
        } else {
            // If there are no observers, the LiveData value will not have been updated. Thus, we need
            // to get the latest value from SharedPreferences.
            getValueFromCache(key, defaultValue)
        }
    }

    override fun onActive() {
        super.onActive()
        super.setValue(getValueFromCache(key, defaultValue))
    }

}


class CacheIntLiveData(cache: ICacheable, key: String, defValue: Int) :
    CacheLiveData<Int>(cache, key, defValue) {
    override fun getValueFromCache(key: String, defValue: Int): Int =
        cache.readInt(key, defValue)

    override fun setValueInCache(value: Int) {
        cache.saveInt(key, value)
    }
}


class CacheStringLiveData(cache: ICacheable, key: String, defValue: String) :
    CacheLiveData<String>(cache, key, defValue) {
    override fun getValueFromCache(key: String, defValue: String): String =
        cache.readString(key, defValue)

    override fun setValueInCache(value: String) {
        cache.saveString(key, value)
    }
}


class CacheBooleanLiveData(cache: ICacheable, key: String, defValue: Boolean) :
    CacheLiveData<Boolean>(cache, key, defValue) {
    override fun getValueFromCache(key: String, defValue: Boolean): Boolean =
        cache.readBoolean(key, defValue)

    override fun setValueInCache(value: Boolean) {
        cache.saveBoolean(key, value)
    }
}


class CacheFloatLiveData(cache: ICacheable, key: String, defValue: Float) :
    CacheLiveData<Float>(cache, key, defValue) {
    override fun getValueFromCache(key: String, defValue: Float): Float =
        cache.readFloat(key, defValue)

    override fun setValueInCache(value: Float) {
        cache.saveFloat(key, value)
    }
}


class CacheLongLiveData(cache: ICacheable, key: String, defValue: Long) :
    CacheLiveData<Long>(cache, key, defValue) {
    override fun getValueFromCache(key: String, defValue: Long): Long =
        cache.readLong(key, defValue)

    override fun setValueInCache(value: Long) {
        cache.saveLong(key, value)
    }
}


fun ICacheable.intLiveData(key: String, defValue: Int): CacheLiveData<Int> {
    return CacheIntLiveData(this, key, defValue)
}

fun ICacheable.stringLiveData(
    key: String,
    defValue: String
): CacheLiveData<String> {
    return CacheStringLiveData(this, key, defValue)
}

fun ICacheable.booleanLiveData(
    key: String,
    defValue: Boolean
): CacheLiveData<Boolean> {
    return CacheBooleanLiveData(this, key, defValue)
}

fun ICacheable.floatLiveData(key: String, defValue: Float): CacheLiveData<Float> {
    return CacheFloatLiveData(this, key, defValue)
}

fun ICacheable.longLiveData(key: String, defValue: Long): CacheLiveData<Long> {
    return CacheLongLiveData(this, key, defValue)
}
