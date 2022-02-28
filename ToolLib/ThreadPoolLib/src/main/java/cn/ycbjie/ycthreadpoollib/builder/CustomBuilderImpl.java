package cn.ycbjie.ycthreadpoollib.builder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.ycbjie.ycthreadpoollib.config.ThreadPoolType;
import cn.ycbjie.ycthreadpoollib.factory.MyThreadFactory;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/05/17
 *     desc  : SingleBuilder
 *     revise:
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
        this.mWorkQueue = new SynchronousQueue<>();
    }

    public ExecutorService create(MyThreadFactory myThreadFactory) {
        return new ThreadPoolExecutor(this.mCorePoolSize, this.mMaximumPoolSize,
                this.mKeepAliveTime, this.mUnit, this.mWorkQueue, myThreadFactory);
    }

    public ThreadPoolType getType() {
        return ThreadPoolType.CUSTOM;
    }

    public CustomBuilderImpl corePoolSize(int corePoolSize) {
        this.mCorePoolSize = corePoolSize;
        return this;
    }

    public CustomBuilderImpl maximumPoolSize(int maximumPoolSize) {
        this.mMaximumPoolSize = maximumPoolSize;
        return this;
    }

    public CustomBuilderImpl keepAliveTime(long keepAliveTime) {
        this.mKeepAliveTime = keepAliveTime;
        return this;
    }

    public CustomBuilderImpl unit(TimeUnit unit) {
        this.mUnit = unit;
        return this;
    }

    public CustomBuilderImpl workQueue(BlockingQueue<Runnable> workQueue) {
        this.mWorkQueue = workQueue;
        return this;
    }
}

