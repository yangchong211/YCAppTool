package com.ycbjie.douban.api;

import com.ycbjie.library.api.ConstantALiYunApi;
import com.ycbjie.library.http.RetrofitWrapper;
import com.ycbjie.douban.bean.DouBookBean;
import com.ycbjie.douban.bean.DouBookDetailBean;

import io.reactivex.Observable;



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
