package com.ns.yc.lifehelper.ui.other.workDo.contract;


import android.content.Intent;

import com.ns.yc.lifehelper.base.mvp.BasePresenter;
import com.ns.yc.lifehelper.base.mvp.BaseView;
import com.ns.yc.lifehelper.ui.other.workDo.model.TaskDetailEntity;

import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：干货集中营首页桥梁
 * 修订历史：
 * ================================================
 */
public interface SearchListContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        Intent intent();
        void hideNoResults();
        void updateToolbarTitle(String title);
        void showNoResults();
        void updateList(List<TaskDetailEntity> list);
        void finishActivity();
        void startActivityAndForResult(Intent intent, int code);
    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        void bindView(SearchListContract.View homeView);
        void onItemClick(int position, TaskDetailEntity entity);
    }


}
