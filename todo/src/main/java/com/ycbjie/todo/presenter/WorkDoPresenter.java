package com.ycbjie.todo.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.todo.R;
import com.ycbjie.todo.contract.WorkDoContract;
import com.ycbjie.library.db.realm.RealmWorkDoHelper;
import com.ycbjie.library.utils.time.DateUtils;
import com.ycbjie.todo.model.MainPageItem;
import com.ycbjie.library.db.cache.CacheTaskDetailEntity;
import com.ycbjie.todo.ui.activity.WorkNewActivity;
import com.ycbjie.todo.ui.activity.WorkSettingActivity;
import com.ycbjie.todo.ui.adapter.WorkPageAdapter;
import com.ycbjie.todo.ui.fragment.PageFragment;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.realm.RealmResults;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static android.app.Activity.RESULT_OK;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/10/14.
 * 描    述：此版块训练dagger2+MVP
 * 修订历史：
 * ================================================
 */
public class WorkDoPresenter implements WorkDoContract.Presenter {

    private WorkDoContract.View mView;
    private CompositeSubscription mSubscriptions;
    private Context mContext;
    private int lastPosition;

    @Inject
    WorkPageAdapter mAdapter;
    @Inject
    List<MainPageItem> mItems;
    @Inject
    RealmWorkDoHelper mDataDao;
    @Inject
    WorkDoPresenter(Context context) {
        mContext = context;
    }

    @Override
    public void bindView(WorkDoContract.View androidView) {
        this.mView = androidView;
        mSubscriptions = new CompositeSubscription();
    }


