package com.ycbjie.news.api;


import com.ycbjie.news.model.WeChatBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by PC on 2017/9/5.
 * 作者：PC
 */

public interface WeChatApi {

    /**
     * http://api.tianapi.com/wxnew/?key=APIKEY&num=10
     */
    @GET("wxnew/")
    Observable<WeChatBean> getWeChatNews(@Query("key") String key,
                                         @Query("num") int num,
                                         @Query("page") int page);


    /**
     * 微信精选列表
     */
    @GET("wxnew")
    Observable<WeChatBean> getWXHotSearch(@Query("key") String key,
                                                @Query("num") int num,
                                                @Query("page") int page,
                                                @Query("word") String word);


}
