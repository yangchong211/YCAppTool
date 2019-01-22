package com.ycbjie.douban.api;

import com.ycbjie.douban.bean.DouHotMovieBean;

import io.reactivex.Observable;
import retrofit2.http.GET;


public interface HotMovieApi {

    /**
     * 豆瓣热映电影，每日更新
     */
    @GET("v2/movie/in_theaters")
    Observable<DouHotMovieBean> getHotMovie();

}
