package com.ycbjie.android.contract





import com.ycbjie.android.model.bean.ProjectListBean
import com.ycbjie.android.network.ResponseBean
import com.yc.library.base.mvp.BasePresenter
import com.yc.library.base.mvp.BaseView

interface AndroidCollectContract {

    interface View : BaseView {
        fun setDataView(bean: ResponseBean<ProjectListBean>?)
        fun setDataErrorView(message: String?)
        fun setNetWorkErrorView()
    }

    interface Presenter : BasePresenter {
        fun getCollectArticleList(page: Int)
    }

}
