package com.yc.store

import android.app.Application

/**
 * @author 杨充
 * blog  : https://github.com/yangchong211
 * time  : 2017/05/23
 * desc  : 初始化工具类，获取上下文，会自动初始化
 * revise:
 */
object StoreToolHelper {

    private var sApplication: Application? = null

    fun init(app: Application?) {
        if (app == null) {
            throw NullPointerException("Argument 'app' of type Application (#0 out of 1, zero-based) is marked by @android.support.annotation.NonNull but got null for it")
        } else {
            sApplication = app
        }
    }

    val app: Application?
        get() = if (sApplication != null) {
            sApplication
        } else {
            throw NullPointerException("u should init first")
        }
}