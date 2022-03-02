package com.yc.taskscheduler;

import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;


public abstract class AbsTaskRunnable<R> implements Runnable {

    private static final String TAG = "Task";
    private final AtomicBoolean mCanceledAtomic = new AtomicBoolean(false);
    private final AtomicReference<Thread> mTaskThread = new AtomicReference<>();


    /**
     * 异步线程处理任务，在非主线程执行
     * @return 处理后的结果
     * @throws InterruptedException 获取InterruptedException异常，来判断任务是否被取消
     */
    public abstract R doInBackground() throws InterruptedException;

    /**
     * 异步线程处理后返回的结果，在主线程执行
     * @param result 结果
     */
    public abstract void onSuccess(R result);

    /**
     * 异步线程处理出现异常的回调，按需处理，未置成抽象，主线程执行
     * @param throwable 异常
     */
    public void onFail(Throwable throwable){

    }

    /**
     * 任务被取消的回调，主线程执行
     *
     */
    public void onCancel(){

    }

    /**
     * 将任务标记为取消，没法真正取消正在执行的任务，只是结果不在onSuccess里回调
     * cancel 不一定能让任务停止，和AsyncTask同样道理，可参考
     * {#link http://silencedut.com/2016/07/08/%E5%9F%BA%E4%BA%8E%E6%9C%80%E6%96%B0%E7%89%88%E6%9C%AC%E7%9A%84AsyncTask%E6%BA%90%E7%A0%81%E8%A7%A3%E8%AF%BB%E5%8F%8AAsyncTask%E7%9A%84%E9%BB%91%E6%9A%97%E9%9D%A2/}
     **/

    void cancel() {

        this.mCanceledAtomic.set(true);

        Thread t = mTaskThread.get();
        if(t!=null) {
            Log.d(TAG,"Task cancel: "+t.getName());
            t.interrupt();
        }

        TaskScheduler.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                onCancel();
            }
        });
    }

    /**
     * 任务是已取消
     * @return 任务是否已被取消
     */
    public boolean isCanceled() {

        return mCanceledAtomic.get();
    }

    @Override
    public void run() {
        try {

            Log.d(TAG,"Task : "+Thread.currentThread().getName());
            mTaskThread.compareAndSet(null,Thread.currentThread());

            mCanceledAtomic.set(false);

            final R result = doInBackground();

            TaskScheduler.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if(!isCanceled()){
                        onSuccess(result);
                    }
                }
            });
        }  catch (final Throwable throwable) {

            Log.e(TAG,"handle background Task  error " +throwable);
            TaskScheduler.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if(!isCanceled()){
                        onFail(throwable);
                    }
                }
            });
        }
    }

}
