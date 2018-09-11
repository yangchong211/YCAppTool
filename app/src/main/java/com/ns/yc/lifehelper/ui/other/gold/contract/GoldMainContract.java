package com.ns.yc.lifehelper.ui.other.gold.contract;


import com.ns.yc.lifehelper.base.mvp.BasePresenter;
import com.ns.yc.lifehelper.base.mvp.BaseView;
import com.ns.yc.lifehelper.ui.other.gold.model.GoldManagerBean;
import com.ns.yc.lifehelper.ui.other.gold.model.GoldManagerItemBean;

import io.realm.RealmList;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：
 * 修订历史：
 * ================================================
 */
public interface GoldMainContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        void updateTab(RealmList<GoldManagerItemBean> mList);
        void jumpToManager(GoldManagerBean goldManagerList);
    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        void initManagerList();
        void initRealm();
        void setManagerList();
    }


}
