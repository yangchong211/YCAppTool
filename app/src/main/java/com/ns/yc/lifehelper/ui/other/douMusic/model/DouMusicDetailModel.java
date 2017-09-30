package com.ns.yc.lifehelper.ui.other.douMusic.model;

import android.content.Context;

import com.ns.yc.lifehelper.api.ConstantALiYunApi;
import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.other.douMusic.api.DouMusicDetailApi;
import com.ns.yc.lifehelper.ui.other.douMusic.bean.DouMusicDetailBean;

import rx.Observable;


/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

public class DouMusicDetailModel {

    private static DouMusicDetailModel musicDetailModel;
    private DouMusicDetailApi mApiService;

    public DouMusicDetailModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(ConstantALiYunApi.API_DOUBAN)
                .create(DouMusicDetailApi.class);
    }

    public static DouMusicDetailModel getInstance(Context context){
        if(musicDetailModel == null) {
            musicDetailModel = new DouMusicDetailModel(context);
        }
        return musicDetailModel;
    }

    public Observable<DouMusicDetailBean> getMusicDetail(String id) {
        Observable<DouMusicDetailBean> musicDetail = mApiService.getMusicDetail(id);
        return musicDetail;
    }


}
