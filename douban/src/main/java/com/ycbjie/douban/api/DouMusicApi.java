package com.ycbjie.douban.api;

import com.ycbjie.douban.bean.DouMusicBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface DouMusicApi {

    /**
     * 根据tag获取音乐
     * @param tag   搜索关键字
     * @param count 一次请求的数目 最多100
     *              https://api.douban.com/v2/music/search?tag=经典老歌&start=0&count=30
     */

    @GET("/v2/music/search")
    Observable<DouMusicBean> getMusic(@Query("tag") String tag,
                                      @Query("start") int start,
                                      @Query("count") int count);

}
