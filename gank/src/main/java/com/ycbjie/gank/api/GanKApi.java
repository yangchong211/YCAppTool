package com.ycbjie.gank.api;


import com.ycbjie.gank.bean.bean.CategoryResult;
import com.ycbjie.gank.bean.bean.GanKEveryDay;
import com.ycbjie.gank.bean.bean.GanKIoDataBean;
import com.ycbjie.gank.bean.bean.SearchResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface GanKApi {

    @GET("data/{category}/{number}/{page}")
    Observable<CategoryResult> getCategoryDate(@Path("category") String category,
                                               @Path("number") int number,
                                               @Path("page") int page);

    @GET("random/data/福利/{number}")
    Observable<CategoryResult> getRandomBeauties(@Path("number") int number);


    @GET("search/query/{key}/category/all/count/{count}/page/{page}")
    Observable<SearchResult> getSearchResult(@Path("key") String key,
                                             @Path("count") int count,
                                             @Path("page") int page);


    /**
     * 分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
     * 数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
     * 请求个数： 数字，大于0
     * 第几页：数字，大于0
     * eg: http://gank.io/api/data/Android/10/1
     */
    @GET("data/{type}/{pre_page}/{page}")
    Observable<GanKIoDataBean> getGanKIoData(@Path("type") String id,
                                             @Path("page") int page,
                                             @Path("pre_page") int pre_page);

    /**
     * 每日数据： http://gank.io/api/day/年/月/日
     * eg:http://gank.io/api/day/2015/08/06
     */
    @GET("day/{year}/{month}/{day}")
    Observable<GanKEveryDay> getGanKIoDay(@Path("year") String year,
                                          @Path("month") String month,
                                          @Path("day") String day);

}
