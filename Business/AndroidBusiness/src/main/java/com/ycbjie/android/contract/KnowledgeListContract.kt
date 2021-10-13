package com.ycbjie.android.contract

import com.ycbjie.android.model.bean.ProjectListBean
import com.ycbjie.library.base.mvp.BasePresenter
import com.ycbjie.library.base.mvp.BaseView


interface KnowledgeListContract{

    interface View : BaseView {
        fun loadAllArticles(bean: ProjectListBean?, refresh: Boolean)
        fun getKnowledgeFail(message: String, refresh: Boolean)

    }

    interface Presenter : BasePresenter {
        fun getKnowledgeList(id: Int, refresh: Boolean)
    }

}
