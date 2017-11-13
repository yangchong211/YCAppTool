package com.ns.yc.lifehelper.ui.guide.contract;


import com.ns.yc.lifehelper.base.BasePresenter;
import com.ns.yc.lifehelper.base.BaseView;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：启动页
 * 修订历史：
 * ================================================
 */
public interface GuideContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        //跳转到主页面
        void toMainActivity();
        //展示图片
        void showGuideLogo(String logo);
    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        void goMainActivity();
    }


}
