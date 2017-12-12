package com.ns.yc.lifehelper.ui.other.myNews.wxNews.model.api;

import com.ns.yc.lifehelper.api.ConstantALiYunApi;
import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.model.bean.WxNewsDetailBean;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.model.bean.WxNewsSearchBean;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.model.bean.WxNewsTypeBean;

import rx.Observable;


/**
 * Created by PC on 2017/8/30.
 * 作者：PC
 */

public class WxNewsModel {

    private static WxNewsModel model;
    private WxNewsApi mApiService;

    private WxNewsModel() {
        mApiService = RetrofitWrapper
                .getInstance(ConstantALiYunApi.ALiYunJs)
                .create(WxNewsApi.class);
    }

    public static WxNewsModel getInstance(){
        if(model == null) {
            model = new WxNewsModel();
        }
        return model;
    }

    public Observable<WxNewsTypeBean> getWxNewsChannel(String authorization) {
        return mApiService.getWxNewsChannel(authorization);
    }

    public Observable<WxNewsDetailBean> getWxNewsDetail(String authorization ,
                                                        String channelid ,
                                                        String num ,
                                                        String start) {
        return mApiService.getWxNewsDetail(authorization, channelid, num, start);
    }

    public Observable<WxNewsSearchBean> getWxNewsSearch(String authorization , String keyword) {
        return mApiService.getWxNewsSearch(authorization, keyword);
    }

}
