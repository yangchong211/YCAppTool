package com.yc.ycthreadpoollib.pool.builder;

import com.yc.ycthreadpoollib.pool.config.ThreadPoolType;
import com.yc.ycthreadpoollib.pool.factory.MyThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/05/17
 *     desc  : SingleBuilder
 *     revise: 有数量固定的核心线程，且有数量无限多的非核心线程，适合用于执行定时任务和固定周期的重复任务
 * </pre>
 */
public class ScheduledBuilderImpl extends AbsThreadPoolBuilder<ExecutorService> {

    private int mSize = 1;

    public ScheduledBuilderImpl() {
    }

    public ScheduledExecutorService create(MyThreadFactory myThreadFactory) {
        //有数量固定的核心线程，且有数量无限多的非核心线程，适合用于执行定时任务和固定周期的重复任务
        return Executors.newScheduledThreadPool(this.mSize, myThreadFactory);
    }

    public ThreadPoolType getType() {
        return ThreadPoolType.SCHEDULED;
    }

    public ScheduledBuilderImpl setSize(int size) {
        this.mSize = size;
        return this;
    }
}

