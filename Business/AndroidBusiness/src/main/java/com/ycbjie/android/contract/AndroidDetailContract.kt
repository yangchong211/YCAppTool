package com.ycbjie.android.contract



import com.ycbjie.library.base.mvp.BasePresenter
import com.ycbjie.library.base.mvp.BaseView

interface AndroidDetailContract {

    interface View : BaseView {

    }

    interface Presenter : BasePresenter {
        fun unCollectArticle(articleId: Int)
        fun collectInArticle(articleId: Int)
    }

}