    @Override
    public void subscribe() {
        mView.setViewPagerAdapter(mAdapter);
        initVpItem();
        // 坑: mVp.getCurrentItem() 某些时候不能获得第一页和最后一页的Index
        for (int i = 0; i < Constant.DAY_OF_WEEK; i++) {
            ((PageFragment) mItems.get(i).getFragment()).clearTasks();
        }
        RealmResults<CacheTaskDetailEntity> allTask ;
        if (SPUtils.getInstance(Constant.SP_NAME).getBoolean(Constant.CONFIG_KEY.SHOW_WEEK_TASK)){
            allTask = mDataDao.findAllTaskOfThisWeekFromSunday();
        } else{
            allTask = mDataDao.findAllTask();
        }
        if (allTask!=null){
            for (CacheTaskDetailEntity t : allTask) {
                int day = t.getDayOfWeek();
                PageFragment fragment = (PageFragment) mItems.get(day - 1).getFragment();
                if (fragment!=null){
                    fragment.insertTask(t);
                }
            }
        }
    }


    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
        mItems = null;
        mDataDao.close();
    }


    private void initVpItem() {
        int currIndex;
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        currIndex = dayOfWeek - 1;
        Intent intent = mView.getIntent();
        if (intent != null) {
            currIndex = intent.getIntExtra(Constant.INTENT_EXTRA_SWITCH_TO_INDEX, currIndex);
        }
        mView.setViewPagerCurrentItem(currIndex, true);
    }



    /**
     * 悬浮按钮点击事件
     */
    @Override
    public void setOnFabClick() {
        int i = mView.getCurrentViewPagerItem();
        Intent intent = new Intent(mContext, WorkNewActivity.class);
        intent.putExtra(Constant.INTENT_EXTRA_DAY_OF_WEEK, i + 1);
        intent.putExtra(Constant.INTENT_EXTRA_MODE_OF_NEW_ACT, Constant.MODE_OF_NEW_ACT.MODE_CREATE);
        mView.startActivityAndForResult(intent, Constant.NEW_ACTIVITY_REQUEST_CODE);
    }


    /**
     * menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_settings) {
            Intent intent = new Intent(mContext, WorkSettingActivity.class);
            intent.putExtra(Constant.INTENT_EXTRA_SWITCH_TO_INDEX, mView.getCurrentViewPagerItem());
            mContext.startActivity(intent);
        }
        return true;
    }


    /**
     * 处理返回逻辑
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        Bundle bundle = data.getExtras();
        CacheTaskDetailEntity task = (CacheTaskDetailEntity) bundle.getSerializable(Constant.INTENT_BUNDLE_NEW_TASK_DETAIL);
        PageFragment fragment = (PageFragment) mItems.get(mView.getCurrentViewPagerItem()).getFragment();

        if (requestCode == Constant.NEW_ACTIVITY_REQUEST_CODE) {
            //插入数据
            fragment.insertTask(task);
            mDataDao.insertTask(task);
        } else if (requestCode == Constant.EDIT_ACTIVITY_REQUEST_CODE) {
            //编辑数据
            CacheTaskDetailEntity oldTask = fragment.deleteTask(lastPosition);
            if (oldTask!=null){
                fragment.insertTask(task);
                mDataDao.editTask(oldTask, task);
            }
        }
    }


    /**
     * 点击事件
     */
    @Override
    public void onListTaskItemClick(int position, CacheTaskDetailEntity entity) {
        toEditActivity(position, entity);
    }


    /**
     * 长按点击事件
     */
    @Override
    public void onListTaskItemLongClick(int position, CacheTaskDetailEntity entity) {
        mView.showDialog(position, entity);
    }


    /**
     * 跳转编辑页面
     * @param position              索引
     * @param entity                entry
     */
    private void toEditActivity(int position, CacheTaskDetailEntity entity) {
        Intent intent = new Intent(mContext, WorkNewActivity.class);
        intent.putExtra(Constant.INTENT_EXTRA_EDIT_TASK_DETAIL_ENTITY, entity.cloneObj());
        intent.putExtra(Constant.INTENT_EXTRA_MODE_OF_NEW_ACT, Constant.MODE_OF_NEW_ACT.MODE_EDIT);
        mView.startActivityAndForResult(intent, Constant.EDIT_ACTIVITY_REQUEST_CODE);
        lastPosition = position;
    }


    /**
     * 标记为已经完成
     */
    @Override
    public void dialogActionFlagTask(int position, CacheTaskDetailEntity entity) {
        int newState;
        if (entity.getState() == Constant.TaskState.DEFAULT) {
            newState = Constant.TaskState.FINISHED;
        } else {
            newState = Constant.TaskState.DEFAULT;
        }
        switchTaskState(position, entity, newState);
    }


    /**
     * 编辑笔记任务
     */
    @Override
    public void dialogActionEditTask(int position, CacheTaskDetailEntity entity) {
        toEditActivity(position, entity);
    }


    /**
     * 删除笔记任务
     */
    @Override
    public void dialogActionDeleteTask(int position, CacheTaskDetailEntity entity) {
        final Subscription subscription = deleteTaskWithDelay(position, entity);
        mView.showAction("即将删除", "撤销", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscription.unsubscribe();
            }
        });
    }

    /**
     * 任务推迟一天
     */
    @Override
    public void dialogActionPutOffTask(int position, CacheTaskDetailEntity entity) {
        final Subscription subscription = putOffTaskOneDayWithDelay(position, entity);
        mView.showAction("该任务即将推延一天", "撤销", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscription.unsubscribe();
            }
        });
    }

    private void switchTaskState(int position, CacheTaskDetailEntity entity, int newState) {
        PageFragment fragment = (PageFragment) mItems.get(mView.getCurrentViewPagerItem()).getFragment();
        mDataDao.switchTaksState(entity, newState);
        fragment.getAdapter().notifyItemChanged(position);
    }

    private Subscription deleteTaskWithDelay(final int position, final CacheTaskDetailEntity entity) {
        return Observable
                .just(1)
                .delay(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        deleteTask(position, entity);
                    }
                });

    }

    private void deleteTask(int position, CacheTaskDetailEntity entity) {
        PageFragment fragment = (PageFragment) mItems.get(mView.getCurrentViewPagerItem()).getFragment();
        RealmWorkDoHelper dao = RealmWorkDoHelper.getInstance();
        fragment.deleteTask(position);
        dao.deleteTask(entity);
    }

    private Subscription putOffTaskOneDayWithDelay(final int position, final CacheTaskDetailEntity entity) {
        return Observable
                .just(1)
                .delay(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        putOffTaskOneDay(position, entity);
                    }
                });
    }


    private void putOffTaskOneDay(int position, CacheTaskDetailEntity entity) {
        PageFragment fragment = (PageFragment) mItems.get(mView.getCurrentViewPagerItem()).getFragment();
        CacheTaskDetailEntity oldEntity = fragment.deleteTask(position);
        LogUtils.e("putOffTaskOneDay"+entity.equals(oldEntity));
        if (oldEntity!=null){
            CacheTaskDetailEntity newEntity = oldEntity.cloneObj();
            newEntity.setDayOfWeek(DateUtils.calNextDayDayOfWeek(oldEntity.getDayOfWeek()));
            ((PageFragment) mItems.get(newEntity.getDayOfWeek() - 1).getFragment()).insertTask(newEntity);
            mDataDao.deleteTask(oldEntity);
            mDataDao.insertTask(newEntity);
        }
    }


}
