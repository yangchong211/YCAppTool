package com.ycbjie.douban.api;

import android.content.Context;

import com.ycbjie.library.api.ConstantALiYunApi;
import com.ycbjie.library.http.RetrofitWrapper;
import com.ycbjie.douban.bean.DouHotMovieBean;

import io.reactivex.Observable;


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
