package com.yc.jetpack.study.lifecycle

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.yc.toolutils.AppLogUtils

class MyLifecycleObserver : LifecycleObserver {

    private lateinit var hostActivity: AppCompatActivity

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(lifecycleOwner: LifecycleOwner) {
        hostActivity = lifecycleOwner as AppCompatActivity
        AppLogUtils.d("MyLifecycleObserver" , "onCreate")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        AppLogUtils.d("MyLifecycleObserver" , "onResume")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        AppLogUtils.d("MyLifecycleObserver" , "onStart")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        AppLogUtils.d("MyLifecycleObserver" , "onStop")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        AppLogUtils.d("MyLifecycleObserver" , "onDestroy")
    }

}