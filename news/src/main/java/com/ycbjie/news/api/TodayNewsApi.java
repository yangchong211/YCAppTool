package com.ycbjie.news.api;

import com.ycbjie.news.model.TodayNewsChannel;
import com.ycbjie.news.model.TodayNewsDetail;
import com.ycbjie.news.model.TodayNewsSearch;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by PC on 2017/8/30.
 * 作者：PC
 */

public interface TodayNewsApi {

    /**
     * 获取新闻频道ID接口数据
     */
    @GET("news/channel")
    Observable<TodayNewsChannel> getTodayNewsChannel(@Header("Authorization") String token);


    /**
     * 获取文章接口
     */
    @GET("news/get")
    Observable<TodayNewsDetail> getTodayNewsDetail(@Header("Authorization") String token,
                                                   @Query("channelid") String channelid,
                                                   @Query("num") String num,
                                                   @Query("start") String intstart);

    /**
     * 关键词搜索文章接口
     */
    @GET("news/search")
    Observable<TodayNewsSearch> getTodayNewsSearch(@Header("Authorization") String token,
                                                   @Query("keyword") String keyword);

}
