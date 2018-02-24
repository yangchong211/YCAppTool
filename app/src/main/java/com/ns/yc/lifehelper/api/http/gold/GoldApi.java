package com.ns.yc.lifehelper.api.http.gold;

import com.ns.yc.lifehelper.ui.other.gold.model.GoldListBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

public interface GoldApi {

    /**
     * 文章列表
     */
    @GET("1.1/classes/Entry")
    Observable<List<GoldListBean>> getGoldList(@Header("X-LC-Id") String id,
                                               @Header("X-LC-Sign") String sign,
                                               @Query("where") String where,
                                               @Query("order") String order,
                                               @Query("include") String include,
                                               @Query("limit") int limit,
                                               @Query("skip") int skip);

    /**
     * 热门推荐
     */
    @GET("1.1/classes/Entry")
    Observable<List<GoldListBean>> getGoldHot(@Header("X-LC-Id") String id,
                                              @Header("X-LC-Sign") String sign,
                                              @Query("where") String where,
                                              @Query("order") String order,
                                              @Query("include") String include,
                                              @Query("limit") int limit);


}
