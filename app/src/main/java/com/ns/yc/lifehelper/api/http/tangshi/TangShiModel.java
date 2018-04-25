package com.ns.yc.lifehelper.api.http.tangshi;

import android.content.Context;

import com.ns.yc.lifehelper.api.constantApi.ConstantJsApi;
import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.other.myTsSc.bean.TangShiBean;
import com.ns.yc.lifehelper.ui.other.myTsSc.bean.TangShiChapter;
import com.ns.yc.lifehelper.ui.other.myTsSc.bean.TangShiDetail;

import rx.Observable;


/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

public class TangShiModel {

    private static TangShiModel model;
    private TangShiApi mApiService;

    public TangShiModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(ConstantJsApi.JiSuApi)
                .create(TangShiApi.class);
    }

    public static TangShiModel getInstance(Context context){
        if(model == null) {
            model = new TangShiModel(context);
        }
        return model;
    }

    public Observable<TangShiChapter> getTangShiAhapter(String appkey) {
        Observable<TangShiChapter> tangShi = mApiService.getTangShiAhapter(appkey);
        return tangShi;
    }

    public Observable<TangShiDetail> getTangShiDetail(String appkey , String detailid) {
        Observable<TangShiDetail> tangShi = mApiService.getTangShiDetail(appkey ,detailid);
        return tangShi;
    }

    public Observable<TangShiBean> getTangShiSearch(String appkey , String keyword) {
        Observable<TangShiBean> tangShi = mApiService.getTangShiSearch(appkey ,keyword);
        return tangShi;
    }

    public Observable<TangShiChapter> getSongCiAhapter(String appkey) {
        Observable<TangShiChapter> tangShi = mApiService.getTangShiAhapter(appkey);
        return tangShi;
    }

    public Observable<TangShiDetail> getSongCiDetail(String appkey , String detailid) {
        Observable<TangShiDetail> tangShi = mApiService.getTangShiDetail(appkey ,detailid);
        return tangShi;
    }

    public Observable<TangShiBean> getSongCiSearch(String appkey , String keyword) {
        Observable<TangShiBean> tangShi = mApiService.getTangShiSearch(appkey ,keyword);
        return tangShi;
    }

    public Observable<TangShiChapter> getYuanQuAhapter(String appkey) {
        Observable<TangShiChapter> tangShi = mApiService.getYuanQuAhapter(appkey);
        return tangShi;
    }

    public Observable<TangShiDetail> getYuanQuDetail(String appkey , String detailid) {
        Observable<TangShiDetail> tangShi = mApiService.getYuanQuDetail(appkey ,detailid);
        return tangShi;
    }

    public Observable<TangShiBean> getYuanQuSearch(String appkey , String keyword) {
        Observable<TangShiBean> tangShi = mApiService.getYuanQuSearch(appkey ,keyword);
        return tangShi;
    }

}
