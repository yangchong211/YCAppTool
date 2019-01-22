package com.ycbjie.android.contract


import com.ycbjie.android.model.bean.BannerBean
import com.ycbjie.android.model.bean.HomeListBean
import com.ycbjie.library.base.mvp.BasePresenter
import com.ycbjie.library.base.mvp.BaseView
import io.reactivex.Observable
import com.ycbjie.android.network.ResponseBean
import org.json.JSONObject

interface AndroidHomeContract {

    interface View : BaseView {
        fun setDataErrorView()
        fun setNetWorkErrorView()
        fun setDataView(bean: ResponseBean<HomeListBean>)
        fun setBannerView(bean: ResponseBean<List<BannerBean>>?)
        fun unCollectArticleSuccess()
        fun unCollectArticleFail(t: Throwable)
        fun collectInArticleSuccess()
        fun collectInArticleFail(t: Throwable)
    }

    interface Presenter : BasePresenter {
        fun getHomeList(page: Int)
        fun getBannerData(isRefresh: Boolean)
        fun unCollectArticle(selectId: Int)
        fun collectInArticle(selectId: Int)
    }

    interface mode {
        fun getHomeList(page: Int): Observable<ResponseBean<HomeListBean>>
        fun getBannerData(): Observable<ResponseBean<List<BannerBean>>>
        fun unCollectArticle(selectId: Int): Observable<ResponseBean<JSONObject>>
        fun collectInArticle(selectId: Int): Observable<ResponseBean<JSONObject>>
    }

}
