package com.yc.logupload.config

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import com.yc.appprocesslib.AppStateMonitor
import com.yc.appprocesslib.StateListener

/**
 * @author yangchong
 * email  : yangchong211@163.com
 * time   : 2022/5/11
 * desc   : 初始化配置
 *          实践文档：https://wiki.zuoyebang.cc/pages/viewpage.action?pageId=356760839
 * revise :
 */
@SuppressLint("StaticFieldLeak")
object UploadInitHelper {

    var isInitial = false
        private set
    var config: UploadConfig? = null
        private set
    var mContext: Context ? = null
        private set
    var bean: UploadLogBean? = null
        private set

    @SuppressLint("StaticFieldLeak")
    @Synchronized
    fun init(context: Context, config: UploadConfig?) {
        if (isInitial) {
            return
        }
        isInitial = true
        UploadInitHelper.config = config
        //全局上下文，避免内存泄漏
        val appContext = context as? Application ?: context.applicationContext
        mContext = appContext
        AppStateMonitor.getInstance().registerStateListener(object : StateListener {
            override fun onInForeground() {
                Log.d("AppStateMonitor : " , "在前台")
            }

            override fun onInBackground() {
                Log.d("AppStateMonitor : " , "在后台")
                //todo 根据服务端下发指令，实现切换到后台，符合条件上传日志
                if (config?.isInBackgroundUpload == true){
                    //todo 如果已经上传成功，则不需要再次上传。因此需要记录上传状态
                    Log.d("AppStateMonitor : " , "在后台开始上传日志")
                    //上传日志到网络
                    //val intent = Intent(context, UploadService::class.java)
                    //context.startService(intent)
                }
            }
        })
    }

    fun setUploadConfig(config: UploadConfig) {
        UploadInitHelper.config = config
    }

    fun setUploadBean(bean: UploadLogBean) {
        UploadInitHelper.bean = bean
    }

}