package com.yc.jetpack.study.model

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yc.jetpack.R
import org.koin.android.viewmodel.ext.android.viewModel

class SavedStateActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_state)
        getHaveParamsModel()

        // Show the ViewModel property's value in a TextView
        val liveData = mSavedStateViewModel?.getName()
        liveData?.observe(this, Observer { savedString ->
            val str = savedString ?: this.resources.getString(R.string.app_name)
            (findViewById<View>(R.id.saved_vm_tv) as TextView).text = str
        })

        // Save button
        findViewById<View>(R.id.save_bt).setOnClickListener {
            val newName = (findViewById<View>(R.id.name_et) as EditText).text.toString()
            //处理model请求
            mSavedStateViewModel?.saveNewName(newName)
        }
    }

    //这种方式报错
    //var mySavedStateViewModel1: SavedStateViewModel by viewModel()
    // 这种是在koin初始化时候，将model添加进去，因此可以通过该种方式获取viewModel对象
    val mySavedStateViewModel2  by viewModel<SavedStateViewModel>()
    val myViewModel: SavedDataViewModel by viewModel()

    //下面这两种，是使用原始的方式获取viewModel对象
    private var mSavedStateViewModel: SavedStateViewModel? = null
    private fun getHaveParamsModel() {
        // 不带参数的ViewModel获取方法使用非常简单
        mSavedStateViewModel = ViewModelProvider(this).get(
            SavedStateViewModel::class.java
        )
        // 获取带有参数的ViewModel
        val viewModel = ViewModelProvider(this,
            SavedDataViewModel.ViewModeFactory(SavedDataRepository()))
    }


}