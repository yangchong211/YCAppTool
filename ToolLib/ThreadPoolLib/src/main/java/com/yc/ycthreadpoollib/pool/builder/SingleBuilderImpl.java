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
 *     revise: 内部只有一个核心线程，所有任务进来都要排队按顺序执行
 * </pre>
 */
public class SingleBuilderImpl extends AbsThreadPoolBuilder<ExecutorService> {

    public SingleBuilderImpl() {

    }

    public ExecutorService create(MyThreadFactory myThreadFactory) {
        //相当于这个
        /*return new FinalizableDelegatedExecutorService
                (new ThreadPoolExecutor(1, 1,
                        0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>(),
                        myThreadFactory));*/
        //内部只有一个核心线程，所有任务进来都要排队按顺序执行
        return Executors.newSingleThreadExecutor(myThreadFactory);
    }

    public ThreadPoolType getType() {
        return ThreadPoolType.SINGLE;
    }
}
