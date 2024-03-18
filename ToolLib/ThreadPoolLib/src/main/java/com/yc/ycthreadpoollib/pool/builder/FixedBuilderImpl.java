package com.yc.ycthreadpoollib.pool.builder;

import com.yc.ycthreadpoollib.pool.config.ThreadPoolType;
import com.yc.ycthreadpoollib.pool.factory.MyThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/05/17
 *     desc  : SingleBuilder
 *     revise: 线程数量固定的线程池，全部为核心线程，响应较快，不用担心线程会被回收。
 * </pre>
 */
public class FixedBuilderImpl extends AbsThreadPoolBuilder<ExecutorService> {

    private int mSize = 1;

    public FixedBuilderImpl() {
    }

    public ExecutorService create(MyThreadFactory myThreadFactory) {
        // //相当于这个
        /*return new ThreadPoolExecutor(
                this.mSize, this.mSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                myThreadFactory);*/
        //线程数量固定的线程池，全部为核心线程，响应较快，不用担心线程会被回收。
        return Executors.newFixedThreadPool(this.mSize, myThreadFactory);
    }

    public ThreadPoolType getType() {
        return ThreadPoolType.FIXED;
    }

    public FixedBuilderImpl setSize(int size) {
        this.mSize = size;
        return this;
    }
}
