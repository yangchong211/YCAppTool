package com.ns.yc.lifehelper.ui.other.myKnowledge.model;

import android.content.Context;

import com.ns.yc.lifehelper.api.constantApi.ConstantALiYunApi;
import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.other.myKnowledge.api.KnowledgeSearchHotApi;
import com.ns.yc.lifehelper.ui.other.myKnowledge.bean.SearchHotBean;

import rx.Observable;


/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

public class KnowledgeSearchHotModel {

    private static KnowledgeSearchHotModel hotSearchModel;
    private KnowledgeSearchHotApi mApiService;

    public KnowledgeSearchHotModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(ConstantALiYunApi.NEW_SEED)
                .create(KnowledgeSearchHotApi.class);
    }

    public static KnowledgeSearchHotModel getInstance(Context context){
        if(hotSearchModel == null) {
            hotSearchModel = new KnowledgeSearchHotModel(context);
        }
        return hotSearchModel;
    }

    public Observable<SearchHotBean> getHotTag() {
        Observable<SearchHotBean> hotTags = mApiService.getHotTags();
        return hotTags;
    }


}
