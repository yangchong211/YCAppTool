package com.yc.ycthreadpoollib.pool.builder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.yc.ycthreadpoollib.pool.config.ThreadPoolType;
import com.yc.ycthreadpoollib.pool.factory.MyThreadFactory;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/05/17
 *     desc  : 缓存线程池
 *     revise: 它是一个数量无限多的线程池，都是非核心线程，适合执行大量耗时小的任务
 * </pre>
 */
public class CachedBuilderImpl extends AbsThreadPoolBuilder<ExecutorService> {

    public CachedBuilderImpl() {

    }

    public ExecutorService create(MyThreadFactory myThreadFactory) {
        //相当于这个
        /*return new ThreadPoolExecutor(
                0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                myThreadFactory);*/
        //它是一个数量无限多的线程池，都是非核心线程，适合执行大量耗时小的任务
        return Executors.newCachedThreadPool(myThreadFactory);
    }

    public ThreadPoolType getType() {
        return ThreadPoolType.CACHED;
    }
}
