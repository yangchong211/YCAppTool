package com.ycbjie.douban.api;

import com.ycbjie.douban.bean.DouBookBean;
import com.ycbjie.douban.bean.DouBookDetailBean;
import com.ycbjie.douban.bean.DouHotMovieBean;
import com.ycbjie.douban.bean.DouMovieDetailBean;
import com.ycbjie.douban.bean.DouMusicBean;
import com.ycbjie.douban.bean.DouMusicDetailBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DouBanApi {

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




    /**
     * 获取电影详情
     * @param id 电影bean里的id
     */
    @GET("v2/movie/subject/{id}")
    Observable<DouMovieDetailBean> getMovieDetail(@Path("id") String id);


    /**
     * 获取电影详情
     * @param id 电影bean里的id
     */
    @GET("/v2/music/{id}")
    Observable<DouMusicDetailBean> getMusicDetail(@Path("id") String id);

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


    /**
     * 豆瓣热映电影，每日更新
     */
    @GET("v2/movie/in_theaters")
    Observable<DouHotMovieBean> getHotMovie();


    /**
     * 获取豆瓣电影top250
     *
     * @param start 从多少开始，如从"0"开始
     * @param count 一次请求的数目，如"10"条，最多100
     */
    @GET("v2/movie/top250")
    Observable<DouHotMovieBean> getMovieTop250(@Query("start") int start, @Query("count") int count);



}
