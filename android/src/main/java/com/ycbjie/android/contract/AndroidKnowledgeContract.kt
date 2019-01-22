package com.ycbjie.android.contract
import com.ycbjie.android.model.bean.TreeBean
import com.ycbjie.library.base.mvp.BasePresenter
import com.ycbjie.library.base.mvp.BaseView
import com.ycbjie.android.network.ResponseBean


class AndroidKnowledgeContract {

    interface View : BaseView {
        fun getTreeSuccess(bean: ResponseBean<List<TreeBean>>?)
    }

    interface Presenter : BasePresenter {
        fun getKnowledgeTree()
    }


}