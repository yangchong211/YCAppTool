package com.ns.yc.lifehelper.api.http.douban;

import com.ns.yc.lifehelper.ui.other.douban.douMovie.bean.DouHotMovieBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by PC on 2017/8/22.
 * 作者：PC
 */

public interface TopMovieApi {


    /**
     * 获取豆瓣电影top250
     *
     * @param start 从多少开始，如从"0"开始
     * @param count 一次请求的数目，如"10"条，最多100
     */
    @GET("v2/movie/top250")
    Observable<DouHotMovieBean> getMovieTop250(@Query("start") int start, @Query("count") int count);

}
