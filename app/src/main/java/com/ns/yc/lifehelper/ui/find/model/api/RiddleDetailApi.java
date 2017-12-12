package com.ns.yc.lifehelper.ui.find.model.api;

import com.ns.yc.lifehelper.ui.find.model.bean.RiddleDetailBean;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by PC on 2017/8/30.
 * 作者：PC
 */

public interface RiddleDetailApi {

    /**
     * 谜语查询接口
     * classid      分类ID
     * keyword      关键词 为空为随机获取
     * pagenum      当前页
     * pagesize     每页数据 最大为2
     */
    @GET("miyu/search")
    Observable<RiddleDetailBean> getRiddleDetail(@Header("Authorization") String token,
                                                  @Query("classid") String classid,
                                                  @Query("keyword") String keyword,
                                                  @Query("pagenum") String pagenum,
                                                  @Query("pagesize") String pagesize);

}
