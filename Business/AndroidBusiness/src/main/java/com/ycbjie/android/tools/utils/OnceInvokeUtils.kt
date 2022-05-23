package com.ycbjie.android.tools.utils

import com.yc.toolutils.AppLogUtils

class OnceInvokeUtils {

    private val hashMap : HashMap<String,Boolean> = hashMapOf()

    fun invoke(key: String, callback: () -> Unit) {
        // Unit函数相当于java中的void
        // 首先 Unit 本身是一个用 object 表示的单例，所以可以理解为Kotlin有一个类，这个类只有一个单例对象，叫 Unit 。
        // 在Kotlin中，一切方法/函数都是表达式，表达式是总是有值的，所以每一个方法都必有一个返回值。
        // 如果没有用 return 明确的指定，那么一般来说就会用自动帮我们加上 Unit
        if (hashMap[key] != true) {
            AppLogUtils.d("invoke:${key} -> go head")
            callback.invoke()
            hashMap[key] = true
        } else {
            AppLogUtils.d("invoke:${key} -> has been called ,just ignore")
        }
    }

    fun invoke(key: String, listener: CallbackListener) {
        if (hashMap[key] != true) {
            AppLogUtils.d("invoke:\${key} -> go head")
            listener.callback()
            hashMap[key] = true
        } else {
            AppLogUtils.d("invoke:\${key} -> has been called ,just ignore")
        }
    }

    fun reset() {
        AppLogUtils.d("reset")
        hashMap.clear()
    }

    fun reset(key: String) {
        AppLogUtils.d("reset:${key}")
        hashMap.remove(key)
    }

    //接口
    interface CallbackListener {
        fun callback()
    }

}