package com.ns.yc.lifehelper.api.http.expressDelivery;

import com.ns.yc.lifehelper.ui.other.expressDelivery.bean.ExpressDeliveryBean;

import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;

/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

public interface ExpressDeliveryApi {

    @GET("express/type")
    Observable<ExpressDeliveryBean> getExpressDelivery(@Header("Authorization") String token);

}
