package com.ycbjie.news.api;

import android.content.Context;

import com.ycbjie.library.http.RetrofitWrapper;
import com.ycbjie.news.model.TodayNewsChannel;
import com.ycbjie.news.model.TodayNewsDetail;
import com.ycbjie.news.model.TodayNewsSearch;

import io.reactivex.Observable;


/**
 * Created by PC on 2017/8/30.
 * 作者：PC
 */

public class TodayNewsModel {

    private static TodayNewsModel model;
    private TodayNewsApi mApiService;

    /**
     * 阿里云极速数据
     * 新闻
     */
    public static final String ALiYunJs = "http://jisuwxwzjx.market.alicloudapi.com";

    public TodayNewsModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(ALiYunJs)
                .create(TodayNewsApi.class);
    }

    public static TodayNewsModel getInstance(Context context){
        if(model == null) {
            model = new TodayNewsModel(context);
        }
        return model;
    }

    public Observable<TodayNewsChannel> getTodayNewsChannel(String authorization) {
        Observable<TodayNewsChannel> todayNewsChannel = mApiService.getTodayNewsChannel(authorization);
        return todayNewsChannel;
    }

    public Observable<TodayNewsDetail> getTodayNewsDetail(String authorization , String channelid , String num , String start) {
        Observable<TodayNewsDetail> todayNewsDetail = mApiService.getTodayNewsDetail(authorization, channelid, num, start);
        return todayNewsDetail;
    }

    public Observable<TodayNewsSearch> getTodayNewsSearch(String authorization , String keyword) {
        Observable<TodayNewsSearch> todayNewsSearch = mApiService.getTodayNewsSearch(authorization, keyword);
        return todayNewsSearch;
    }

}
