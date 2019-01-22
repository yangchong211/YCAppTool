package com.ycbjie.douban.api;

import com.ycbjie.douban.bean.DouMusicDetailBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface DouMusicDetailApi {

    /**
     * 获取电影详情
     * @param id 电影bean里的id
     */
    @GET("/v2/music/{id}")
    Observable<DouMusicDetailBean> getMusicDetail(@Path("id") String id);

}
