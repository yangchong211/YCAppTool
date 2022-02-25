package cn.ycbjie.ycthreadpoollib;


import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public final class ScheduleTask {

    private final ExecutorService mExecutors;

    private ScheduleTask() {
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable runnable) {
                //创建一个线程
                return new Thread(runnable, "ScheduleTask");
            }
        };
        this.mExecutors = Executors.newSingleThreadExecutor(threadFactory);
    }

    public static ScheduleTask getInstance() {
        //单例模式
        return ScheduleTask.Holder.INSTANCE;
    }

    public void schedule(Runnable runnable) {
        //提交一个事务，runnable
        this.mExecutors.execute(runnable);
    }

    private static class Holder {

        private static final ScheduleTask INSTANCE = new ScheduleTask();

        private Holder() {

        }
    }

}
