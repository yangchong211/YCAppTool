package com.yc.ycthreadpoollib.easy;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import java.util.concurrent.Executor;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2019/5/11
 *     desc   : task代理类。具体用这个
 *     revise :
 *     GitHub : https://github.com/yangchong211/YCThreadPool
 * </pre>
 */
public class DelegateTaskExecutor extends AbsTaskExecutor {

    /**
     * 使用 volatile 保证代理对象是唯一的
     */
    private static volatile DelegateTaskExecutor sInstance;

    @NonNull
    private AbsTaskExecutor mDelegate;

    @NonNull
    private final AbsTaskExecutor mDefaultTaskExecutor;

    @NonNull
    private static final Executor sMainThreadExecutor = new Executor() {
        @Override
        public void execute(Runnable command) {
            if (command != null) {
                getInstance().postToMainThread(command);
            }
        }
    };

    @NonNull
    private static final Executor sCoreThreadExecutor = new Executor() {
        @Override
        public void execute(Runnable command) {
            if (command != null) {
                getInstance().executeOnCore(command);
            }
        }
    };

    @NonNull
    private static final Executor sIOThreadExecutor = new Executor() {
        @Override
        public void execute(Runnable command) {
            if (command != null) {
                getInstance().executeOnDiskIO(command);
            }
        }
    };

    @NonNull
    private static final Executor sCpuThreadExecutor = new Executor() {
        @Override
        public void execute(Runnable command) {
            if (command != null) {
                getInstance().executeOnCpu(command);
            }
        }
    };

    private DelegateTaskExecutor() {
        //创建委托类
        mDefaultTaskExecutor = new DefaultTaskExecutor();
        mDelegate = mDefaultTaskExecutor;
    }

    @NonNull
    public static DelegateTaskExecutor getInstance() {
        if (sInstance != null) {
            return sInstance;
        }
        synchronized (DelegateTaskExecutor.class) {
            if (sInstance == null) {
                sInstance = new DelegateTaskExecutor();
            }
        }
        return sInstance;
    }

    /**
     * 外部开发者，可以调用该方法设置自己的taskExecutor
     *
     * @param taskExecutor AbsTaskExecutor实现类
     */
    public void setDelegate(@Nullable AbsTaskExecutor taskExecutor) {
        mDelegate = taskExecutor == null ? mDefaultTaskExecutor : taskExecutor;
    }

    @Override
    public void executeOnCore(@NonNull Runnable runnable) {
        if (runnable != null) {
            mDelegate.executeOnCore(runnable);
        }
    }

    @Override
    public void executeOnDiskIO(@Nullable Runnable runnable) {
        if (runnable != null) {
            mDelegate.executeOnDiskIO(runnable);
        }
    }

    @Override
    public void executeOnCpu(@NonNull Runnable runnable) {
        if (runnable != null) {
            mDelegate.executeOnCpu(runnable);
        }
    }

    @Override
    public void executeOnScheduled(@NonNull Runnable runnable, long delayMills) {
        if (runnable != null) {
            mDelegate.executeOnScheduled(runnable, delayMills);
        }
    }

    @Override
    public void postToMainThread(@Nullable Runnable runnable) {
        if (runnable != null) {
            mDelegate.postToMainThread(runnable);
        }
    }

    @Override
    public void postToMainThreadDelay(@Nullable Runnable runnable, long delayMills) {
        if (runnable != null) {
            mDelegate.postToMainThreadDelay(runnable, delayMills);
        }
    }

    @Override
    public void removeRunnable(@Nullable Runnable runnable) {
        if (runnable != null) {
            mDelegate.removeRunnable(runnable);
        }
    }

    @Override
    public Handler getMainHandler() {
        return mDelegate.getMainHandler();
    }

    @Override
    public void postIoHandler(@NonNull Runnable runnable) {
        if (runnable != null) {
            mDelegate.postIoHandler(runnable);
        }
    }

    /**
     * 使用HandlerThread和handler处理消息
     *
     * @return MainThreadExecutor
     */
    @NonNull
    public Executor getMainThreadExecutor() {
        return sMainThreadExecutor;
    }

    /**
     * 获取核心线程池，主要是执行一些核心任务，比如初始化相关操作
     *
     * @return CoreThreadExecutor
     */
    @NonNull
    public Executor getCoreThreadExecutor() {
        return sCoreThreadExecutor;
    }

    /**
     * 获得io密集型线程池，有好多任务其实占用的CPU time非常少，所以使用缓存线程池,基本上来着不拒
     *
     * @return IOThreadPoolExecutor
     */
    @NonNull
    public Executor getIOThreadExecutor() {
        return sIOThreadExecutor;
    }

    /**
     * 获得cpu密集型线程池,因为占据CPU的时间片过多的话会影响性能，所以这里控制了最大并发，防止主线程的时间片减少
     *
     * @return CPUThreadPoolExecutor
     */
    @NonNull
    public Executor getCpuThreadExecutor() {
        return sCpuThreadExecutor;
    }


    /**
     * 判断是否是主线程
     *
     * @return true表示主线成
     */
    @Override
    public boolean isMainThread() {
        return mDelegate.isMainThread();
    }


    /**
     * 执行有生命周期的任务
     */
    public Runnable postToMainThread(LifecycleOwner lifecycleOwner, Runnable runnable) {
        LifecycleRunnable lifecycleRunnableDelegate = new LifecycleRunnable(lifecycleOwner,
                getMainHandler(), Lifecycle.Event.ON_DESTROY, runnable);
        mDelegate.postToMainThread(lifecycleRunnableDelegate);
        return lifecycleRunnableDelegate;
    }


    /**
     * 执行有生命周期的任务,指定Lifecycle.Event
     */
    public Runnable postToMainThread(LifecycleOwner lifecycleOwner,
                                     Lifecycle.Event targetEvent, Runnable runnable) {
        LifecycleRunnable lifecycleRunnableDelegate = new LifecycleRunnable(lifecycleOwner,
                getMainHandler(), targetEvent, runnable);
        mDelegate.postToMainThread(lifecycleRunnableDelegate);
        return lifecycleRunnableDelegate;
    }

    public void postToMainThread(Runnable runnable, long delayed) {
        if (runnable != null) {
            getMainHandler().postDelayed(runnable, delayed);
        }
    }

    public Runnable postToMainThread(LifecycleOwner lifecycleOwner, Runnable runnable, long delayed) {
        LifecycleRunnable lifecycleRunnableDelegate = new LifecycleRunnable(lifecycleOwner,
                getMainHandler(), Lifecycle.Event.ON_DESTROY, runnable);
        getMainHandler().postDelayed(lifecycleRunnableDelegate, delayed);
        return lifecycleRunnableDelegate;
    }

    public void removeUICallback(Runnable runnable) {
        if (runnable != null) {
            getMainHandler().removeCallbacks(runnable);
        }
    }

}
