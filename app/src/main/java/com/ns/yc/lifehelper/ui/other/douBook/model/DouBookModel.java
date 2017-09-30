package com.ns.yc.lifehelper.ui.other.douBook.model;

import android.content.Context;

import com.ns.yc.lifehelper.api.ConstantALiYunApi;
import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.other.douBook.api.DouBookApi;
import com.ns.yc.lifehelper.ui.other.douBook.bean.DouBookBean;

import rx.Observable;


/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

public class DouBookModel {

    private static DouBookModel bookModel;
    private DouBookApi mApiService;

    public DouBookModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(ConstantALiYunApi.API_DOUBAN)
                .create(DouBookApi.class);
    }

    public static DouBookModel getInstance(Context context){
        if(bookModel == null) {
            bookModel = new DouBookModel(context);
        }
        return bookModel;
    }

    public Observable<DouBookBean> getHotMovie(String tag, int start , int count) {
        Observable<DouBookBean> book = mApiService.getBook(tag, start, count);
        return book;
    }


}
