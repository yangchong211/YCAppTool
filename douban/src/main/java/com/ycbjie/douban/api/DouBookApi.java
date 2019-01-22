package com.ycbjie.douban.api;

import com.ycbjie.douban.bean.DouBookBean;
import com.ycbjie.douban.bean.DouBookDetailBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


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
