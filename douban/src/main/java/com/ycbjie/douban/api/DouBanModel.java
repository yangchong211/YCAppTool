package com.ycbjie.douban.api;

import com.ycbjie.douban.bean.DouBookBean;
import com.ycbjie.douban.bean.DouBookDetailBean;
import com.ycbjie.douban.bean.DouHotMovieBean;
import com.ycbjie.douban.bean.DouMovieDetailBean;
import com.ycbjie.douban.bean.DouMusicBean;
import com.ycbjie.douban.bean.DouMusicDetailBean;
import com.ycbjie.library.http.RetrofitWrapper;

import io.reactivex.Observable;


public class DouBanModel {

    private static DouBanModel model;
    private DouBanApi mApiService;

    /**
     * 豆瓣接口
     * GET  https://api.douban.com/v2/movie/in_theaters     热映榜
     * GET  https://api.douban.com/v2/movie/top250          Top250电影评分
     * GET  https://api.douban.com/v2/movie/subject/:id     电影条目详情
     * GET  https://api.douban.com/v2/book/search           豆瓣搜索图书
     * GET  https://api.douban.com/v2/book/:id              获取图书信息
     */
    public static final String API_DOUBAN = "Https://api.douban.com/";

    private DouBanModel() {
        mApiService = RetrofitWrapper
                .getInstance(API_DOUBAN)
                .create(DouBanApi.class);
    }

    public static DouBanModel getInstance(){
        if(model == null) {
            model = new DouBanModel();
        }
        return model;
    }

    public Observable<DouBookBean> getBook(String tag, int start , int count) {
        Observable<DouBookBean> book = mApiService.getBook(tag, start, count);
        return book;
    }

    public Observable<DouBookDetailBean> getBookDetail(String id) {
        Observable<DouBookDetailBean> bookDetail = mApiService.getBookDetail(id);
        return bookDetail;
    }

    public Observable<DouMovieDetailBean> getMovieDetail(String id) {
        Observable<DouMovieDetailBean> detailMovie = mApiService.getMovieDetail(id);
        return detailMovie;
    }


    public Observable<DouMusicBean> getMusic(String tag, int start , int count) {
        Observable<DouMusicBean> book = mApiService.getMusic(tag, start, count);
        return book;
    }

    public Observable<DouMusicDetailBean> getMusicDetail(String id) {
        Observable<DouMusicDetailBean> musicDetail = mApiService.getMusicDetail(id);
        return musicDetail;
    }


    public Observable<DouHotMovieBean> getHotMovie() {
        Observable<DouHotMovieBean> hotMovie = mApiService.getHotMovie();
        return hotMovie;
    }

    public Observable<DouHotMovieBean> getTopMovie(int start, int count) {
        Observable<DouHotMovieBean> movieTop250 = mApiService.getMovieTop250(start, count);
        return movieTop250;
    }


}
