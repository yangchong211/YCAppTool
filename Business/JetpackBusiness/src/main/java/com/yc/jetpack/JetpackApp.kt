package com.yc.jetpack

import android.content.Context
import com.yc.library.base.app.LibApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

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