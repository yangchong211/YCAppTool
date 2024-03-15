package com.yc.activitymanager.fragment

import androidx.fragment.app.FragmentActivity

/**
 * @author yangchong
 * time  : 2018/11/9
 * GitHub : https://github.com/yangchong211/YCCommonLib
 * desc  : fragment生命周期管理类
 * revise: 可以监听多个activity的子属所有fragment
 */
class FragmentManager {

    /**
     * 常见代理类监听
     */
    private val mProxyActivityListener = ProxyActivityListener()

    /**
     * 注册activity上所有fragment生命周期变化
     *
     * @param activity          目标activity
     * @param lifecycleListener 对应的监听器
     */
    fun registerActivityLifecycleListener(
        activity: FragmentActivity?,
        lifecycleListener: FragmentLifecycleListener?
    ) {
        if (activity == null || lifecycleListener == null) {
            return
        }
        mProxyActivityListener.registerFragmentLifecycleListener(activity, lifecycleListener)
    }

    /**
     * 解绑注册activity上所有fragment生命周期变化
     *
     * @param activity          目标activity
     * @param lifecycleListener 对应的监听器
     */
    fun unregisterActivityLifecycleListener(
        activity: FragmentActivity?,
        lifecycleListener: FragmentLifecycleListener?
    ) {
        if (activity == null || lifecycleListener == null) {
            return
        }
        mProxyActivityListener.unregisterFragmentLifecycleListener(activity, lifecycleListener)
    }

    companion object {
        const val TAG = "FragmentLifecycle: "

        /**
         * 单例对象
         */
        @Volatile
        private var sInstance: FragmentManager? = null
        val instance: FragmentManager
            get() {
                if (sInstance == null) {
                    synchronized(FragmentManager::class.java) {
                        if (sInstance == null) {
                            sInstance = FragmentManager()
                        }
                    }
                }
                return sInstance!!
            }
    }
}