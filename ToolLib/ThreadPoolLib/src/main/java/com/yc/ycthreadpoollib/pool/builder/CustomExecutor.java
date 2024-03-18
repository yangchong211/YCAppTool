package com.yc.ycthreadpoollib.pool.builder;

import android.util.Log;

import com.yc.ycthreadpoollib.pool.factory.MyThreadFactory;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CustomExecutor extends ThreadPoolExecutor {

    public CustomExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, int priority) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new SynchronousQueue<>(),
                new MyThreadFactory(priority));
    }

    @Override
    public void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        //统一线程池异常处理，添加日志，异常上报等
        //execute执行的情况下，会先执行afterExecute，然后重新抛出异常。而不是被吞掉。只有submit才会吞异常。
        if (t == null && r instanceof Future<?>) {
            try {
                ((Future<?>) r).get();
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException ee) {
                t = ee.getCause();
            } catch (InterruptedException ie) {
                // ignore/reset
                Thread.currentThread().interrupt();
            }
        }
        if (t != null) {
            Log.v("CustomExecutor", "afterExecute:" + t.toString());
            t.printStackTrace();
        }
    }

    /**
     * 执行异步操作
     *
     * @param runnable runnable
     */
    public void post(Runnable runnable) {
        execute(runnable);
    }

}
