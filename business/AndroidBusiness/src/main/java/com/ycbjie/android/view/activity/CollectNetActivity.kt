package com.ycbjie.android.view.activity

import android.content.Context
import android.content.Intent
import com.ns.yc.ycstatelib.StateLayoutManager
import com.ycbjie.android.R
import com.ycbjie.library.base.state.BaseStateActivity

class CollectNetActivity : BaseStateActivity(){


    /**
     * 类的拓展，外部类直接访问，不需要对象指针
     */
    companion object {
        fun startActivity(context: Context){
            val intent = Intent(context,CollectNetActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun initStatusLayout() {
        statusLayoutManager = StateLayoutManager.newBuilder(this)
                .contentView(R.layout.base_refresh_recycle)
                .emptyDataView(R.layout.view_custom_empty_data)
                .errorView(R.layout.view_custom_data_error)
                .loadingView(R.layout.view_custom_loading_data)
                .netWorkErrorView(R.layout.view_custom_network_error)
                .build()
    }

    override fun initView() {

    }

    override fun initListener() {

    }

    override fun initData() {

    }

}