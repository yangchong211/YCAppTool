package com.ns.yc.lifehelper.ui.other.myNews.weChat.model;

import android.content.Context;

import com.ns.yc.lifehelper.api.ConstantTxApi;
import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.other.myNews.weChat.api.WeChatApi;
import com.ns.yc.lifehelper.ui.other.myNews.weChat.bean.WeChatBean;

import rx.Observable;


/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

public class WeChatModel {

    private static WeChatModel model;
    private WeChatApi mApiService;

    public WeChatModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(ConstantTxApi.TX_HTTP)
                .create(WeChatApi.class);
    }

    public static WeChatModel getInstance(Context context){
        if(model == null) {
            model = new WeChatModel(context);
        }
        return model;
    }

    public Observable<WeChatBean> getTxNews(String key , int num , int page) {
        Observable<WeChatBean> weChatNews = mApiService.getWeChatNews(key, num, page);
        return weChatNews;
    }
}
