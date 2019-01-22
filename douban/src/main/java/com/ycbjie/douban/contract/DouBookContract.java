package com.ycbjie.douban.contract;


import com.ycbjie.library.base.mvp.BasePresenter;
import com.ycbjie.library.base.mvp.BaseView;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/12/1
 * 描    述：知乎评论
 * 修订历史：
 * ================================================
 */
public interface DouBookContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {

    }

    //Presenter控制器
    interface Presenter extends BasePresenter {

    }


}
