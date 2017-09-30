package com.ns.yc.lifehelper.ui.other.myKnowledge.api;

import com.ns.yc.lifehelper.ui.other.myKnowledge.bean.SearchHotBean;

import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by PC on 2017/8/22.
 * 作者：PC
 */

public interface KnowledgeSearchHotApi {

    /**
     * 根据热门搜索标签
     *              https://nsapi.pedaily.cn/search/hot/news
     */

    @POST("/search/hot/news")
    Observable<SearchHotBean> getHotTags();

}
