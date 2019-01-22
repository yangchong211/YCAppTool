package com.ycbjie.douban.api;

import android.content.Context;

import com.ycbjie.library.api.ConstantALiYunApi;
import com.ycbjie.library.http.RetrofitWrapper;
import com.ycbjie.douban.bean.DouHotMovieBean;

import io.reactivex.Observable;



public class HotMovieModel {

    private static HotMovieModel hotMovieModel;
    private HotMovieApi mApiService;

    public HotMovieModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(ConstantALiYunApi.API_DOUBAN)
                .create(HotMovieApi.class);
    }

    public static HotMovieModel getInstance(Context context){
        if(hotMovieModel == null) {
            hotMovieModel = new HotMovieModel(context);
        }
        return hotMovieModel;
    }

    public Observable<DouHotMovieBean> getHotMovie() {
        Observable<DouHotMovieBean> hotMovie = mApiService.getHotMovie();
        return hotMovie;
    }


}
