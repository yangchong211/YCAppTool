package com.yc.android.contract



import com.yc.library.base.mvp.BasePresenter
import com.yc.library.base.mvp.BaseView

interface AndroidDetailContract {

    interface View : BaseView {

    }

    interface Presenter : BasePresenter {
        fun unCollectArticle(articleId: Int)
        fun collectInArticle(articleId: Int)
    }

}
