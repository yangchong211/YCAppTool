package com.ns.yc.lifehelper.ui.me.contract;


import com.ycbjie.library.base.mvp.BasePresenter;
import com.ycbjie.library.base.mvp.BaseView;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/3/21
 * 描    述：我的页面
 * 修订历史：
 *          v1.5 17年9月8日修改
 * ================================================
 */
public interface MeFragmentContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {

    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        void getRedHotMessageData();
    }


}
