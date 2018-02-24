package com.ns.yc.lifehelper.ui.other.toDo.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constant.Constant;
import com.ns.yc.lifehelper.base.app.BaseApplication;
import com.ns.yc.lifehelper.ui.other.toDo.bean.MainPageItem;
import com.ns.yc.lifehelper.ui.other.toDo.bean.TaskDetailEntity;
import com.ns.yc.lifehelper.ui.other.toDo.contract.ToDoTimerContract;
import com.ns.yc.lifehelper.ui.other.toDo.view.activity.ToDoAddActivity;
import com.ns.yc.lifehelper.ui.other.toDo.view.activity.ToDoSettingActivity;
import com.ns.yc.lifehelper.ui.other.toDo.view.activity.ToDoTimerFragment;

import java.util.List;

import io.realm.Realm;
import rx.subscriptions.CompositeSubscription;

import static android.app.Activity.RESULT_OK;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：时光日志页面
 * 修订历史：
 * ================================================
 */
public class ToDoTimerPresenter implements ToDoTimerContract.Presenter {

    private ToDoTimerContract.View mView;
    private CompositeSubscription mSubscriptions;
    private Realm realm;
    private Context activity;
    private List<MainPageItem> mItems;


    public ToDoTimerPresenter(ToDoTimerContract.View androidView) {
        this.mView = androidView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        initRealm();
        activity = mView.getActivity();
    }

    private void initRealm() {
        if(realm==null){
            realm = BaseApplication.getInstance().getRealmHelper();
        }
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    /**
     * 添加笔记
     */
    @Override
    public void addNote() {
        int i = mView.getCurrentViewPagerItem();
        String weekDate = "";
        switch (i){
            case 0:
                weekDate = "周一";
                break;
            case 1:
                weekDate = "周二";
                break;
            case 2:
                weekDate = "周三";
                break;
            case 3:
                weekDate = "周四";
                break;
            case 4:
                weekDate = "周五";
                break;
            case 5:
                weekDate = "周六";
                break;
            case 6:
                weekDate = "周日";
                break;
        }
        Intent intent = new Intent(activity, ToDoAddActivity.class);
        intent.putExtra(Constant.DAY_OF_WEEK, weekDate);
        intent.putExtra(Constant.ADD_NEW_OLD, Constant.NOTE_TYPE.add_new);
        mView.startActivityAndForResult(intent,100);
    }

    /**
     * 设置menu点击
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(activity, ToDoSettingActivity.class);
                activity.startActivity(intent);
                mView.finishActivity();
                break;
        }
        return true;
    }

    /**
     * 返回处理
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        Bundle bundle = data.getExtras();
        TaskDetailEntity task = (TaskDetailEntity) bundle.getSerializable("");
        ToDoTimerFragment fragment = (ToDoTimerFragment) mItems.get(mView.getCurrentViewPagerItem()).getFragment();

        /*if (requestCode == 0) {
            fragment.insertTask(task);
            mDataDao.insertTask(task);
        } else if (requestCode == 1) {
            TaskDetailEntity oldTask = fragment.deleteTask(mLastClickedItemPosition);
            fragment.insertTask(task);
            mDataDao.editTask(oldTask, task);
        }*/
    }
}
