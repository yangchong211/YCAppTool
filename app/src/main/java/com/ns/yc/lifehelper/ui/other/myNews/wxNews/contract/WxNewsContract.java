package com.ns.yc.lifehelper.ui.other.myNews.wxNews.contract;


import com.ns.yc.lifehelper.base.BasePresenter;
import com.ns.yc.lifehelper.base.BaseView;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/4/30
 * 描    述：微信新闻
 * 修订历史：
 *          v1.3 修改于2017年8月6日
 * ================================================
 */
public interface WxNewsContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {

    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        void readCacheChannel();
    }


}
