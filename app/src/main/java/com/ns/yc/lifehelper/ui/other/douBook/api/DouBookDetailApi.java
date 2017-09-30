package com.ns.yc.lifehelper.ui.other.douBook.api;

import com.ns.yc.lifehelper.ui.other.douBook.bean.DouBookDetailBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by PC on 2017/8/22.
 * 作者：PC
 */

public interface DouBookDetailApi {

    @GET("v2/book/{id}")
    Observable<DouBookDetailBean> getBookDetail(@Path("id") String id);

}
