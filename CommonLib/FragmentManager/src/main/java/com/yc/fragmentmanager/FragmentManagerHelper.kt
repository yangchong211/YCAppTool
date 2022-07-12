package com.yc.fragmentmanager

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * @author yangchong
 * email  : yangchong211@163.com
 * time   : 2020/6/6
 * desc   : fragment生命周期管理
 * revise :
 */
class FragmentManagerHelper {

    /**
     * 添加fragment监听，在创建时添加。在Activity中使用
     */
    fun addFragmentLifecycle(activity: FragmentActivity?) {
        if (activity != null) {
            //recursive字段设置true，将自动为所有子fragmentManager注册这个回调
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(callbacks, true)
            info("add fragment lifecycle : " + activity.javaClass.simpleName)
        }
    }

    /**
     * 移除fragment监听，建议在销毁时移除。在Activity中使用
     */
    fun removeFragmentLifecycle(activity: FragmentActivity?) {
        if (activity != null) {
            activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(callbacks)
            info("remove fragment lifecycle : " + activity.javaClass.simpleName)
        }
    }

    /**
     * 添加fragment监听，在创建时添加。建议添加根fragment，在nav框架中可以使用NavHostFragment对象
     */
    fun addFragmentLifecycle(fragment: Fragment?) {
        if (fragment != null && fragment.fragmentManager != null) {
            //recursive字段设置true，将自动为所有子fragmentManager注册这个回调
            fragment.fragmentManager?.registerFragmentLifecycleCallbacks(fragmentCallbacks, true)
            info("add fragment lifecycle : " + fragment.javaClass.simpleName)
        }
    }

    /**
     * 移除fragment监听，建议在销毁时移除
     */
    fun removeFragmentLifecycle(fragment: Fragment?) {
        if (fragment != null && fragment.fragmentManager != null) {
            fragment.fragmentManager?.unregisterFragmentLifecycleCallbacks(fragmentCallbacks)
            info("remove fragment lifecycle : " + fragment.javaClass.simpleName)
        }
    }

    private val callbacks: FragmentLifecycleListener =
        object : FragmentLifecycleListener(){

        }

    private val fragmentCallbacks: FragmentLifecycleListener =
        object : FragmentLifecycleListener(){

        }

    private fun info(s: String) {
        //if (BuildConfig.DEBUG) {
            Log.i(TAG, s)
        //}
    }



    companion object {
        const val TAG = "FragmentLifecycle: "

        @Volatile
        private var helper: FragmentManagerHelper? = null

        fun getInstance(): FragmentManagerHelper? {
            if (helper == null) {
                synchronized(FragmentManagerHelper::class.java) {
                    if (helper == null) {
                        helper = FragmentManagerHelper()
                    }
                }
            }
            return helper
        }
    }

}