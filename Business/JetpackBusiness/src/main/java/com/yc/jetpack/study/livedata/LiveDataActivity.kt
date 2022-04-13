package com.yc.jetpack.study.livedata

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.yc.jetpack.R

class LiveDataActivity : AppCompatActivity() {

    private var liveData : MutableLiveData<String> ?= null
    private var viewModel: TextViewModel ?= null
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_state)
        initLive()
        initLiveData()
    }

    private fun initLive(){
        liveData = MutableLiveData()
        liveData?.observe(this, Observer<String?> { newText ->
            // 更新数据
            //tv.setText(newText)
        })
        liveData?.value = "小杨真的是一个逗比么"
    }

    private fun initLiveData() {
        // 创建一个持有某种数据类型的LiveData (通常是在ViewModel中)
        viewModel = ViewModelProviders.of(this).get(TextViewModel::class.java)
        // 创建一个定义了onChange()方法的观察者
        // 开始订阅
        val nameObserver: Observer<String?> =
            Observer<String?> { newText -> // 更新数据
                //tvText.setText(newText)
            }
        // 通过 observe()方法连接观察者和LiveData，注意：observe()方法需要携带一个LifecycleOwner类
        val liveData = viewModel?.currentText
        liveData?.observe(this, nameObserver)
        // 发送数据
        findViewById<View>(R.id.tv_btn).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                count++
                val text: String = when (count % 5) {
                    1 -> "小杨真的是一个逗比么"
                    2 -> "逗比赶紧来star吧"
                    3 -> "小杨想成为大神"
                    4 -> "开始刷新数据啦"
                    else -> "变化成默认的数据"
                }
                viewModel?.currentText?.value = text
            }
        })
    }

}