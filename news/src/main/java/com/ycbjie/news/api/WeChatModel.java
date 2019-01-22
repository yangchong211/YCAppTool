package com.ycbjie.news.api;

import com.ycbjie.library.http.RetrofitWrapper;
import com.ycbjie.news.model.WeChatBean;

import io.reactivex.Observable;


public class WeChatModel {

    private static WeChatModel model;
    private WeChatApi mApiService;

    private WeChatModel() {
        mApiService = RetrofitWrapper
                .getInstance(ConstantTxApi.TX_HTTP)
                .create(WeChatApi.class);
    }

    public static WeChatModel getInstance(){
        if(model == null) {
            model = new WeChatModel();
        }
        return model;
    }

    public Observable<WeChatBean> getTxNews(String key , int num , int page) {
        return mApiService.getWeChatNews(key, num, page);
    }


    public Observable<WeChatBean> getWXHotSearch(String key , int num , int page , String word) {
        return mApiService.getWXHotSearch(key, num, page ,word);
    }

}
