package com.yc.android.model.model

import com.yc.android.contract.AndroidHomeContract
import com.yc.android.model.bean.BannerBean
import com.yc.android.model.bean.HomeListBean
import com.yc.android.model.helper.AndroidHelper
import com.yc.android.network.ResponseBean
import io.reactivex.Observable
import org.json.JSONObject

class HomeModel : AndroidHomeContract.IHomeMode{

    override fun getHomeList(page: Int): Observable<ResponseBean<HomeListBean>> {
        val instance = AndroidHelper.instance()
        return instance.getHomeList(page)
    }

    override fun getBannerData(): Observable<ResponseBean<List<BannerBean>>> {
        val instance = AndroidHelper.instance()
        return instance.getBanner()
    }

    override fun collectInArticle(selectId: Int): Observable<ResponseBean<JSONObject>> {
        val instance = AndroidHelper.instance()
        return instance.collectInArticle(selectId)
    }

    override fun unCollectArticle(selectId: Int): Observable<ResponseBean<JSONObject>> {
        val instance = AndroidHelper.instance()
        return instance.unCollectArticle(selectId)
    }

}