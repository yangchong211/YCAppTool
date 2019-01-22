package com.ycbjie.android.view.activity

import android.content.Intent
import com.ycbjie.android.R
import com.ycbjie.android.presenter.AndroidCollectPresenter
import com.ycbjie.library.base.mvp.BaseActivity

class AndroidCollectActivity : BaseActivity<AndroidCollectPresenter>() {

    companion object {
        fun lunch(context: AndroidActivity?) {
            context?.startActivity(Intent(context, AndroidCollectActivity::class.java))
        }
    }

    override fun getContentView(): Int {
        return R.layout.base_recycler_view
    }

    override fun initView() {

    }

    override fun initListener() {

    }

    override fun initData() {

    }

}