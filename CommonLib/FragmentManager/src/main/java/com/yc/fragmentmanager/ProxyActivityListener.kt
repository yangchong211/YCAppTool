package com.yc.fragmentmanager

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.util.ArrayList
import java.util.HashMap

/**
 * <pre>
 * @author yangchong
 * blog  : https://github.com/yangchong211
 * GitHub : https://github.com/yangchong211/YCCommonLib
 * time  : 2018/11/9
 * desc  : 代理类
 * revise:
</pre> *
 */
class ProxyActivityListener : FragmentLifecycleListener() {
    private val mActivityLifecycleListeners: MutableMap<Class<Activity>, MutableList<FragmentLifecycleListener>> =
        HashMap()

    fun registerFragmentLifecycleListener(
        clazz: Class<Activity>?,
        lifecycleListener: FragmentLifecycleListener?
    ) {
        if (clazz == null || lifecycleListener == null) {
            return
        }
        var lifecycleListeners = mActivityLifecycleListeners[clazz]
        if (lifecycleListeners == null) {
            lifecycleListeners = ArrayList()
        }
        lifecycleListeners.add(lifecycleListener)
        mActivityLifecycleListeners[clazz] = lifecycleListeners
    }

    fun unregisterFragmentLifecycleListener(
        clazz: Class<Activity>?,
        lifecycleListener: FragmentLifecycleListener?
    ): Boolean {
        if (clazz == null || lifecycleListener == null) {
            return false
        }
        synchronized(mActivityLifecycleListeners) {
            val lifecycleListeners = mActivityLifecycleListeners[clazz] ?: return false
            val result = lifecycleListeners.remove(lifecycleListener)
            if (lifecycleListeners.size == 0) {
                mActivityLifecycleListeners.remove(clazz)
            }
            return result
        }
    }

    override fun onFragmentAttached(
        fm: FragmentManager,
        f: Fragment, context: Context
    ) {
        val lifecycleListeners: List<FragmentLifecycleListener>? =
            mActivityLifecycleListeners.get(f.javaClass)
        if (lifecycleListeners == null || lifecycleListeners.size == 0) {
            return
        }
        val callbacks: List<FragmentLifecycleListener> = ArrayList(lifecycleListeners)
        for (i in callbacks.indices) {
            callbacks[i].onFragmentAttached(fm, f, context)
        }
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        val lifecycleListeners: List<FragmentLifecycleListener>? =
            mActivityLifecycleListeners.get(f.javaClass)
        if (lifecycleListeners == null || lifecycleListeners.size == 0) {
            return
        }
        val callbacks: List<FragmentLifecycleListener> = ArrayList(lifecycleListeners)
        for (i in callbacks.indices) {
            callbacks[i].onFragmentCreated(fm, f, savedInstanceState)
        }
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        val lifecycleListeners: List<FragmentLifecycleListener>? =
            mActivityLifecycleListeners.get(f.javaClass)
        if (lifecycleListeners == null || lifecycleListeners.size == 0) {
            return
        }
        val callbacks: List<FragmentLifecycleListener> = ArrayList(lifecycleListeners)
        for (i in callbacks.indices) {
            callbacks[i].onFragmentStarted(fm, f)
        }
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        val lifecycleListeners: List<FragmentLifecycleListener>? =
            mActivityLifecycleListeners.get(f.javaClass)
        if (lifecycleListeners == null || lifecycleListeners.size == 0) {
            return
        }
        val callbacks: List<FragmentLifecycleListener> = ArrayList(lifecycleListeners)
        for (i in callbacks.indices) {
            callbacks[i].onFragmentResumed(fm, f)
        }
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        val lifecycleListeners: List<FragmentLifecycleListener>? =
            mActivityLifecycleListeners.get(f.javaClass)
        if (lifecycleListeners == null || lifecycleListeners.size == 0) {
            return
        }
        val callbacks: List<FragmentLifecycleListener> = ArrayList(lifecycleListeners)
        for (i in callbacks.indices) {
            callbacks[i].onFragmentPaused(fm, f)
        }
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        val lifecycleListeners: List<FragmentLifecycleListener>? =
            mActivityLifecycleListeners.get(f.javaClass)
        if (lifecycleListeners == null || lifecycleListeners.size == 0) {
            return
        }
        val callbacks: List<FragmentLifecycleListener> = ArrayList(lifecycleListeners)
        for (i in callbacks.indices) {
            callbacks[i].onFragmentStopped(fm, f)
        }
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        val lifecycleListeners: List<FragmentLifecycleListener>? =
            mActivityLifecycleListeners.get(f.javaClass)
        if (lifecycleListeners == null || lifecycleListeners.size == 0) {
            return
        }
        val callbacks: List<FragmentLifecycleListener> = ArrayList(lifecycleListeners)
        for (i in callbacks.indices) {
            callbacks[i].onFragmentDestroyed(fm, f)
        }
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        val lifecycleListeners: List<FragmentLifecycleListener>? =
            mActivityLifecycleListeners.get(f.javaClass)
        if (lifecycleListeners == null || lifecycleListeners.size == 0) {
            return
        }
        val callbacks: List<FragmentLifecycleListener> = ArrayList(lifecycleListeners)
        for (i in callbacks.indices) {
            callbacks[i].onFragmentDetached(fm, f)
        }
    }
}