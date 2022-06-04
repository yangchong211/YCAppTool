package com.yc.android.contract

import com.ycbjie.android.model.bean.ProjectListBean
import com.yc.library.base.mvp.BasePresenter
import com.yc.library.base.mvp.BaseView


interface KnowledgeListContract{

    interface View : BaseView {
        fun loadAllArticles(bean: ProjectListBean?, refresh: Boolean)
        fun getKnowledgeFail(message: String, refresh: Boolean)

    }

    interface Presenter : BasePresenter {
        fun getKnowledgeList(id: Int, refresh: Boolean)
    }

}
