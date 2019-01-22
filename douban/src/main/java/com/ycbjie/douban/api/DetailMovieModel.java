package com.ycbjie.douban.api;

import android.content.Context;

import com.ycbjie.library.api.ConstantALiYunApi;
import com.ycbjie.library.http.RetrofitWrapper;
import com.ycbjie.douban.bean.DouMovieDetailBean;

import io.reactivex.Observable;



public class DetailMovieModel {

    private static DetailMovieModel detailMovieModel;
    private DetailMovieApi mApiService;

    public DetailMovieModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(ConstantALiYunApi.API_DOUBAN)
                .create(DetailMovieApi.class);
    }

    public static DetailMovieModel getInstance(Context context){
        if(detailMovieModel == null) {
            detailMovieModel = new DetailMovieModel(context);
        }
        return detailMovieModel;
    }

    public Observable<DouMovieDetailBean> getHotMovie(String id) {
        Observable<DouMovieDetailBean> detailMovie = mApiService.getMovieDetail(id);
        return detailMovie;
    }


}
