package com.ycbjie.news.api;

import com.ycbjie.news.model.TxNewsBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by PC on 2017/8/22.
 * 作者：PC
 */

public interface TxNewsApi {

    /**
     * 获取新闻数据
     * http://api.tianapi.com/social/?key=APIKEY&num=10         社会新闻
     * http://api.tianapi.com/guonei/?key=APIKEY&num=10         国内新闻
     * http://api.tianapi.com/huabian/?key=APIKEY&num=10	    娱乐新闻
     * http://api.tianapi.com/tiyu/?key=APIKEY&num=10           体育新闻
     * http://api.tianapi.com/nba/?key=APIKEY&num=10            NBA新闻
     * http://api.tianapi.com/startup/?key=APIKEY&num=10        创业新闻
     * http://api.tianapi.com/military/?key=APIKEY&num=10       军事新闻
     * http://api.tianapi.com/travel/?key=APIKEY&num=10         旅游咨询
     * http://api.tianapi.com/health/?key=APIKEY&num=10         健康知识
     * http://api.tianapi.com/qiwen/?key=APIKEY&num=10         奇闻异事
     */
    @GET("/social/")
    Observable<TxNewsBean> getTxNews(@Query("key") String key,
                                     @Query("num") int num);


    @GET("/guonei/")
    Observable<TxNewsBean> getTxGnNews(@Query("key") String key,
                                     @Query("num") int num);

    @GET("/huabian/")
    Observable<TxNewsBean> getTxHbNews(@Query("key") String key,
                                       @Query("num") int num);


    @GET("/tiyu/")
    Observable<TxNewsBean> getTxTyNews(@Query("key") String key,
                                       @Query("num") int num);

    @GET("/nba/")
    Observable<TxNewsBean> getTxNbaNews(@Query("key") String key,
                                       @Query("num") int num);


    @GET("/startup/")
    Observable<TxNewsBean> getTxSuNews(@Query("key") String key,
                                       @Query("num") int num);

    @GET("/military/")
    Observable<TxNewsBean> getTxMiNews(@Query("key") String key,
                                       @Query("num") int num);


    @GET("/travel/")
    Observable<TxNewsBean> getTxTrNews(@Query("key") String key,
                                       @Query("num") int num);

    @GET("/health/")
    Observable<TxNewsBean> getTxHeNews(@Query("key") String key,
                                       @Query("num") int num);

    @GET("/qiwen/")
    Observable<TxNewsBean> getTxQwNews(@Query("key") String key,
                                       @Query("num") int num);
}
