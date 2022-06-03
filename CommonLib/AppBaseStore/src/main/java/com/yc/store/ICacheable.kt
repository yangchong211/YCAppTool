package com.yc.store

interface ICacheable {

    fun saveInt(key: String, value: Int)

    fun readInt(key: String, default: Int = 0): Int

    fun saveFloat(key: String, value: Float)

    fun readFloat(key: String, default: Float = 0F): Float

    fun saveDouble(key: String, value: Double)

    fun readDouble(key: String, default: Double = 0.0): Double

    fun saveLong(key: String, value: Long)

    fun readLong(key: String, default: Long = 0L): Long

    fun saveString(key: String, value: String)

    fun readString(key: String, default: String = ""): String

    fun saveBoolean(key: String, value: Boolean)

    fun readBoolean(key: String, default: Boolean): Boolean

    fun removeKey(key: String)

    fun totalSize(): Long

    fun clearData()

}