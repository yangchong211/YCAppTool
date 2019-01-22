package com.ycbjie.douban.api;

import android.content.Context;

import com.ycbjie.library.api.ConstantALiYunApi;
import com.ycbjie.library.http.RetrofitWrapper;
import com.ycbjie.douban.bean.DouMusicBean;

import io.reactivex.Observable;



public class DouMusicModel {

    private static DouMusicModel musicModel;
    private DouMusicApi mApiService;

    public DouMusicModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(ConstantALiYunApi.API_DOUBAN)
                .create(DouMusicApi.class);
    }

    public static DouMusicModel getInstance(Context context){
        if(musicModel == null) {
            musicModel = new DouMusicModel(context);
        }
        return musicModel;
    }

    public Observable<DouMusicBean> getMusic(String tag, int start , int count) {
        Observable<DouMusicBean> book = mApiService.getMusic(tag, start, count);
        return book;
    }


}
