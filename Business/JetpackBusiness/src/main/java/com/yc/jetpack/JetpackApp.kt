package com.yc.jetpack

import android.content.Context
import com.yc.library.base.app.LibApplication

/**
 * <pre>
 * @author 杨充
 * blog  : https://github.com/yangchong211
 * time  : 2017/05/23
 * desc  : 注意，只有从组件library切换到application才会用到
 * revise:
</pre> *
 */
class JetpackApp : LibApplication() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }
}