package com.ns.yc.lifehelper.ui.webView.contract;


import android.app.Activity;

import com.ns.yc.lifehelper.base.mvp.BasePresenter;
import com.ns.yc.lifehelper.base.mvp.BaseView;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhihuDetailBean;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuDetailExtraBean;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/30V
 * 描    述：可动画的详情页面，用腾讯x5库的webView
 * 修订历史：
 * ================================================
 */
public interface WebViewAnimContract {

    interface View extends BaseView {
        void showContent(ZhihuDetailBean zhihuDetailBean);
        void showExtraInfo(ZhiHuDetailExtraBean zhiHuDetailExtraBean);
    }

    interface Presenter extends BasePresenter {
        void bindView(Activity activity);
        void getDetailData(int id);
        void getExtraData(int id);
        void deleteLikeData();
        void insertLikeData();
    }


}
