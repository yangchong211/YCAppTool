package com.yc.ycthreadpoollib.easy;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2019/5/11
 *     desc   : task具体实现类，委托类
 *     revise :
 *     GitHub : https://github.com/yangchong211/YCThreadPool
 * </pre>
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class DefaultTaskExecutor extends AbsTaskExecutor {

    /**
     * 创建对象锁
     * 锁是加在单个对象上，不同的对象没有竞争关系。单个对象，在多线程条件下调用数据是安全的
     */
    private final Object mLock = new Object();
    /**
     * UI主线程共有handler对象
     */
    private volatile Handler mMainHandler;
    /**
     * 配合handlerThread使用的handler，一般用来执行大量任务
     */
    private volatile Handler mIoHandler;
    /**
     * 核心任务的线程池
     */
    private final ExecutorService mCoreExecutor;
    /**
     * IO 密集型任务的线程池
     */
    private final ExecutorService mDiskIO;
    /**
     * CPU 密集型任务的线程池
     * 线程池中线程个数应尽量少，如配置N+1个线程的线程池
     */
    private final ThreadPoolExecutor mCPUThreadPoolExecutor;
    /**
     * 用来执行定时以及周期性任务
     * 优先级较高，核心线程数量固定、非核心线程数量无限制，不会阻塞（闲置时马上回收）
     */
    private final ScheduledThreadPoolExecutor mDelayExecutor;
    /**
     * CPU 核数
     * Java虚拟机可用处理器数量
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    /**
     * 线程池线程数
     * 线程池的核心线程数。在没有设置 allowCoreThreadTimeOut 为 true 的情况下，核心线程会在线程池中一直存活，即使处于闲置状态。
     */
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 5));
    /**
     * 线程池线程数的最大值
     */
    private static final int MAXIMUM_POOL_SIZE = CORE_POOL_SIZE;
    /**
     * 线程空置回收时间
     */
    private static final int KEEP_ALIVE_SECONDS = 5;
    /**
     * 这个是为了保障任务超出BlockingQueue的最大值，且线程池中的线程数已经达到MAXIMUM_POOL_SIZE时候
     * 还有任务到来会采取任务拒绝策略，这里定义的策略就是再开一个缓存线程池去执行。
     * 当然BlockingQueue默认的最大值是int_max，所以理论上这里是用不到的
     */
    private final RejectedExecutionHandler mHandler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            Executors.newCachedThreadPool().execute(r);
        }
    };

    public DefaultTaskExecutor() {
        //主要是处理io密集流
        mDiskIO = Executors.newFixedThreadPool(4, new MyThreadFactory("LoggerTask #"));
        //处理比较核心的任务
        mCoreExecutor = Executors.newSingleThreadExecutor(new MyThreadFactory("ScheduleTask"));
        //处理cpu密集任务
        mCPUThreadPoolExecutor = new MyThreadPoolExecutor(
                CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(), mHandler);
        mCPUThreadPoolExecutor.allowCoreThreadTimeOut(true);
        //处理其他
        mDelayExecutor = new ScheduledThreadPoolExecutor(CORE_POOL_SIZE);
    }


    @Override
    public void executeOnCore(@Nullable Runnable runnable) {
        if (runnable != null && mCoreExecutor != null) {
            //提交一个事务，runnable
            mCoreExecutor.execute(runnable);
        }
    }

    @Override
    public void executeOnDiskIO(@Nullable Runnable runnable) {
        if (runnable != null && mDiskIO != null) {
            mDiskIO.execute(runnable);
        }
    }

    @Override
    public void executeOnCpu(@Nullable Runnable runnable) {
        if (runnable != null && mCPUThreadPoolExecutor != null) {
            mCPUThreadPoolExecutor.execute(runnable);
        }
    }

    @Override
    public void executeOnScheduled(@Nullable Runnable runnable, long delayMills) {
        if (runnable != null && mDelayExecutor != null) {
            mDelayExecutor.schedule(runnable, delayMills, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void postToMainThread(@Nullable Runnable runnable) {
        mMainHandler = getMainHandler();
        if (mMainHandler != null && runnable != null) {
            mMainHandler.post(runnable);
        }
    }

    @Override
    public void postToMainThreadDelay(@Nullable Runnable runnable, long delayMills) {
        mMainHandler = getMainHandler();
        if (mMainHandler != null && runnable != null) {
            mMainHandler.postDelayed(runnable, delayMills);
        }
    }

    @Override
    public void removeRunnable(@Nullable Runnable runnable) {
        mMainHandler = getMainHandler();
        if (mMainHandler != null && runnable != null) {
            mMainHandler.removeCallbacks(runnable);
        }
    }

    @Override
    public Handler getMainHandler() {
        if (mMainHandler == null) {
            synchronized (mLock) {
                if (mMainHandler == null) {
                    mMainHandler = new SafeHandler(Looper.getMainLooper());
                }
            }
        }
        return mMainHandler;
    }

    @Override
    public void postIoHandler(@Nullable Runnable runnable) {
        if (mIoHandler == null) {
            synchronized (mLock) {
                if (mIoHandler == null) {
                    mIoHandler = TaskHandlerThread.get("postIoHandler")
                            .getHandler("postIoHandler");
                }
            }
        }
        if (mIoHandler != null && runnable != null) {
            mIoHandler.post(runnable);
        }
    }

    @Override
    public boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }


}
