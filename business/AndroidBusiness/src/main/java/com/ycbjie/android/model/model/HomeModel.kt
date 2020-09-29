package com.ycbjie.android.model.model

import com.ycbjie.android.contract.AndroidHomeContract
import com.ycbjie.android.model.bean.BannerBean
import com.ycbjie.android.model.helper.AndroidHelper
import com.ycbjie.android.model.bean.HomeListBean
import com.ycbjie.android.network.ResponseBean
import io.reactivex.Observable
import org.json.JSONObject

class HomeModel : AndroidHomeContract.mode{

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