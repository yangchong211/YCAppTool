package com.ns.yc.lifehelper.ui.other.myKnowledge.model;

import android.content.Context;

import com.ns.yc.lifehelper.api.constantApi.ConstantALiYunApi;
import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.other.myKnowledge.api.KnowledgeSearchAllApi;
import com.ns.yc.lifehelper.ui.other.myKnowledge.bean.SearchAllBean;

import rx.Observable;


/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

public class KnowledgeSearchAllModel {

    private static KnowledgeSearchAllModel allSearchModel;
    private KnowledgeSearchAllApi mApiService;

    public KnowledgeSearchAllModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(ConstantALiYunApi.NEW_SEED)
                .create(KnowledgeSearchAllApi.class);
    }

    public static KnowledgeSearchAllModel getInstance(Context context){
        if(allSearchModel == null) {
            allSearchModel = new KnowledgeSearchAllModel(context);
        }
        return allSearchModel;
    }

    public Observable<SearchAllBean> getAllSearch(String k , String t , String u) {
        Observable<SearchAllBean> search = mApiService.getSearch(k, t, u);
        return search;
    }


}
