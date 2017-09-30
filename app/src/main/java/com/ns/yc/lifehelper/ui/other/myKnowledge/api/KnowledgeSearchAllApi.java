package com.ns.yc.lifehelper.ui.other.myKnowledge.api;

import com.ns.yc.lifehelper.ui.other.myKnowledge.bean.SearchAllBean;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by PC on 2017/8/22.
 * 作者：PC
 */

public interface KnowledgeSearchAllApi {

    /**
     * 全局搜索
     * @param k         搜索关键词
     * @param t         资源类型id
     * @param u         上划/最小排序ID
     * @return
     */
    @POST("/search")
    Observable<SearchAllBean> getSearch(@Query("k") String k,
                                        @Query("t") String t,
                                        @Query("u") String u);

}
