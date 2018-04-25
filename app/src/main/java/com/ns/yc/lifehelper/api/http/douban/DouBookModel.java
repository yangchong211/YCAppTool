package com.ns.yc.lifehelper.api.http.douban;

import com.ns.yc.lifehelper.api.constantApi.ConstantALiYunApi;
import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.other.douban.douBook.model.DouBookBean;
import com.ns.yc.lifehelper.ui.other.douban.douBook.model.DouBookDetailBean;

import rx.Observable;


/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

public class DouBookModel{

    private static DouBookModel bookModel;
    private DouBookApi mApiService;

    private DouBookModel() {
        mApiService = RetrofitWrapper
                .getInstance(ConstantALiYunApi.API_DOUBAN)
                .create(DouBookApi.class);
    }

    public static DouBookModel getInstance(){
        if(bookModel == null) {
            bookModel = new DouBookModel();
        }
        return bookModel;
    }

    public Observable<DouBookBean> getHotMovie(String tag, int start , int count) {
        Observable<DouBookBean> book = mApiService.getBook(tag, start, count);
        return book;
    }

    public Observable<DouBookDetailBean> getHotMovie(String id) {
        Observable<DouBookDetailBean> bookDetail = mApiService.getBookDetail(id);
        return bookDetail;
    }

}
