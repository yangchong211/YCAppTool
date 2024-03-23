package com.yc.jetpack.study.lifecycle

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.yc.architecturelib.lifecycle.ActivityLifecycleObserver
import com.yc.jetpack.R
import com.yc.jetpack.study.livedata.TextViewModel
import com.yc.toolutils.system.AppLogUtils


class LifecycleActivity : AppCompatActivity() {

    private var liveData : MutableLiveData<String> ?= null
    private var viewModel: TextViewModel?= null
    private var count = 0

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, LifecycleActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_state)
        initLive()
        testLifecycle()
        AppLogUtils.d("LifecycleActivity" , "onCreate")
    }

    override fun onResume() {
        super.onResume()
        AppLogUtils.d("LifecycleActivity" , "onResume")
    }

    override fun onStart() {
        super.onStart()
        AppLogUtils.d("LifecycleActivity" , "onStart")
    }

    override fun onStop() {
        super.onStop()
        AppLogUtils.d("LifecycleActivity" , "onStop")
    }

    private fun initLive(){
        this.lifecycle.addObserver(ActivityLifecycleObserver())
    }

    private fun testLifecycle() {
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() {
                AppLogUtils.d("LifecycleObserver onCreate() called")
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                AppLogUtils.d("LifecycleObserver onResume() called")
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                AppLogUtils.d("LifecycleObserver onDestroy() called")
            }
        })
    }

}