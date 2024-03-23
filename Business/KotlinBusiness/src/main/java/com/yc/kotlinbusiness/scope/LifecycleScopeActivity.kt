package com.yc.kotlinbusiness.scope

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.yc.kotlinbusiness.R
import com.yc.toolutils.system.AppLogUtils
import kotlinx.coroutines.*

class LifecycleScopeActivity : AppCompatActivity(){

    /**
     * Kotlin 协程提供了一个可供您编写异步代码的 API。通过 Kotlin 协程，您可以定义 CoroutineScope，以帮助您管理何时应运行协程。每个异步操作都在特定范围内运行。
     * 本主题中介绍的内置协程范围包含在每个相应组件的 KTX 扩展程序中。请务必在使用这些范围时添加相应的依赖项。
     * 对于 ViewModelScope，请使用 androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0 或更高版本。
     * 对于 LifecycleScope，请使用 androidx.lifecycle:lifecycle-runtime-ktx:2.4.0 或更高版本。
     * 对于 liveData，请使用 androidx.lifecycle:lifecycle-livedata-ktx:2.4.0 或更高版本。
     */

    private val TAG = "LifecycleScopeActivity: "


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kotlin_lifecycle_scope)

        findViewById<TextView>(R.id.tv_1).setOnClickListener {
            dispatcherScope1()
        }
        findViewById<TextView>(R.id.tv_2).setOnClickListener {
            job?.cancel()
        }
        findViewById<TextView>(R.id.tv_3).setOnClickListener {
            dispatcherScope2()
        }
        findViewById<TextView>(R.id.tv_4).setOnClickListener {
            dispatcherScope3()
        }
        findViewById<TextView>(R.id.tv_5).setOnClickListener {

        }
        findViewById<TextView>(R.id.tv_6).setOnClickListener {

        }
        findViewById<TextView>(R.id.tv_7).setOnClickListener {

        }
        findViewById<TextView>(R.id.tv_8).setOnClickListener {

        }
    }

    private var job: Job ?= null
    private fun dispatcherScope1() {
        job = lifecycleScope.launch(Dispatchers.Main) {
            AppLogUtils.d(TAG+"job start")
            delay(5000)
            AppLogUtils.d(TAG+"job end")
            //思考这种是否会阻塞线程
        }
    }

    private fun dispatcherScope2() {
        lifecycleScope.launch(Dispatchers.Default) {
            AppLogUtils.d(TAG+"job start")
            delay(5000)
            AppLogUtils.d(TAG+"job end")
        }
    }

    private fun dispatcherScope3() {
        lifecycleScope.launch(Dispatchers.IO) {
            AppLogUtils.d(TAG+"job start")
            delay(5000)
            AppLogUtils.d(TAG+"job end")
        }
    }

}