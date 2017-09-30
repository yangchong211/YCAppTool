package com.ns.yc.lifehelper.ui.other.douMusic.model;

import android.content.Context;

import com.ns.yc.lifehelper.api.ConstantALiYunApi;
import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.other.douMusic.api.DouMusicApi;
import com.ns.yc.lifehelper.ui.other.douMusic.bean.DouMusicBean;

import rx.Observable;


/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

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
