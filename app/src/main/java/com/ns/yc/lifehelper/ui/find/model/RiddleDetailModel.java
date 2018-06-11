package com.ns.yc.lifehelper.ui.find.model;

import android.content.Context;

import com.ns.yc.lifehelper.api.constantApi.ConstantALiYunApi;
import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.find.model.api.RiddleDetailApi;
import com.ns.yc.lifehelper.ui.find.model.bean.RiddleDetailBean;

import rx.Observable;


/**
 * Created by PC on 2017/8/30.
 * 作者：PC
 */

public class RiddleDetailModel {

    private static RiddleDetailModel model;
    private RiddleDetailApi mApiService;

    public RiddleDetailModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(ConstantALiYunApi.ALiYunJsRiddle)
                .create(RiddleDetailApi.class);
    }

    public static RiddleDetailModel getInstance(Context context){
        if(model == null) {
            model = new RiddleDetailModel(context);
        }
        return model;
    }


    public Observable<RiddleDetailBean> getRiddleDetail(String authorization,
                                                        String classid,
                                                        String keyword,
                                                        String pagenum,
                                                        String pagesize) {
        Observable<RiddleDetailBean> riddleDetail =
                mApiService.getRiddleDetail(authorization, classid, keyword, pagenum, pagesize);
        return riddleDetail;
    }


}
