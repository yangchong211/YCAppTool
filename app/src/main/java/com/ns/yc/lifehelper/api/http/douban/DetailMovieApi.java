package com.ns.yc.lifehelper.api.http.douban;

import com.ns.yc.lifehelper.ui.other.douban.douMovie.bean.DouMovieDetailBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by PC on 2017/8/22.
 * 作者：PC
 */

public interface DetailMovieApi {

    /**
     * 获取电影详情
     * @param id 电影bean里的id
     */
    @GET("v2/movie/subject/{id}")
    Observable<DouMovieDetailBean> getMovieDetail(@Path("id") String id);

}
