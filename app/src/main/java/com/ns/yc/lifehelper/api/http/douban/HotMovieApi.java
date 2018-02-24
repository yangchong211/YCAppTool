package com.ns.yc.lifehelper.api.http.douban;

import com.ns.yc.lifehelper.ui.other.douban.douMovie.bean.DouHotMovieBean;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by PC on 2017/8/22.
 * 作者：PC
 */

public interface HotMovieApi {

    /**
     * 豆瓣热映电影，每日更新
     */
    @GET("v2/movie/in_theaters")
    Observable<DouHotMovieBean> getHotMovie();

}
