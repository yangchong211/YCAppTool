package com.ns.yc.lifehelper.ui.other.myKnowledge.api;

import com.ns.yc.lifehelper.ui.other.myKnowledge.bean.GanKEveryDay;
import com.ns.yc.lifehelper.ui.other.myKnowledge.bean.GanKIoDataBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by PC on 2017/8/22.
 * 作者：PC
 */

public interface GanKIoOtherApi {

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
