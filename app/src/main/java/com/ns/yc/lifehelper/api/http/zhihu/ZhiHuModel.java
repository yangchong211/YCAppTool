package com.ns.yc.lifehelper.api.http.zhihu;

import com.ns.yc.lifehelper.api.constantApi.ConstantALiYunApi;
import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuCommentBean;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuDailyListBean;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuDailyBeforeListBean;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuDetailExtraBean;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuHotBean;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuSectionBean;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuSectionChildBean;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuThemeBean;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhihuDetailBean;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuThemeChildBean;

import rx.Observable;


/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

public class ZhiHuModel {

    private static ZhiHuModel model;
    private ZhiHuApi mApiService;

    private ZhiHuModel() {
        mApiService = RetrofitWrapper
                .getInstance(ConstantALiYunApi.ZHI_HU)
                .create(ZhiHuApi.class);
    }

    public static ZhiHuModel getInstance(){
        if(model == null) {
            model = new ZhiHuModel();
        }
        return model;
    }

    /**
     * 最新日报
     */
    public Observable<ZhiHuDailyListBean> getDailyList() {
        return mApiService.getDailyList();
    }

    /**
     * 主题日报
     */
    public Observable<ZhiHuThemeBean> getThemeList() {
        return mApiService.getThemeList();
    }

    /**
     * 专栏日报
     */
    public Observable<ZhiHuSectionBean> getSectionList() {
        return mApiService.getSectionList();
    }

    /**
     * 热门日报
     */
    public Observable<ZhiHuHotBean> getHotList() {
        return mApiService.getHotList();
    }

    /**
     * 日报详情
     */
    public Observable<ZhihuDetailBean> getDetailInfo(int id) {
        return mApiService.getDetailInfo(id);
    }

    /**
     * 日报的额外信息
     */
    public Observable<ZhiHuDetailExtraBean> getDetailExtraInfo(int id) {
        return mApiService.getDetailExtraInfo(id);
    }

    /**
     * 日报的长评论
     */
    public Observable<ZhiHuCommentBean> getLongCommentInfo(int id) {
        return mApiService.getLongCommentInfo(id);
    }

    /**
     * 日报的短评论
     */
    public Observable<ZhiHuCommentBean> getShortCommentInfo(int id) {
        return mApiService.getShortCommentInfo(id);
    }

    /**
     * 主题日报详情
     */
    public Observable<ZhiHuThemeChildBean> getThemeChildList(int id) {
        return mApiService.getThemeChildList(id);
    }

    /**
     * 专栏日报详情
     */
    public Observable<ZhiHuSectionChildBean> getSectionChildList(int id) {
        return mApiService.getSectionChildList(id);
    }

    /**
     * 往期日报
     */
    public Observable<ZhiHuDailyBeforeListBean> fetchDailyBeforeListInfo(String date) {
        return mApiService.getDailyBeforeList(date);
    }


}
