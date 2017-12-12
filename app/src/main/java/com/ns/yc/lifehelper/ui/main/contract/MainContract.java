package com.ns.yc.lifehelper.ui.main.contract;


import android.app.Activity;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.ns.yc.lifehelper.base.BasePresenter;
import com.ns.yc.lifehelper.base.BaseView;

import java.util.ArrayList;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/3/18
 * 描    述：Main主页面
 * 修订历史：
 * ================================================
 */
public interface MainContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {

    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        void bindView(Activity activity);
        ArrayList<CustomTabEntity> getTabEntity();
        void getUpdate();
    }


}
