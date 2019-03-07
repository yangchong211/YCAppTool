package com.ycbjie.android.base

import com.ycbjie.library.base.app.InitializeService
import com.ycbjie.library.base.app.LibApplication
import com.ycbjie.library.base.config.AppConfig
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
        AppConfig.INSTANCE.initConfig(this)
        //在子线程中初始化
        InitializeService.start(this)
    }



}