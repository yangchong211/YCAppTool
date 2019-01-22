package com.ycbjie.news.api;

import android.content.Context;

import com.ycbjie.library.http.RetrofitWrapper;
import com.ycbjie.news.model.TxNewsBean;

import io.reactivex.Observable;


/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

public class TxNewsModel {

    private static TxNewsModel bookModel;
    private TxNewsApi mApiService;

    public TxNewsModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(ConstantTxApi.TX_HTTP)
                .create(TxNewsApi.class);
    }

    public static TxNewsModel getInstance(Context context){
        if(bookModel == null) {
            bookModel = new TxNewsModel(context);
        }
        return bookModel;
    }

    public Observable<TxNewsBean> getTxNews(String key , int num) {
        Observable<TxNewsBean> txNews = mApiService.getTxNews(key, num);
        return txNews;
    }
    public Observable<TxNewsBean> getTxGnNews(String key , int num) {
        Observable<TxNewsBean> txNews = mApiService.getTxGnNews(key, num);
        return txNews;
    }
    public Observable<TxNewsBean> getTxHbNews(String key , int num) {
        Observable<TxNewsBean> txNews = mApiService.getTxHbNews(key, num);
        return txNews;
    }
    public Observable<TxNewsBean> getTxTyNews(String key , int num) {
        Observable<TxNewsBean> txNews = mApiService.getTxTyNews(key, num);
        return txNews;
    }
    public Observable<TxNewsBean> getTxNbaNews(String key , int num) {
        Observable<TxNewsBean> txNews = mApiService.getTxNbaNews(key, num);
        return txNews;
    }
    public Observable<TxNewsBean> getTxSuNews(String key , int num) {
        Observable<TxNewsBean> txNews = mApiService.getTxSuNews(key, num);
        return txNews;
    }
    public Observable<TxNewsBean> getTxMiNews(String key , int num) {
        Observable<TxNewsBean> txNews = mApiService.getTxMiNews(key, num);
        return txNews;
    }
    public Observable<TxNewsBean> getTxTrNews(String key , int num) {
        Observable<TxNewsBean> txNews = mApiService.getTxTrNews(key, num);
        return txNews;
    }
    public Observable<TxNewsBean> getTxHeNews(String key , int num) {
        Observable<TxNewsBean> txNews = mApiService.getTxHeNews(key, num);
        return txNews;
    }
    public Observable<TxNewsBean> getTxQwNews(String key , int num) {
        Observable<TxNewsBean> txNews = mApiService.getTxQwNews(key, num);
        return txNews;
    }
}
