package com.yc.taskscheduler;


import android.os.Handler;
import android.os.Process;
import android.util.Log;
import com.yc.easyexecutor.DelegateTaskExecutor;
import com.yc.easyexecutor.SafeHandler;
import com.yc.easyexecutor.TaskHandlerThread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public final class TaskScheduler {

    private volatile static TaskScheduler sTaskScheduler;
    private static final String TAG = "TaskScheduler";
    private final ExecutorService mParallelExecutor ;
    private final ExecutorService mTimeOutExecutor ;
    private InterLog mILog = new InterLog() {
        @Override
        public void info(String info) {
            Log.i(TAG,info);
        }

        @Override
        public void error(String error) {
            Log.e(TAG,error);
        }
    };

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int MAXIMUM_POOL_SIZE = CORE_POOL_SIZE * 2 + 1;
    private static final long KEEP_ALIVE = 60L;
    private static final BlockingQueue<Runnable> POOL_WORK_QUEUE =
            new LinkedBlockingQueue<>(128);

    public static TaskScheduler getInstance() {
        if(sTaskScheduler==null) {
            synchronized (TaskScheduler.class) {
                if(sTaskScheduler==null) {
                    sTaskScheduler = new TaskScheduler();
                }
            }
        }
        return sTaskScheduler;
    }

    private TaskScheduler() {
        mParallelExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,
                KEEP_ALIVE,TimeUnit.SECONDS,POOL_WORK_QUEUE, MyThreadFactory.TASK_SCHEDULER_FACTORY);
        /*
          没有核心线程的线程池要用 SynchronousQueue 而不是LinkedBlockingQueue，SynchronousQueue是一个只有一个任务的队列，
          这样每次就会创建非核心线程执行任务,因为线程池任务放入队列的优先级比创建非核心线程优先级大.
         */
        mTimeOutExecutor = new ThreadPoolExecutor(0,MAXIMUM_POOL_SIZE,
                KEEP_ALIVE,TimeUnit.SECONDS,new SynchronousQueue<Runnable>(), MyThreadFactory.TIME_OUT_THREAD_FACTORY);
    }

    public static void addLogImpl(InterLog taskLog) {
        if(taskLog != null) {
            getInstance().mILog = taskLog;
        }
    }

    public static ExecutorService executorService() {
        return getInstance().mParallelExecutor;
    }

    /**
     * 获取回调到handlerName线程的handler.一般用于在一个后台线程执行同一种任务，避免线程安全问题。如数据库，文件操作
     * @param handlerName 线程名
     * @return 异步任务handler
     */
    public static Handler provideHandler(String handlerName) {
        TaskHandlerThread taskHandlerThread = TaskHandlerThread.get(handlerName);
        taskHandlerThread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
        taskHandlerThread.start();
        return new SafeHandler(taskHandlerThread.getLooper());
    }

    /**
     * 主线程周期性执行任务，默认立刻执行，之后间隔period执行，不需要时注意取消,每次执行时如果有相同的任务，默认会先取消
     * @param task 执行的任务
     */
    public static void scheduleTask(final SchedulerTask task) {
        task.canceled.compareAndSet(true,false);
        final ScheduledExecutorService service = new ScheduledThreadPoolExecutor(
                1, MyThreadFactory.SCHEDULER_THREAD_FACTORY);
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(task.canceled.get()) {
                    service.shutdownNow();
                }else {
                    if(task.mainThread) {
                        DelegateTaskExecutor.getInstance().postToMainThread(task);
                    }else {
                        task.run();
                    }
                }
            }
        }, task.startDelayMillisecond, task.periodMillisecond, TimeUnit.MILLISECONDS);
    }

    /**
     * 取消周期性任务
     * @param schedulerTask 任务对象
     */
    public static void stopScheduleTask(final SchedulerTask schedulerTask) {
        schedulerTask.canceled.compareAndSet(false,true);
    }


    /**
     *执行一个后台任务，无回调
     * **/
    public static void execute(Runnable task) {
        getInstance().mILog.info("execute Runnable"+task.toString());
        getInstance().mParallelExecutor.execute(task);
    }

    /**
     *执行一个后台任务，如果不需回调
     * @see #execute(Runnable)
     **/
    public static <R> void execute(AbsTaskRunnable<R> task) {
        getInstance().mILog.info("execute task"+task.toString());
        getInstance().mParallelExecutor.execute(task);
    }

    /**
     * 取消一个任务
     * @param task 被取消的任务
     */
    public static void cancelTask(AbsTaskRunnable task) {
        if(task!=null) {
            task.cancel();
        }
    }

    /**
     * 使用一个单独的线程池来执行超时任务，避免引起他线程不够用导致超时
     *  @param timeOutMillis  超时时间，单位毫秒
     ** 通过实现error(Exception) 判断是否为 TimeoutException 来判断是否超时,
     *                        不能100%保证实际的超时时间就是timeOutMillis，但一般没必要那么精确
     * */
    public static <R> void executeTimeOutTask(final long timeOutMillis, final AbsTaskRunnable<R> timeOutTask) {
        final Future future = getInstance().mTimeOutExecutor.submit(timeOutTask);
        getInstance().mTimeOutExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    future.get(timeOutMillis,TimeUnit.MILLISECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException e ) {
                    DelegateTaskExecutor.getInstance().postToMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!timeOutTask.isCanceled()) {
                                timeOutTask.cancel();
                            }
                        }
                    });
                }
            }
        });
    }

}
