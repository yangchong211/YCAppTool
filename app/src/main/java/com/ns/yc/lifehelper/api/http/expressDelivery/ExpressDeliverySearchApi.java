package com.ns.yc.lifehelper.api.http.expressDelivery;

import com.ns.yc.lifehelper.ui.other.expressDelivery.bean.ExpressDeliverySearchBean;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

public interface ExpressDeliverySearchApi {

    @GET("express/query")
    Observable<ExpressDeliverySearchBean> getExpressDeliverySearch(@Header("Authorization") String token,
                                                                   @Query("number") String number,
                                                                   @Query("type") String type);

}
