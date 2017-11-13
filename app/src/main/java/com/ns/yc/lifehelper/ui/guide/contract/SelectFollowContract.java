package com.ns.yc.lifehelper.ui.guide.contract;


import android.app.Activity;

import com.ns.yc.lifehelper.base.BasePresenter;
import com.ns.yc.lifehelper.base.BaseView;
import com.ns.yc.lifehelper.bean.SelectPoint;

import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2016/5/18
 * 描    述：关注点页面
 * 修订历史：
 * ================================================
 */
public interface SelectFollowContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        //刷新数据
        void refreshData(List<SelectPoint> list);
        //跳转首页
        void toMainActivity();
    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        //添加数据
        void addData(Activity activity);
        //跳转首页
        void goMainActivity();
        //添加数据到数据库
        void addSelectToRealm(Integer[] selectedIndices);
    }


}
