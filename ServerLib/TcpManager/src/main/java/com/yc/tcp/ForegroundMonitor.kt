package com.yc.tcp

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle

/**
 * 监听app 前/后台状态切换
 */
class ForegroundMonitor(val callBack: ChangeForegroundCallBack) :
    Application.ActivityLifecycleCallbacks {

    var isForeground: Boolean = false
        set(value) {
            field = value
            callBack.invoke(value)
        }

    private var resumedActivityCount = 0

    var isFirstActivityResumed: Boolean = false
    override fun onActivityResumed(activity: Activity) {
        resumedActivityCount++
        if (resumedActivityCount > 0) {
            isForeground = true

        }
        isFirstActivityResumed = true
        //LogUtils.d("onActivityResumed:${resumedActivityCount},${activity::class.java.simpleName},isForground:${isForground}")
    }


    override fun onActivityPaused(activity: Activity) {

        resumedActivityCount--
        if (resumedActivityCount <= 0) {
            isForeground = false
        }
        //LogUtils.d("onActivityPaused:${resumedActivityCount},${activity::class.java.simpleName},isForground:${isForground}")
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }


    fun register(context: Context) {
        if (context is Application) {
            context.registerActivityLifecycleCallbacks(this)
        } else {
            throw IllegalArgumentException("context must be Application")
        }
    }

    fun unRegister(context: Context) {
        if (context is Application) {
            context.unregisterActivityLifecycleCallbacks(this)
        } else {
            throw IllegalArgumentException("context must be Application")
        }
    }

}
typealias ChangeForegroundCallBack = (foreground: Boolean) -> Unit