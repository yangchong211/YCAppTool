package com.yc.android.contract
import com.yc.android.model.bean.TreeBean
import com.yc.android.network.ResponseBean
import com.yc.library.base.mvp.BasePresenter
import com.yc.library.base.mvp.BaseView


class AndroidKnowledgeContract {

    interface View : BaseView {
        fun getTreeSuccess(bean: ResponseBean<List<TreeBean>>?)
    }

    interface Presenter : BasePresenter {
        fun getKnowledgeTree()
    }


}