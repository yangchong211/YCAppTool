package com.ycbjie.douban.api;

import com.ycbjie.douban.bean.DouMovieDetailBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;



public interface DetailMovieApi {

    /**
     * 获取电影详情
     * @param id 电影bean里的id
     */
    @GET("v2/movie/subject/{id}")
    Observable<DouMovieDetailBean> getMovieDetail(@Path("id") String id);

}
