package com.yc.store.ext

import com.yc.store.ICacheable
import com.yc.store.ext.*
import kotlin.reflect.KProperty


@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class BoolCache(val key: String, val defValue: Boolean = false)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class IntCache(val key: String, val defValue: Int = 0)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class LongCache(val key: String, val defValue: Long = 0L)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class FloatCache(val key: String, val defValue: Float = 0f)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class StringCache(val key: String, val defValue: String = "")

@Suppress("UNCHECKED_CAST")
operator fun <T, V> ICacheable.getValue(thisRef: T, property: KProperty<*>): V {
    val returnType = property.returnType
    return when {
        returnType.isTypeBoolean -> {
            val preference =
                property.annotations.find { it is BoolCache } as? BoolCache

            if (preference != null) {
                readBoolean(preference.key, preference.defValue) as V
            } else {
                throw IllegalArgumentException("Cacheable is null")
            }
        }
        returnType.isTypeInt -> {
            val preference =
                property.annotations.find { it is IntCache } as? IntCache

            if (preference != null) {
                readInt(preference.key, preference.defValue) as V
            } else {
                throw IllegalArgumentException("Cacheable is null")
            }
        }
        returnType.isTypeLong -> {
            val preference =
                property.annotations.find { it is LongCache } as? LongCache

            if (preference != null) {
                readLong(preference.key, preference.defValue) as V
            } else {
                throw IllegalArgumentException("Cacheable is null")
            }
        }
        returnType.isTypeFloat -> {
            val preference =
                property.annotations.find { it is FloatCache } as? FloatCache

            if (preference != null) {
                readFloat(preference.key, preference.defValue) as V
            } else {
                throw IllegalArgumentException("Cacheable is null")
            }
        }
        returnType.isTypeString -> {
            val preference =
                property.annotations.find { it is StringCache } as? StringCache

            if (preference != null) {
                readString(preference.key, preference.defValue) as V
            } else {
                throw IllegalArgumentException("Cacheable is null")
            }
        }
        else -> {
            throw IllegalArgumentException("Type is not supported")
        }
    }
}

@Suppress("UNCHECKED_CAST")
operator fun <T, V> ICacheable.setValue(thisRef: T, property: KProperty<*>, value: V) {
    val returnType = property.returnType
    when {
        returnType.isTypeBoolean -> {
            val preference =
                property.annotations.find { it is BoolCache } as? BoolCache

            if (preference != null) {
                saveBoolean(preference.key, value as Boolean)
            } else {
                throw IllegalArgumentException("Cacheable is null")
            }
        }
        returnType.isTypeInt -> {
            val preference =
                property.annotations.find { it is IntCache } as? IntCache

            if (preference != null) {
                saveInt(preference.key, value as Int)
            } else {
                throw IllegalArgumentException("Cacheable is null")
            }
        }
        returnType.isTypeLong -> {
            val preference =
                property.annotations.find { it is LongCache } as? LongCache

            if (preference != null) {
                saveLong(preference.key, value as Long)
            } else {
                throw IllegalArgumentException("Cacheable is null")
            }
        }
        returnType.isTypeFloat -> {
            val preference =
                property.annotations.find { it is FloatCache } as? FloatCache

            if (preference != null) {
                saveFloat(preference.key, value as Float)
            } else {
                throw IllegalArgumentException("Cacheable is null")
            }
        }
        returnType.isTypeString -> {
            val preference =
                property.annotations.find { it is StringCache } as? StringCache

            if (preference != null) {
                saveString(preference.key, value as String)
            } else {
                throw IllegalArgumentException("Cacheable is null")
            }
        }
        else -> {
            throw IllegalArgumentException("Type is not supported")
        }
    }
}