package com.yc.jetpack.study.livedata

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.yc.jetpack.R
import com.yc.jetpack.study.lifecycle.LifecycleActivity
import com.yc.jetpack.study.navigation.NavigationActivity

class LiveDataActivity : AppCompatActivity() {

    private var liveData : MutableLiveData<String> ?= null
    private var viewModel: TextViewModel ?= null
    private var count = 0
    private var tvName: TextView? = null
    private var btnSave: Button? = null
    private var tvSavedVm: TextView? = null
    private var tvTextNext: TextView? = null

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, LiveDataActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data)
        initView()
        initLive()
        initLiveData()
    }

    override fun onResume() {
        super.onResume()
        liveData?.value = "onResume"
    }

    override fun onPause() {
        super.onPause()
        liveData?.value = "onPause"
    }

    override fun onStop() {
        super.onStop()
        liveData?.value = "onStop"
    }

    override fun onStart() {
        super.onStart()
        liveData?.value = "onStart"
    }

    override fun onDestroy() {
        super.onDestroy()
        liveData?.value = "onDestroy"
    }

    private fun initView() {
        tvName = findViewById(R.id.tv_name)
        btnSave = findViewById(R.id.btn_save)
        tvSavedVm = findViewById(R.id.tv_saved_vm)
        tvTextNext = findViewById(R.id.tv_text_next)
    }

    private fun initLive(){
        liveData = MutableLiveData<String>()
        liveData?.observe(this, Observer<String?> { newText ->
            // 更新数据
            tvName?.text = newText
            Log.i("LiveDataActivity: " , lifecycle.currentState.toString())
        })
        liveData?.value = "onCreate"
    }

    private fun initLiveData() {
        // 创建一个持有某种数据类型的LiveData (通常是在ViewModel中)
        viewModel = ViewModelProviders.of(this).get(TextViewModel::class.java)
        // 创建一个定义了onChange()方法的观察者
        // 开始订阅1
        val nameObserver: Observer<String?> = Observer<String?> { newText ->
            // 更新数据
            tvSavedVm?.text = newText
        }
        // 通过 observe()方法连接观察者和LiveData，注意：observe()方法需要携带一个LifecycleOwner类
        viewModel?.getCurrentText()?.observe(this, nameObserver)


        // 开始订阅2
        viewModel?.getNextText()?.observe(this){
            Log.d("返回的数据是：","${it?.toString()}")
            tvTextNext?.text = it.toString()
        }

        // 发送数据
        btnSave?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                count++
                val text: String = when (count % 5) {
                    1 -> "小杨真的是一个逗比么"
                    2 -> "逗比赶紧来star吧"
                    3 -> "小杨想成为大神"
                    4 -> "开始刷新数据啦"
                    else -> "变化成默认的数据"
                }
                viewModel?.getCurrentText()?.value = text
                viewModel?.getNextText()?.value = "yc: $text"

                if (count % 5 == 3){
                    LifecycleActivity.startActivity(this@LiveDataActivity)
                }
            }
        })
    }

}