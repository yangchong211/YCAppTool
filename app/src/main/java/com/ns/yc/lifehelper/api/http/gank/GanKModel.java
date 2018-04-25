package com.ns.yc.lifehelper.api.http.gank;

import com.ns.yc.lifehelper.api.constantApi.ConstantGanKApi;
import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.other.gank.bean.CategoryResult;
import com.ns.yc.lifehelper.ui.other.gank.bean.SearchResult;

import rx.Observable;


public class GanKModel {

    private static GanKModel model;
    private GanKApi mApiService;

    public GanKModel() {
        mApiService = RetrofitWrapper
                .getInstance(ConstantGanKApi.GAN_K_IO_API)
                .create(GanKApi.class);
    }

    public static GanKModel getInstance(){
        if(model == null) {
            model = new GanKModel();
        }
        return model;
    }

    public Observable<CategoryResult> getCategoryDate(String tag, int start , int count) {
        Observable<CategoryResult> data = mApiService.getCategoryDate(tag, start, count);
        return data;
    }

    public Observable<CategoryResult> getRandomBeauties(int count) {
        Observable<CategoryResult> data = mApiService.getRandomBeauties(count);
        return data;
    }

    public Observable<SearchResult> getSearchResult(String key, int start , int count) {
        Observable<SearchResult> data = mApiService.getSearchResult(key, start, count);
        return data;
    }

}
