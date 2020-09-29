package com.ycbjie.android.contract


import com.ycbjie.android.model.bean.ProjectListBean
import com.ycbjie.android.model.bean.SearchTag
import com.ycbjie.library.base.mvp.BasePresenter
import com.ycbjie.library.base.mvp.BaseView

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
