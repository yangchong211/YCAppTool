package com.ns.yc.lifehelper.api.http.news;

import com.ns.yc.lifehelper.api.constantApi.ConstantTxApi;
import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.other.myNews.weChat.model.bean.WeChatBean;

import rx.Observable;


/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

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
