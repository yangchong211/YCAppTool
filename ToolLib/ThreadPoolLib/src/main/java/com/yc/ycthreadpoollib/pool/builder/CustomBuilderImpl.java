package com.yc.ycthreadpoollib.pool.builder;

import com.yc.ycthreadpoollib.pool.config.ThreadPoolType;
import com.yc.ycthreadpoollib.pool.factory.MyThreadFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/05/17
 *     desc  : 自定义线程池
 *     revise: 这个是高级的用法，需要对线程池有较深入的理解才可以使用。否则建议直接使用系统提供api创建线程池
 * </pre>
 */
public class CustomBuilderImpl extends AbsThreadPoolBuilder<ExecutorService> {

    private int mCorePoolSize = 1;
    private int mMaximumPoolSize = Integer.MAX_VALUE;
    private long mKeepAliveTime = 60L;
    private TimeUnit mUnit;
    private BlockingQueue<Runnable> mWorkQueue;

    public CustomBuilderImpl() {
        this.mUnit = TimeUnit.SECONDS;
        //设置默认的线程处理的队列
        //作用是，把提交的runnable存储到队列里面
        this.mWorkQueue = new SynchronousQueue<>();
    }

    public ExecutorService create(MyThreadFactory myThreadFactory) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                this.mCorePoolSize, this.mMaximumPoolSize,
                this.mKeepAliveTime, this.mUnit,
                this.mWorkQueue, myThreadFactory);
        return threadPoolExecutor;
    }

    public ThreadPoolType getType() {
        return ThreadPoolType.CUSTOM;
    }

    /**
     * 线程池中核心线程的数量
     *
     * @param corePoolSize
     * @return
     */
    public CustomBuilderImpl corePoolSize(int corePoolSize) {
        this.mCorePoolSize = corePoolSize;
        return this;
    }

    /**
     * 线程池中的最大线程数，当任务数量超过最大线程数时其它任务可能就会被阻塞
     *
     * @param maximumPoolSize
     * @return
     */
    public CustomBuilderImpl maximumPoolSize(int maximumPoolSize) {
        this.mMaximumPoolSize = maximumPoolSize;
        return this;
    }

    /**
     * 非核心线程的超时时长，当执行时间超过这个时间时，非核心线程就会被回收
     *
     * @param keepAliveTime
     * @return
     */
    public CustomBuilderImpl keepAliveTime(long keepAliveTime) {
        this.mKeepAliveTime = keepAliveTime;
        return this;
    }

    /**
     * 枚举时间单位
     *
     * @param unit
     * @return
     */
    public CustomBuilderImpl unit(TimeUnit unit) {
        this.mUnit = unit;
        return this;
    }

    /**
     * 线程池中的任务队列，我们提交给线程池的runnable会被存储在这个对象上
     *
     * @param workQueue
     * @return
     */
    public CustomBuilderImpl workQueue(BlockingQueue<Runnable> workQueue) {
        this.mWorkQueue = workQueue;
        return this;
    }

}

