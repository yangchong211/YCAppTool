package com.ns.yc.lifehelper.ui.other.myNews.wxNews.api;

import com.ns.yc.lifehelper.ui.other.myNews.wxNews.bean.WxNewsDetailBean;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.bean.WxNewsSearchBean;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.bean.WxNewsTypeBean;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by PC on 2017/8/30.
 * 作者：PC
 */

public interface WxNewsApi {

    /**
     * 获取新闻频道ID接口数据
     */
    @GET("weixinarticle/channel")
    Observable<WxNewsTypeBean> getWxNewsChannel(@Header("Authorization") String token);

    /**
     * 获取文章接口
     */
    @GET("weixinarticle/get")
    Observable<WxNewsDetailBean> getWxNewsDetail(@Header("Authorization") String token,
                                                 @Query("channelid") String channelid,
                                                 @Query("num") String num,
                                                 @Query("start") String intstart);

    /**
     * 关键词搜索文章接口
     */
    @GET("weixinarticle/search")
    Observable<WxNewsSearchBean> getWxNewsSearch(@Header("Authorization") String token,
                                                 @Query("keyword") String keyword);

}
