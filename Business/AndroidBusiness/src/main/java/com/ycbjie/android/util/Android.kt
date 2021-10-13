package com.ycbjie.android.util

class Android{

    //扩展函数
    fun String.lastChar(): Char = this.get(this.length - 1)

    //拓展属性
    val String.lastChar: Char
        get() = get(length - 1)

    fun testFunExtension() {
        val str = "test extension fun"
        println(str.lastChar())

        val s = "abc"
        println(s.lastChar)
    }
}