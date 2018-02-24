package com.ns.yc.lifehelper.api.http.tangshi;

import com.ns.yc.lifehelper.ui.other.myTsSc.bean.TangShiBean;
import com.ns.yc.lifehelper.ui.other.myTsSc.bean.TangShiChapter;
import com.ns.yc.lifehelper.ui.other.myTsSc.bean.TangShiDetail;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

public interface TangShiApi {

    /**
     * 唐诗
     */
    @GET("tangshi/chapter")
    Observable<TangShiChapter> getTangShiAhapter(@Query("appkey") String appkey);

    @GET("tangshi/detail")
    Observable<TangShiDetail> getTangShiDetail(@Query("appkey") String appkey ,
                                               @Query("detailid") String detailid);

    @GET("tangshi/search")
    Observable<TangShiBean> getTangShiSearch(@Query("appkey") String appkey ,
                                             @Query("keyword") String keyword);

    /**
     * 宋词
     */
    @GET("songci/chapter")
    Observable<TangShiChapter> getSongCiAhapter(@Query("appkey") String appkey);

    @GET("songci/detail")
    Observable<TangShiDetail> getSongCiDetail(@Query("appkey") String appkey ,
                                               @Query("detailid") String detailid);

    @GET("songci/search")
    Observable<TangShiBean> getSongCiSearch(@Query("appkey") String appkey ,
                                             @Query("keyword") String keyword);

    /**
     * 元曲
     */
    @GET("songci/chapter")
    Observable<TangShiChapter> getYuanQuAhapter(@Query("appkey") String appkey);

    @GET("songci/detail")
    Observable<TangShiDetail> getYuanQuDetail(@Query("appkey") String appkey ,
                                              @Query("detailid") String detailid);

    @GET("songci/search")
    Observable<TangShiBean> getYuanQuSearch(@Query("appkey") String appkey ,
                                            @Query("keyword") String keyword);
}
