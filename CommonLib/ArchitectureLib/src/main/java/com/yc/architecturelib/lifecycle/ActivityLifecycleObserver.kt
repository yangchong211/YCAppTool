package com.yc.architecturelib.lifecycle

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.yc.toolutils.system.AppLogUtils

class ActivityLifecycleObserver : LifecycleObserver {

    private var fragmentActivity: FragmentActivity?= null

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(lifecycleOwner: LifecycleOwner){
        fragmentActivity = lifecycleOwner as FragmentActivity
        AppLogUtils.d("ActivityLifecycleObserver" , "onCreate")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        AppLogUtils.d("ActivityLifecycleObserver" , "onStart")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        AppLogUtils.d("ActivityLifecycleObserver" , "onResume")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        AppLogUtils.d("ActivityLifecycleObserver" , "onPause")
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        AppLogUtils.d("ActivityLifecycleObserver" , "onStop")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        AppLogUtils.d("ActivityLifecycleObserver" , "onDestroy")
    }

}