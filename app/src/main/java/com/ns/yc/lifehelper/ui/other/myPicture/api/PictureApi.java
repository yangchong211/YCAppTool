package com.ns.yc.lifehelper.ui.other.myPicture.api;

import com.ns.yc.lifehelper.ui.other.myNews.txNews.bean.TxNewsBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by PC on 2017/8/22.
 * 作者：PC
 */

public interface PictureApi {

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

}
