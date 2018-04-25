package com.ns.yc.lifehelper.ui.other.myKnowledge.model;

import android.content.Context;

import com.ns.yc.lifehelper.api.constantApi.ConstantGanKApi;
import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.other.myKnowledge.api.GanKIoOtherApi;
import com.ns.yc.lifehelper.ui.other.myKnowledge.bean.GanKEveryDay;
import com.ns.yc.lifehelper.ui.other.myKnowledge.bean.GanKIoDataBean;

import rx.Observable;


/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

public class GanKIoDataModel {

    private static GanKIoDataModel ganKModel;
    private GanKIoOtherApi mApiService;

    public GanKIoDataModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(ConstantGanKApi.GAN_K_IO_API)
                .create(GanKIoOtherApi.class);
    }

    public static GanKIoDataModel getInstance(Context context){
        if(ganKModel == null) {
            ganKModel = new GanKIoDataModel(context);
        }
        return ganKModel;
    }

    public Observable<GanKIoDataBean> getGanKData(String type , int page , int pre_page) {
        Observable<GanKIoDataBean> gankIoData = mApiService.getGanKIoData(type, page, pre_page);
        return gankIoData;
    }

    public Observable<GanKEveryDay> getGanKIoDay(String year , String month , String day) {
        Observable<GanKEveryDay> gankIoDay = mApiService.getGanKIoDay(year, month, day);
        return gankIoDay;
    }


}
