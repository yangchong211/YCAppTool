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

