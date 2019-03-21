package com.ycbjie.zhihu.api;

import com.ycbjie.zhihu.model.ZhiHuCommentBean;
import com.ycbjie.zhihu.model.ZhiHuDailyBeforeListBean;
import com.ycbjie.zhihu.model.ZhiHuDailyListBean;
import com.ycbjie.zhihu.model.ZhiHuDetailExtraBean;
import com.ycbjie.zhihu.model.ZhiHuHotBean;
import com.ycbjie.zhihu.model.ZhiHuSectionBean;
import com.ycbjie.zhihu.model.ZhiHuSectionChildBean;
import com.ycbjie.zhihu.model.ZhiHuThemeBean;
import com.ycbjie.zhihu.model.ZhiHuThemeChildBean;
import com.ycbjie.zhihu.model.ZhihuDetailBean;

import rx.Observable;



public class ZhiHuModel {

    private static ZhiHuModel model;
    private ZhiHuApi mApiService;
    /**
     * 知乎日报
     */
    private static final String ZHI_HU = "http://news-at.zhihu.com/api/4/";

    private ZhiHuModel() {
        mApiService = RetrofitWrapper
                .getInstance(ZHI_HU)
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
