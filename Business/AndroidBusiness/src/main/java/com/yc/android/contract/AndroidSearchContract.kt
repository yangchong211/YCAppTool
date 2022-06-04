package com.yc.android.contract


import com.yc.android.model.bean.ProjectListBean
import com.yc.android.model.bean.SearchTag
import com.yc.library.base.mvp.BasePresenter
import com.yc.library.base.mvp.BaseView

class AndroidSearchContract{

    interface View : BaseView {
        fun setSearchTagSuccess(t: MutableList<SearchTag>?)
        fun setSearchTagFail(message: String)
        fun setAllData(t: ProjectListBean?, refresh: Boolean)
        fun setSearchResultSuccess(t: ProjectListBean?, refresh: Boolean)
        fun setSearchResultFail(message: String)
    }

    interface Presenter : BasePresenter {
        fun search(str: String, b: Boolean)
        fun getSearchTag()
    }

}
