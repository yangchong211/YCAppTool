package com.ycbjie.gank.api;

import com.ycbjie.gank.bean.bean.GanKEveryDay;
import com.ycbjie.gank.bean.bean.GanKIoDataBean;
import com.ycbjie.library.http.RetrofitWrapper;
import com.ycbjie.gank.bean.bean.CategoryResult;
import com.ycbjie.gank.bean.bean.SearchResult;
import io.reactivex.Observable;


public class GanKModel {

    private static final String GAN_K_IO_API = "http://gank.io/api/";
    private static GanKModel model;
    private GanKApi mApiService;

    public GanKModel() {
        mApiService = RetrofitWrapper
                .getInstance(GAN_K_IO_API)
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
    public Observable<GanKIoDataBean> getGanKData(String type , int page , int pre_page) {
        Observable<GanKIoDataBean> gankIoData = mApiService.getGanKIoData(type, page, pre_page);
        return gankIoData;
    }

    public Observable<GanKEveryDay> getGanKIoDay(String year , String month , String day) {
        Observable<GanKEveryDay> gankIoDay = mApiService.getGanKIoDay(year, month, day);
        return gankIoDay;
    }


}
