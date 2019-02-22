package com.ycbjie.android.view.activity

import android.app.Activity
import android.content.Intent
import com.ycbjie.android.R
import com.ycbjie.android.contract.AndroidCollectContract
import com.ycbjie.android.presenter.AndroidCollectPresenter
import com.ycbjie.library.base.mvp.BaseActivity

class AndroidCollectActivity : BaseActivity<AndroidCollectPresenter>()
        , AndroidCollectContract.View{

    private var persenter : AndroidCollectPresenter ?= null
    private var page = 1

    companion object {
        fun lunch(context: Activity?) {
            context?.startActivity(Intent(context, AndroidCollectActivity::class.java))
        }
    }

    override fun getContentView(): Int {
        return R.layout.base_recycler_view
    }

    override fun initView() {
        initRecyclerView()
    }

    override fun initListener() {

    }

    override fun initData() {

    }

    private fun initRecyclerView() {

    }

}