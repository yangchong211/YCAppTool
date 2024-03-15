package com.yc.activitymanager.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * @author yangchong
 * blog  : https://github.com/yangchong211
 * GitHub : https://github.com/yangchong211/YCCommonLib
 * time  : 2018/11/9
 * desc  : Fragment生命周期
 * revise:
 */
abstract class FragmentLifecycleListener : FragmentManager.FragmentLifecycleCallbacks() {


    override fun onFragmentPreAttached(
        fm: FragmentManager,
        f: Fragment,
        context: Context
    ) {
        super.onFragmentPreAttached(fm, f, context)
    }

    override fun onFragmentAttached(
        fm: FragmentManager,
        f: Fragment,
        context: Context
    ) {
        super.onFragmentAttached(fm, f, context)
    }

    override fun onFragmentCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        super.onFragmentCreated(fm, f, savedInstanceState)
        info("fragment lifecycle  onFragmentCreated : " + f.javaClass.simpleName)
    }

    override fun onFragmentActivityCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        super.onFragmentActivityCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState)
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        super.onFragmentStarted(fm, f)
        info("fragment lifecycle  onFragmentStarted : " + f.javaClass.simpleName)
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        super.onFragmentResumed(fm, f)
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        super.onFragmentPaused(fm, f)
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        super.onFragmentStopped(fm, f)
        info("fragment lifecycle  onFragmentStopped : " + f.javaClass.simpleName)
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        super.onFragmentViewDestroyed(fm, f)
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        super.onFragmentDestroyed(fm, f)
        info("fragment lifecycle  onFragmentDestroyed : " + f.javaClass.simpleName)
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        super.onFragmentDetached(fm, f)
    }

    private fun info(s: String) {
        Log.i(com.yc.activitymanager.fragment.FragmentManager.TAG, s)
    }

}