package com.ns.yc.lifehelper.api.http.news;

import android.content.Context;

import com.ns.yc.lifehelper.api.constantApi.ConstantALiYunApi;
import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.other.myNews.wyNews.bean.TodayNewsChannel;
import com.ns.yc.lifehelper.ui.other.myNews.wyNews.bean.TodayNewsDetail;
import com.ns.yc.lifehelper.ui.other.myNews.wyNews.bean.TodayNewsSearch;

import rx.Observable;


/**
 * Created by PC on 2017/8/30.
 * 作者：PC
 */

public class TodayNewsModel {

    private static TodayNewsModel model;
    private TodayNewsApi mApiService;

    public TodayNewsModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(ConstantALiYunApi.ALiYunJs)
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
