package com.ns.yc.lifehelper.ui.other.myNews.wxNews.model;

import com.ns.yc.lifehelper.api.ConstantALiYunApi;
import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.api.WxNewsApi;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.bean.WxNewsDetailBean;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.bean.WxNewsSearchBean;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.bean.WxNewsTypeBean;

import rx.Observable;


/**
 * Created by PC on 2017/8/30.
 * 作者：PC
 */

public class WxNewsModel {

    private static WxNewsModel model;
    private WxNewsApi mApiService;

    public WxNewsModel() {
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
        Observable<WxNewsTypeBean> wxNewsChannel = mApiService.getWxNewsChannel(authorization);
        return wxNewsChannel;
    }

    public Observable<WxNewsDetailBean> getWxNewsDetail(String authorization , String channelid , String num , String start) {
        Observable<WxNewsDetailBean> wxNewsDetail = mApiService.getWxNewsDetail(authorization, channelid, num, start);
        return wxNewsDetail;
    }

    public Observable<WxNewsSearchBean> getWxNewsSearch(String authorization , String keyword) {
        Observable<WxNewsSearchBean> wxNewsSearch = mApiService.getWxNewsSearch(authorization, keyword);
        return wxNewsSearch;
    }

}
