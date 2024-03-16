package com.yc.appcontextlib.serial;


import android.os.AsyncTask;
import java.util.concurrent.Executor;

public class Dispatcher {

    public Dispatcher() {

    }

    public static Executor singleGroup() {
        return AsyncTask.SERIAL_EXECUTOR;
    }

    public static Executor poolGroup() {
        return AsyncTask.THREAD_POOL_EXECUTOR;
    }

    public static void async(Runnable task) {
        async(poolGroup(), task);
    }

    public static void async(Executor threadGroup, Runnable task) {
        (new InnerAsyncTask0()).executeOnExecutor(threadGroup, new Runnable[]{task});
    }

    public static void async(IDispatchRunnable task) {
        async(poolGroup(), task);
    }

    public static void async(Executor threadGroup, IDispatchRunnable task) {
        (new InnerAsyncTask1()).executeOnExecutor(threadGroup, new IDispatchRunnable[]{task});
    }

    private static class InnerAsyncTask1 extends AsyncTask<IDispatchRunnable, Void, IDispatchRunnable> {
        private InnerAsyncTask1() {
        }

        @Override
        protected IDispatchRunnable doInBackground(IDispatchRunnable... tasks) {
            tasks[0].onWorkThread();
            return tasks[0];
        }

        @Override
        protected void onPostExecute(IDispatchRunnable task) {
            task.onMainThread();
        }
    }

    private static class InnerAsyncTask0 extends AsyncTask<Runnable, Void, Void> {
        private InnerAsyncTask0() {
        }

        @Override
        protected Void doInBackground(Runnable... tasks) {
            tasks[0].run();
            return null;
        }
    }
}

