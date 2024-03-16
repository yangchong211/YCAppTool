package com.yc.appcontextlib.parallel;

import android.os.Process;

import androidx.annotation.IntRange;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author: 杨充
 * @email : yangchong211@163.com
 * @time : 2019/03/15
 * @desc : task任务接口
 * @revise :
 * GitHub ：https://github.com/yangchong211/YCEfficient
 */
public interface TaskInterface {

    /**
     * 线程的优先级
     *
     * @return 优先级
     */
    @IntRange(from = Process.THREAD_PRIORITY_FOREGROUND, to = Process.THREAD_PRIORITY_LOWEST)
    int priority();

    /**
     * 执行任务所在的线程池
     *
     * @return Executor对象
     */
    Executor runOnExecutor();

    /**
     * 所依赖的父亲们,父亲们执行完了，孩子才能执行
     *
     * @return 集合
     */
    List<Class<? extends AbsParallelTask>> getDependsTaskList();

    /**
     * 在非主线程执行的Task是否需要在被调用await的时候等待，默认不需要
     *
     * @return 返回true即在Application的onCreate中阻塞，直到该任务执行完
     */
    boolean needWait();


}
