package com.ycbjie.android.util

import android.util.Log

class KotlinTest{

    private val name: Int by lazy { 1 }

    fun printname() {
        println(name)
    }

    /**
     * 测试方法
     */
    fun test(){
        val c1 = C1()
        c1.eat()
        val c2 = C2()
        c2.eat()
        c2.a()
        val c3 = C3()
        c3.c1()
        c3.c2()


        var list1 = arrayOf("数据1","数据2","数据3")
        val a = list1[1]
    }


    /**
     * 继承接口
     */
    class C1 : A{
        override fun eat() {
            Log.d("yc------C1类","调用方法eat")
        }

    }

    /**
     * 实现抽象类
     */
    class C2 : B(){
        override fun eat() {
            Log.d("yc------C2类","调用方法eat")
        }
    }

    /**
     * 实现C父类
     */
    class C3 : C(){
        override fun c2() {
            super.c2()
            Log.d("yc------C3类","调用方法eat")
        }
    }

    class C4 : C() , A{
        override fun eat() {

        }

        override fun interA() {
            //调用A接口类interA方法
            super<A>.interA()
            //调用C类interA方法
            super<C>.interA()
        }
    }

    /**
     * 接口
     */
    interface A{
        fun eat()
        //接口成员默认就是 open 的
        fun interA() {
            Log.d("yc------A类","接口成员默认就是 open 的")
        }
    }

    /**
     * 抽象类
     */
    abstract class B{
        abstract fun eat()
        fun a(){
            Log.d("yc------B类","调用方法a")
        }
    }

    /**
     * 普通类
     */
    open class C{
        fun c1(){
            Log.d("yc------C类","调用方法c1")
        }

        open fun c2(){
            Log.d("yc------C类","调用方法c2")
        }

        open fun interA() {
            Log.d("yc------C类","调用方法interA")
        }
    }




}