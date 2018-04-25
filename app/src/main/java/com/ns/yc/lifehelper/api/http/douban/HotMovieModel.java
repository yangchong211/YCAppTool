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
