package com.ns.yc.lifehelper.api.http.gank;


import com.ns.yc.lifehelper.ui.other.gank.bean.CategoryResult;
import com.ns.yc.lifehelper.ui.other.gank.bean.SearchResult;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;


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




}
