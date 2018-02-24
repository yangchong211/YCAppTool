package com.ns.yc.lifehelper.ui.me.model;


import com.ns.yc.lifehelper.model.bean.UpdateBean;

import retrofit2.http.GET;
import rx.Observable;

public interface MeApi {

    String ME_HOST = "http://yczbj.org/app/update";

    /**
     * 获取最新版本信息
     * @return
     */
    @GET("version")
    Observable<UpdateBean> getVersionInfo();

}
