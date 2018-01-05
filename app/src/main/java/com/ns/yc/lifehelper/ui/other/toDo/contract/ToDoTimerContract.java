package com.ns.yc.lifehelper.ui.other.toDo.contract;


import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.ns.yc.lifehelper.base.mvp1.BasePresenter;
import com.ns.yc.lifehelper.base.mvp1.BaseView;
import com.ns.yc.lifehelper.ui.other.toDo.bean.MainPageItem;

import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：时光日志页面
 * 修订历史：
 * ================================================
 */
public interface ToDoTimerContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        //获取指示器所在位置[周几]
        int getCurrentViewPagerItem();
        //获取上下文
        Context getActivity();
        //跳转
        void startActivityAndForResult(Intent intent, int code);
        //关闭Activity
        void finishActivity();
        //获取item
        List<MainPageItem> getPageItem();
    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        //添加笔记
        void addNote();
        //设置menu点击
        boolean onOptionsItemSelected(MenuItem item);
        //
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }


}
