package com.ycbjie.android.base

import android.content.Context
import com.ycbjie.library.base.app.LibApplication

/**
 * <pre>
 *     @author 杨充
 *     blog  :
 *     time  : 2018/02/30
 *     desc  : app，从集成模式切换到组件模式调用
 *     revise:
 * </pre>
 */

class AndroidApp : LibApplication() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

}