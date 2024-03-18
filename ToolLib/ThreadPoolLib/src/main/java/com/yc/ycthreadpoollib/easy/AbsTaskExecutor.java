package com.yc.ycthreadpoollib.easy;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2019/5/11
 *     desc   : 抽象task任务类
 *     revise :
 *     GitHub : https://github.com/yangchong211/YCThreadPool
 * </pre>
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public abstract class AbsTaskExecutor {

    /**
     * 使用核心任务的线程池执行给定的任务
     *
     * @param runnable 核心任务
     */
    public abstract void executeOnCore(@NonNull Runnable runnable);

    /**
     * 在磁盘IO线程池中执行给定的任务
     *
     * @param runnable 在磁盘IO线程池中运行的可运行程序
     */
    public abstract void executeOnDiskIO(@NonNull Runnable runnable);

    /**
     * 使用 CPU 密集型任务的线程池执行给定的任务
     *
     * @param runnable runnable
     */
    public abstract void executeOnCpu(@NonNull Runnable runnable);

    /**
     * 执行定时以及周期性任务，执行异步操作
     *
     * @param runnable   runnable
     * @param delayMills 延迟毫秒数
     */
    public abstract void executeOnScheduled(@NonNull Runnable runnable, long delayMills);

    /**
     * 使用UI主线程共有handler对象，执行post任务【将给定的任务提交给主线程】
     *
     * @param runnable runnable
     */
    public abstract void postToMainThread(@NonNull Runnable runnable);

    /**
     * 使用UI主线程共有handler对象，执行post任务【将给定的任务提交给主线程】
     *
     * @param runnable runnable
     */
    public abstract void postToMainThreadDelay(@NonNull Runnable runnable, long delayMills);

    /**
     * 移除消息
     * @param runnable  runnable
     */
    public abstract void removeRunnable(@NonNull Runnable runnable);

    /**
     * 获取UI主线程共有handler对象
     *
     * @return handler
     */
    public abstract Handler getMainHandler();

    /**
     * 配合handlerThread使用的handler，一般用来执行大量任务
     *
     * @param runnable runnable
     */
    public abstract void postIoHandler(@NonNull Runnable runnable);

    /**
     * 使用UI主线程共有handler对象，执行任务
     *
     * @param runnable runnable
     */
    public void executeOnMainThread(@NonNull Runnable runnable) {
        if (isMainThread()) {
            runnable.run();
        } else {
            postToMainThread(runnable);
        }
    }

    /**
     * 判断是否是主线程
     *
     * @return true表示主线程
     */
    public abstract boolean isMainThread();
}
