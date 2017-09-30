package com.ns.yc.lifehelper.ui.other.myNews.weChat.api;

import com.ns.yc.lifehelper.ui.other.myNews.weChat.bean.WeChatBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

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


}
