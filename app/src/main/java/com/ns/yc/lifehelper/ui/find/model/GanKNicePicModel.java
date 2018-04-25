package com.ns.yc.lifehelper.ui.find.model;

import android.content.Context;

import com.ns.yc.lifehelper.api.constantApi.ConstantGanKApi;
import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.find.model.api.GanKNicePicApi;
import com.ns.yc.lifehelper.ui.find.model.bean.GanKNicePicBean;

import rx.Observable;


/**
 * Created by PC on 2017/8/30.
 * 作者：PC
 */

public class GanKNicePicModel {

    private static GanKNicePicModel model;
    private GanKNicePicApi mApiService;

    public GanKNicePicModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(ConstantGanKApi.GAN_K_IO_API)
                .create(GanKNicePicApi.class);
    }

    public static GanKNicePicModel getInstance(Context context){
        if(model == null) {
            model = new GanKNicePicModel(context);
        }
        return model;
    }

    public Observable<GanKNicePicBean> getGanKNicePic(String size , String page) {
        Observable<GanKNicePicBean> ganKNicePic = mApiService.getGanKNicePic(size, page);
        return ganKNicePic;
    }

}
