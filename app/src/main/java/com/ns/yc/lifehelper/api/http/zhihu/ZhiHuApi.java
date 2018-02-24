package com.ns.yc.lifehelper.api.http.zhihu;

import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuCommentBean;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuDailyListBean;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuDailyBeforeListBean;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhihuDetailBean;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuDetailExtraBean;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuHotBean;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuSectionBean;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuSectionChildBean;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuThemeBean;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuThemeChildBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;


public interface ZhiHuApi {

    /**
     * 最新日报
     */
    @GET("news/latest")
    Observable<ZhiHuDailyListBean> getDailyList();

    /**
     * 主题日报
     */
    @GET("themes")
    Observable<ZhiHuThemeBean> getThemeList();


    /**
     * 专栏日报
     */
    @GET("sections")
    Observable<ZhiHuSectionBean> getSectionList();

    /**
     * 热门日报
     */
    @GET("news/hot")
    Observable<ZhiHuHotBean> getHotList();


    /**
     * 日报详情
     */
    @GET("news/{id}")
    Observable<ZhihuDetailBean> getDetailInfo(@Path("id") int id);

    /**
     * 日报的额外信息
     */
    @GET("story-extra/{id}")
    Observable<ZhiHuDetailExtraBean> getDetailExtraInfo(@Path("id") int id);

    /**
     * 日报的长评论
     */
    @GET("story/{id}/long-comments")
    Observable<ZhiHuCommentBean> getLongCommentInfo(@Path("id") int id);

    /**
     * 日报的短评论
     */
    @GET("story/{id}/short-comments")
    Observable<ZhiHuCommentBean> getShortCommentInfo(@Path("id") int id);

    /**
     * 主题日报详情List
     */
    @GET("theme/{id}")
    Observable<ZhiHuThemeChildBean> getThemeChildList(@Path("id") int id);

    /**
     * 专栏日报详情
     */
    @GET("section/{id}")
    Observable<ZhiHuSectionChildBean> getSectionChildList(@Path("id") int id);

    /**
     * 往期日报
     */
    @GET("news/before/{date}")
    Observable<ZhiHuDailyBeforeListBean> getDailyBeforeList(@Path("date") String date);

}
