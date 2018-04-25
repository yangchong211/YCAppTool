package com.ns.yc.lifehelper.api.http.douban;

import android.content.Context;

import com.ns.yc.lifehelper.api.constantApi.ConstantALiYunApi;
import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.other.douban.douMovie.bean.DouMovieDetailBean;

import rx.Observable;


/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

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
