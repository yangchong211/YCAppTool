package com.ns.yc.lifehelper.ui.find.model.api;

import com.ns.yc.lifehelper.ui.find.model.bean.GanKNicePicBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by PC on 2017/8/30.
 * 作者：PC
 */

public interface GanKNicePicApi {

    /**
     * 谜语查询接口
     * size      访问数据条数
     * page      页码
     */
    @GET("{size}/{page}")
    Observable<GanKNicePicBean> getGanKNicePic(@Path("size") String size,
                                                @Path("page") String page);

}
