package com.ns.yc.lifehelper.ui.me.model;

import com.ycbjie.library.http.RetrofitWrapper;
import com.ns.yc.lifehelper.model.bean.UpdateBean;

import rx.Observable;


public class MeAppModel {

    private static MeAppModel model;
    private MeApi mApiService;

    private MeAppModel() {
        mApiService = RetrofitWrapper
                .getInstance(MeApi.ME_HOST)
                .create(MeApi.class);
    }

    public static MeAppModel getInstance(){
        if(model == null) {
            model = new MeAppModel();
        }
        return model;
    }


    public Observable<UpdateBean> getVersionInfo() {
        return mApiService.getVersionInfo();
    }


}
