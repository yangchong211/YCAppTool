package com.ns.yc.lifehelper.ui.main.contract;


import android.app.Activity;

import com.ns.yc.lifehelper.base.BasePresenter;
import com.ns.yc.lifehelper.base.BaseView;
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

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        void showContent(ZhihuDetailBean zhihuDetailBean);
        void showExtraInfo(ZhiHuDetailExtraBean zhiHuDetailExtraBean);
    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        void bindView(Activity activity);
        void getDetailData(int id);
        void getExtraData(int id);
        void deleteLikeData();
        void insertLikeData();
    }


}
