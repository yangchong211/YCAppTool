package com.ns.yc.lifehelper.api.http.douban;

import com.ns.yc.lifehelper.ui.other.douban.douBook.model.DouBookBean;
import com.ns.yc.lifehelper.ui.other.douban.douBook.model.DouBookDetailBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by PC on 2017/8/22.
 * 作者：PC
 */

public interface DouBookApi {

    /**
     * 根据tag获取图书
     * @param tag   搜索关键字
     * @param count 一次请求的数目 最多100
     *              https://api.douban.com/v2/book/search?tag=文学&start=0&count=30
     */
    @GET("v2/book/search")
    Observable<DouBookBean> getBook(@Query("tag") String tag,
                                    @Query("start") int start,
                                    @Query("count") int count);


    @GET("v2/book/{id}")
    Observable<DouBookDetailBean> getBookDetail(@Path("id") String id);


}
