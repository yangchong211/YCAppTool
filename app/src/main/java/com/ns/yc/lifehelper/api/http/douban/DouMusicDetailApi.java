package com.ns.yc.lifehelper.api.http.douban;

import com.ns.yc.lifehelper.ui.other.douban.douMusic.bean.DouMusicDetailBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by PC on 2017/8/22.
 * 作者：PC
 */

public interface DouMusicDetailApi {

    /**
     * 获取电影详情
     * @param id 电影bean里的id
     */
    @GET("/v2/music/{id}")
    Observable<DouMusicDetailBean> getMusicDetail(@Path("id") String id);

}
