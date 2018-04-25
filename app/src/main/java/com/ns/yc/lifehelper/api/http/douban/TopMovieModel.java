package com.ns.yc.lifehelper.api.http.douban;

import android.content.Context;

import com.ns.yc.lifehelper.api.constantApi.ConstantALiYunApi;
import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.other.douban.douMovie.bean.DouHotMovieBean;

import rx.Observable;


/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

public class TopMovieModel {

    private static TopMovieModel topMovieModel;
    private TopMovieApi mApiService;

    public TopMovieModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(ConstantALiYunApi.API_DOUBAN)
                .create(TopMovieApi.class);
    }

    public static TopMovieModel getInstance(Context context){
        if(topMovieModel == null) {
            topMovieModel = new TopMovieModel(context);
        }
        return topMovieModel;
    }

    public Observable<DouHotMovieBean> getTopMovie(int start, int count) {
        Observable<DouHotMovieBean> movieTop250 = mApiService.getMovieTop250(start, count);
        return movieTop250;
    }


}
