package com.ns.yc.lifehelper.ui.other.imTalk.contract;


import android.support.v7.app.AppCompatActivity;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.ns.yc.lifehelper.base.mvp1.BasePresenter;
import com.ns.yc.lifehelper.base.mvp1.BaseView;

import java.util.ArrayList;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：
 * 修订历史：
 * ================================================
 */
public interface ImTalkContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        void onDisconnected(int error);
    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        ArrayList<CustomTabEntity> getTabEntity();
        void bindView(AppCompatActivity activity);
        void addConnectionListener();
    }


}
