package com.yc.serialtasklib;


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

    public static void async(DispatchRunnable task) {
        async(poolGroup(), task);
    }

    public static void async(Executor threadGroup, DispatchRunnable task) {
        (new InnerAsyncTask1()).executeOnExecutor(threadGroup, new DispatchRunnable[]{task});
    }

    private static class InnerAsyncTask1 extends AsyncTask<DispatchRunnable, Void, DispatchRunnable> {
        private InnerAsyncTask1() {
        }

        protected DispatchRunnable doInBackground(DispatchRunnable... tasks) {
            tasks[0].onWorkThread();
            return tasks[0];
        }

        protected void onPostExecute(DispatchRunnable task) {
            task.onMainThread();
        }
    }

    private static class InnerAsyncTask0 extends AsyncTask<Runnable, Void, Void> {
        private InnerAsyncTask0() {
        }

        protected Void doInBackground(Runnable... tasks) {
            tasks[0].run();
            return null;
        }
    }
}

