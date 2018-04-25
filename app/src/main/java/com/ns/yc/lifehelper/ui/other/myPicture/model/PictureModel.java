package com.ns.yc.lifehelper.ui.other.myPicture.model;

import android.content.Context;

import com.ns.yc.lifehelper.api.constantApi.ConstantImageApi;
import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.other.myNews.txNews.bean.TxNewsBean;
import com.ns.yc.lifehelper.ui.other.myPicture.api.PictureApi;

import rx.Observable;


/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

public class PictureModel {

    private static PictureModel bookModel;
    private PictureApi mApiService;

    public PictureModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(ConstantImageApi.MOVIE_URL_01)
                .create(PictureApi.class);
    }

    public static PictureModel getInstance(Context context){
        if(bookModel == null) {
            bookModel = new PictureModel(context);
        }
        return bookModel;
    }

    public Observable<TxNewsBean> getTxNews(String key , int num) {
        Observable<TxNewsBean> txNews = mApiService.getTxNews(key, num);
        return txNews;
    }
}
