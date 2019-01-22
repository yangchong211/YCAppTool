package com.ycbjie.zhihu.web.contract;


import android.app.Activity;

import com.ycbjie.library.base.mvp.BasePresenter;
import com.ycbjie.library.base.mvp.BaseView;
import com.ycbjie.zhihu.model.ZhihuDetailBean;
import com.ycbjie.zhihu.model.ZhiHuDetailExtraBean;

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
