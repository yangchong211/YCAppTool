package com.ycbjie.todo.contract;


import android.content.Intent;
import android.view.MenuItem;

import com.ycbjie.library.base.mvp.BasePresenter;
import com.ycbjie.library.base.mvp.BaseView;
import com.ycbjie.library.db.cache.CacheTaskDetailEntity;
import com.ycbjie.todo.ui.adapter.WorkPageAdapter;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：干货集中营首页桥梁
 * 修订历史：
 * ================================================
 */
public interface WorkDoContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        //设置适配器
        void setViewPagerAdapter(WorkPageAdapter mAdapter);
        //设置指示器位置
        void setViewPagerCurrentItem(int currIndex, boolean b);
        //获取当前指示器的位置
        int getCurrentViewPagerItem();
        //跳转页面
        void startActivityAndForResult(Intent intent, int code);
        //弹窗
        void showDialog(int position, CacheTaskDetailEntity entity);
        Intent getIntent();
        void finishActivity();
        void showAction(String message, String action, android.view.View.OnClickListener listener);
    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        //绑定view
        void bindView(WorkDoContract.View androidView);
        //悬浮按钮点击事件
        void setOnFabClick();
        //menu
        boolean onOptionsItemSelected(MenuItem item);
        //处理返回的逻辑
        void onActivityResult(int requestCode, int resultCode, Intent data);
        //点击事件
        void onListTaskItemClick(int position, CacheTaskDetailEntity entity);
        //长按点击事件
        void onListTaskItemLongClick(int position, CacheTaskDetailEntity entity);
        void dialogActionFlagTask(int position, CacheTaskDetailEntity entity);
        void dialogActionEditTask(int position, CacheTaskDetailEntity entity);
        void dialogActionDeleteTask(int position, CacheTaskDetailEntity entity);
        void dialogActionPutOffTask(int position, CacheTaskDetailEntity entity);
    }


}
