package com.ycbjie.todo.contract;


import com.ycbjie.library.base.mvp.BasePresenter;
import com.ycbjie.library.base.mvp.BaseView;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/10/21
 * 描    述：此版块训练dagger2+MVP
 * 修订历史：
 * ================================================
 */
public interface WorkNewContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        void bindView(WorkNewContract.View androidView);
    }


}
