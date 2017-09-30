package com.ns.yc.lifehelper.ui.other.douBook.model;

import android.content.Context;

import com.ns.yc.lifehelper.api.ConstantALiYunApi;
import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.other.douBook.api.DouBookDetailApi;
import com.ns.yc.lifehelper.ui.other.douBook.bean.DouBookDetailBean;

import rx.Observable;


/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

public class DouBookDetailModel {

    private static DouBookDetailModel bookModel;
    private DouBookDetailApi mApiService;

    public DouBookDetailModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(ConstantALiYunApi.API_DOUBAN)
                .create(DouBookDetailApi.class);
    }

    public static DouBookDetailModel getInstance(Context context){
        if(bookModel == null) {
            bookModel = new DouBookDetailModel(context);
        }
        return bookModel;
    }

    public Observable<DouBookDetailBean> getHotMovie(String id) {
        Observable<DouBookDetailBean> bookDetail = mApiService.getBookDetail(id);
        return bookDetail;
    }


}
