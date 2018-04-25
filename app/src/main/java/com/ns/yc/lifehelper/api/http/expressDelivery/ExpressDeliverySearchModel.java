package com.ns.yc.lifehelper.api.http.expressDelivery;

import android.content.Context;

import com.ns.yc.lifehelper.api.constantApi.ConstantALiYunApi;
import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.other.expressDelivery.bean.ExpressDeliverySearchBean;

import rx.Observable;


/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

public class ExpressDeliverySearchModel {

    private static ExpressDeliverySearchModel expressDeliveryModel;
    private ExpressDeliverySearchApi mApiService;

    public ExpressDeliverySearchModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(ConstantALiYunApi.ExpressDeliveryInquiry)
                .create(ExpressDeliverySearchApi.class);
    }

    public static ExpressDeliverySearchModel getInstance(Context context){
        if(expressDeliveryModel == null) {
            expressDeliveryModel = new ExpressDeliverySearchModel(context);
        }
        return expressDeliveryModel;
    }

    public Observable<ExpressDeliverySearchBean> queryInfo(String header , String number , String type) {
        Observable<ExpressDeliverySearchBean> expressDeliveryCall = mApiService.getExpressDeliverySearch(header,number,type);
        return expressDeliveryCall;
    }


}
