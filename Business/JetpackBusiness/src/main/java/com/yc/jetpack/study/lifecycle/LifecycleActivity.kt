package com.yc.jetpack.study.lifecycle

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.yc.jetpack.R
import com.yc.jetpack.study.livedata.LiveDataActivity
import com.yc.jetpack.study.livedata.TextViewModel
import com.yc.toolutils.AppLogUtils

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
        this.lifecycle.addObserver(MyLifecycleObserver())
    }

}