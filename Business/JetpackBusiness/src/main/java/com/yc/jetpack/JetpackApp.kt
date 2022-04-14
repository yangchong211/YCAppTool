package com.yc.jetpack

import android.content.Context
import com.yc.library.base.app.LibApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * @author: 杨充
 * email  : yangchong211@163.com
 * time   : 2017/05/23
 * desc   : 注意，只有从组件library切换到application才会用到
 * revise : v1.2.1 修改了koin初始化
 */
class JetpackApp : LibApplication() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }


    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@JetpackApp)
            androidFileProperties()
            modules(mainModule)
        }
    }
}