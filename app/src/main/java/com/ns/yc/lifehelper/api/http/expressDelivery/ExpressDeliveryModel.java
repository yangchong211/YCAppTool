package com.ns.yc.lifehelper.api.http.expressDelivery;

import android.content.Context;

import com.ns.yc.lifehelper.api.constantApi.ConstantALiYunApi;
import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.other.expressDelivery.bean.ExpressDeliveryBean;

import rx.Observable;


/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

public class ExpressDeliveryModel {

    private static ExpressDeliveryModel expressDeliveryModel;
    private ExpressDeliveryApi mApiService;

    public ExpressDeliveryModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(ConstantALiYunApi.ExpressDelivery)
                .create(ExpressDeliveryApi.class);
    }

    public static ExpressDeliveryModel getInstance(Context context){
        if(expressDeliveryModel == null) {
            expressDeliveryModel = new ExpressDeliveryModel(context);
        }
        return expressDeliveryModel;
    }

    public Observable<ExpressDeliveryBean> queryInfo(String header) {
        Observable<ExpressDeliveryBean> expressDeliveryCall = mApiService.getExpressDelivery(header);
        return expressDeliveryCall;
    }


}
