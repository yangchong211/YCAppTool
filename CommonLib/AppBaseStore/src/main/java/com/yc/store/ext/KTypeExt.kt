package com.yc.store.ext

import kotlin.reflect.KClass
import kotlin.reflect.KType


private fun KType.isClass(cls: KClass<*>): Boolean {
    return this.classifier == cls
}

val KType.isTypeBoolean: Boolean get() = this.isClass(Boolean::class)
        || this.isClass(java.lang.Boolean::class)

val KType.isTypeInt: Boolean get() = this.isClass(Int::class)
        || this.isClass(java.lang.Integer::class)

val KType.isTypeLong: Boolean get() = this.isClass(Long::class)
        || this.isClass(java.lang.Long::class)

val KType.isTypeFloat: Boolean get() = this.isClass(Float::class)
        || this.isClass(java.lang.Float::class)

val KType.isTypeString: Boolean get() = this.isClass(String::class)
        || this.isClass(java.lang.String::class)