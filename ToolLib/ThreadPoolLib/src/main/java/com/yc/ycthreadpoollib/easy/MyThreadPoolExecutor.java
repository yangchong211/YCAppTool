package com.yc.ycthreadpoollib.easy;

import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 向该线程池中提交了好多异步任务，但是这些任务都没有对异常进行`try-catch`处理，并且运行的时候都抛出了异常。
 * 这会对线程池的运行带来什么影响？正确答案是：没有影响。
 * 线程池会捕获包括`Error`在内的所有异常，并且在程序最后，将出现过的异常和当前任务传递给`afterExecute`方法(空方法，不作任何处理)。
 */
public class MyThreadPoolExecutor extends ThreadPoolExecutor {

    private static final String TAG = "EasyExecutor";

    public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                new MyThreadFactory("EasyExecutor"));
    }

    public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                TimeUnit unit, BlockingQueue<Runnable> workQueue,
                                RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                new MyThreadFactory("EasyExecutor"), handler);
    }


    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (t == null && r instanceof Future<?>) {
            try {
                ((Future<?>) r).get();
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException ee) {
                t = ee.getCause();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        if (t != null) {
            Log.d(TAG, "afterExecute:" + t);
            t.printStackTrace();
        }
    }
}
