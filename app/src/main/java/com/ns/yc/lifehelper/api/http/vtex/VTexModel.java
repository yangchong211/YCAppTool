package com.ns.yc.lifehelper.api.http.vtex;

import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.other.vtex.model.bean.NodeBean;
import com.ns.yc.lifehelper.ui.other.vtex.model.bean.NodeListBean;
import com.ns.yc.lifehelper.ui.other.vtex.model.bean.RepliesListBean;

import java.util.List;

import rx.Observable;


public class VTexModel {

    private static VTexModel model;
    private VTexApi mApiService;

    private VTexModel() {
        mApiService = RetrofitWrapper
                .getInstance(VTexApi.HOST)
                .create(VTexApi.class);
    }

    public static VTexModel getInstance(){
        if(model == null) {
            model = new VTexModel();
        }
        return model;
    }

    public Observable<NodeBean> fetchNodeInfo(String name) {
        return mApiService.getNodeInfo(name);
    }

    public Observable<List<NodeListBean>> fetchTopicList(String name) {
        return mApiService.getTopicList(name);
    }

    public Observable<List<NodeListBean>> fetchTopicInfo(String id) {
        return mApiService.getTopicInfo(id);
    }

    public Observable<List<RepliesListBean>> fetchRepliesList(String id){
        return mApiService.getRepliesList(id);
    }




}
