package com.yc.activitymanager.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import java.util.ArrayList
import java.util.HashMap

/**
 * @author yangchong
 * blog  : https://github.com/yangchong211
 * GitHub : https://github.com/yangchong211/YCCommonLib
 * time  : 2018/11/9
 * desc  : 代理类
 * revise:
 */
class ProxyActivityListener : FragmentLifecycleListener() {

    private val mActivityLifecycleListeners: MutableMap<FragmentActivity, MutableList<FragmentLifecycleListener>> =
        HashMap()

    fun registerFragmentLifecycleListener(
        activity: FragmentActivity?,
        lifecycleListener: FragmentLifecycleListener?
    ) {
        if (activity == null || lifecycleListener == null) {
            return
        }
        var lifecycleListeners = mActivityLifecycleListeners[activity]
        if (lifecycleListeners == null) {
            lifecycleListeners = ArrayList()
        }
        lifecycleListeners.add(lifecycleListener)
        mActivityLifecycleListeners[activity] = lifecycleListeners
        //recursive字段设置true，将自动为所有子fragmentManager注册这个回调
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(this, true)
        info("add fragment lifecycle : " + activity.javaClass.simpleName)
    }

    fun unregisterFragmentLifecycleListener(
        activity: FragmentActivity?,
        lifecycleListener: FragmentLifecycleListener?
    ): Boolean {
        if (activity == null || lifecycleListener == null) {
            return false
        }
        synchronized(mActivityLifecycleListeners) {
            val lifecycleListeners = mActivityLifecycleListeners[activity] ?: return false
            val result = lifecycleListeners.remove(lifecycleListener)
            if (lifecycleListeners.size == 0) {
                mActivityLifecycleListeners.remove(activity)
            }
            activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(this)
            info("remove fragment lifecycle : " + activity.javaClass.simpleName)
            return result
        }
    }


    override fun onFragmentAttached(
        fm: FragmentManager,
        f: Fragment, context: Context
    ) {
        if (f.activity == null){
            return
        }
        val lifecycleListeners: List<FragmentLifecycleListener>? =
            mActivityLifecycleListeners[f.activity]
        if (lifecycleListeners == null || lifecycleListeners.isEmpty()) {
            return
        }
        val callbacks: List<FragmentLifecycleListener> = ArrayList(lifecycleListeners)
        for (i in callbacks.indices) {
            callbacks[i].onFragmentAttached(fm, f, context)
        }
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        if (f.activity == null){
            return
        }
        val lifecycleListeners: List<FragmentLifecycleListener>? =
            mActivityLifecycleListeners[f.activity]
        if (lifecycleListeners == null || lifecycleListeners.isEmpty()) {
            return
        }
        val callbacks: List<FragmentLifecycleListener> = ArrayList(lifecycleListeners)
        for (i in callbacks.indices) {
            callbacks[i].onFragmentCreated(fm, f, savedInstanceState)
        }
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        if (f.activity == null){
            return
        }
        val lifecycleListeners: List<FragmentLifecycleListener>? =
            mActivityLifecycleListeners[f.activity]
        if (lifecycleListeners == null || lifecycleListeners.isEmpty()) {
            return
        }
        val callbacks: List<FragmentLifecycleListener> = ArrayList(lifecycleListeners)
        for (i in callbacks.indices) {
            callbacks[i].onFragmentStarted(fm, f)
        }
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        if (f.activity == null){
            return
        }
        val lifecycleListeners: List<FragmentLifecycleListener>? =
            mActivityLifecycleListeners[f.activity]
        if (lifecycleListeners == null || lifecycleListeners.isEmpty()) {
            return
        }
        val callbacks: List<FragmentLifecycleListener> = ArrayList(lifecycleListeners)
        for (i in callbacks.indices) {
            callbacks[i].onFragmentResumed(fm, f)
        }
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        if (f.activity == null){
            return
        }
        val lifecycleListeners: List<FragmentLifecycleListener>? =
            mActivityLifecycleListeners[f.activity]
        if (lifecycleListeners == null || lifecycleListeners.isEmpty()) {
            return
        }
        val callbacks: List<FragmentLifecycleListener> = ArrayList(lifecycleListeners)
        for (i in callbacks.indices) {
            callbacks[i].onFragmentPaused(fm, f)
        }
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        if (f.activity == null){
            return
        }
        val lifecycleListeners: List<FragmentLifecycleListener>? =
            mActivityLifecycleListeners[f.activity]
        if (lifecycleListeners == null || lifecycleListeners.isEmpty()) {
            return
        }
        val callbacks: List<FragmentLifecycleListener> = ArrayList(lifecycleListeners)
        for (i in callbacks.indices) {
            callbacks[i].onFragmentStopped(fm, f)
        }
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        if (f.activity == null){
            return
        }
        val lifecycleListeners: List<FragmentLifecycleListener>? =
            mActivityLifecycleListeners[f.activity]
        if (lifecycleListeners == null || lifecycleListeners.isEmpty()) {
            return
        }
        val callbacks: List<FragmentLifecycleListener> = ArrayList(lifecycleListeners)
        for (i in callbacks.indices) {
            callbacks[i].onFragmentDestroyed(fm, f)
        }
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        if (f.activity == null){
            return
        }
        val lifecycleListeners: List<FragmentLifecycleListener>? =
            mActivityLifecycleListeners[f.activity]
        if (lifecycleListeners == null || lifecycleListeners.isEmpty()) {
            return
        }
        val callbacks: List<FragmentLifecycleListener> = ArrayList(lifecycleListeners)
        for (i in callbacks.indices) {
            callbacks[i].onFragmentDetached(fm, f)
        }
    }

    private fun info(s: String) {
        Log.i(com.yc.activitymanager.fragment.FragmentManager.TAG, s)
    }
}