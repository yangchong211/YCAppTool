package com.ycbjie.douban.api;

import android.content.Context;

import com.ycbjie.library.api.ConstantALiYunApi;
import com.ycbjie.library.http.RetrofitWrapper;
import com.ycbjie.douban.bean.DouMusicDetailBean;

import io.reactivex.Observable;


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
