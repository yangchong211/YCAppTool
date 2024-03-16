package com.yc.appcontextlib.parallel;

import android.os.Looper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: 杨充
 * @email : yangchong211@163.com
 * @time : 2019/03/15
 * @desc : task分发
 * @revise :
 * GitHub ：https://github.com/yangchong211/YCEfficient
 */
public final class ParallelTaskDispatcher {

    /**
     * 所有任务需要等待的时间
     */
    private static final int WAITING_TIME = 10000;
    /**
     * 存放每个Task  （key= Class < ? extends AppStartTask>）
     */
    private final HashMap<Class<? extends AbsParallelTask>, AbsParallelTask> mTaskHashMap;
    /**
     * 每个Task的孩子 （key= Class < ? extends AppStartTask>）
     */
    private final HashMap<Class<? extends AbsParallelTask>, List<Class<? extends AbsParallelTask>>> mTaskChildHashMap;
    /**
     * 通过Add添加进来的所有任务
     */
    private final List<AbsParallelTask> mStartTaskList;
    /**
     * 拓扑排序后的所有任务
     */
    private List<AbsParallelTask> mSortTaskList;
    /**
     * 拓扑排序后的主线程的任务
     */
    private final List<AbsParallelTask> mSortMainThreadTaskList;
    /**
     * 拓扑排序后的子线程的任务
     */
    private final List<AbsParallelTask> mSortThreadPoolTaskList;
    /**
     * 需要等待的任务总数，用于阻塞
     */
    private CountDownLatch mCountDownLatch;
    /**
     * 需要等待的任务总数，用于CountDownLatch
     */
    private final AtomicInteger mNeedWaitCount;
    /**
     * 所有的任务开始时间，结束时间
     */
    private long mStartTime, mFinishTime;
    /**
     * 所有阻塞任务的总超时时间
     */
    private long mAllTaskWaitTimeOut;
    /**
     * 是否展示日志log
     */
    private boolean isShowLog;

    public static synchronized ParallelTaskDispatcher create() {
        //使用单例模式
        return new ParallelTaskDispatcher();
    }

    private ParallelTaskDispatcher() {
        mTaskHashMap = new HashMap<>();
        mTaskChildHashMap = new HashMap<>();
        mStartTaskList = new ArrayList<>();
        mNeedWaitCount = new AtomicInteger();
        mSortMainThreadTaskList = new ArrayList<>();
        mSortThreadPoolTaskList = new ArrayList<>();
    }

    public ParallelTaskDispatcher setAllTaskWaitTimeOut(long allTaskWaitTimeOut) {
        mAllTaskWaitTimeOut = allTaskWaitTimeOut;
        return this;
    }

    public ParallelTaskDispatcher setShowLog(boolean showLog) {
        isShowLog = showLog;
        return this;
    }

    public ParallelTaskDispatcher addAppStartTask(AbsParallelTask appStartTask) {
        if (appStartTask == null) {
            throw new RuntimeException("addAppStartTask() 传入的appStartTask为null");
        }
        mStartTaskList.add(appStartTask);
        if (ifNeedWait(appStartTask)) {
            mNeedWaitCount.getAndIncrement();
        }
        return this;
    }

    public ParallelTaskDispatcher start() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new RuntimeException("start方法必须在主线程调用");
        }
        mStartTime = System.currentTimeMillis();
        //拓扑排序，拿到排好序之后的任务队列
        mSortTaskList = ParallelTaskUtils.getSortResult(mStartTaskList, mTaskHashMap, mTaskChildHashMap);
        initRealSortTask();
        printSortTask();
        mCountDownLatch = new CountDownLatch(mNeedWaitCount.get());
        dispatchAppStartTask();
        return this;
    }

    /**
     * 分别处理主线程和子线程的任务
     */
    private void initRealSortTask() {
        for (AbsParallelTask appStartTask : mSortTaskList) {
            if (appStartTask.isRunOnMainThread()) {
                mSortMainThreadTaskList.add(appStartTask);
            } else {
                mSortThreadPoolTaskList.add(appStartTask);
            }
        }
    }

    /**
     * 输出排好序的Task
     */
    private void printSortTask() {
        StringBuilder sb = new StringBuilder();
        sb.append("当前所有任务排好的顺序为：");
        for (int i = 0; i < mSortTaskList.size(); i++) {
            String taskName = mSortTaskList.get(i).getClass().getSimpleName();
            if (i != 0) {
                sb.append("---＞");
            }
            sb.append(taskName);
        }
        ParallelTaskUtils.showLog(isShowLog, sb.toString());
    }

    /**
     * 发送任务
     */
    private void dispatchAppStartTask() {
        //再发送非主线程的任务
        for (AbsParallelTask appStartTask : mSortThreadPoolTaskList) {
            TaskRunnable runnable = new TaskRunnable(appStartTask, this);
            //提交一个事务
            appStartTask.runOnExecutor().execute(runnable);
        }
        //再发送主线程的任务，防止主线程任务阻塞，导致子线程任务不能立刻执行
        for (AbsParallelTask appStartTask : mSortMainThreadTaskList) {
            new TaskRunnable(appStartTask, this).run();
        }
    }

    /**
     * 通知Children一个前置任务已完成
     * @param appStartTask      task
     */
    void setNotifyChildren(AbsParallelTask appStartTask) {
        List<Class<? extends AbsParallelTask>> arrayList = mTaskChildHashMap.get(appStartTask.getClass());
        if (arrayList != null && arrayList.size() > 0) {
            for (Class<? extends AbsParallelTask> aclass : arrayList) {
                AbsParallelTask startTask = mTaskHashMap.get(aclass);
                if (startTask != null) {
                    startTask.notifyTask();
                }
            }
        }
    }

    /**
     * 标记已经完成的Task
     * @param appStartTask      task
     */
    void markAppStartTaskFinish(AbsParallelTask appStartTask) {
        ParallelTaskUtils.showLog(isShowLog, "任务完成了：" + appStartTask.getClass().getSimpleName());
        if (ifNeedWait(appStartTask)) {
            mCountDownLatch.countDown();
            mNeedWaitCount.getAndDecrement();
        }
    }

    /**
     * 是否需要等待，主线程的任务本来就是阻塞的
     * @param task      task任务
     * @return          是否阻塞
     */
    private boolean ifNeedWait(AbsParallelTask task) {
        return !task.isRunOnMainThread() && task.needWait();
    }

    /**
     * 等待，阻塞主线程
     */
    public void await() {
        try {
            if (mCountDownLatch == null) {
                throw new RuntimeException("在调用await()之前，必须先调用start()");
            }
            if (mAllTaskWaitTimeOut == 0) {
                mAllTaskWaitTimeOut = WAITING_TIME;
            }
            mCountDownLatch.await(mAllTaskWaitTimeOut, TimeUnit.MILLISECONDS);
            mFinishTime = System.currentTimeMillis() - mStartTime;
            ParallelTaskUtils.showLog(isShowLog, "启动耗时：" + mFinishTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
