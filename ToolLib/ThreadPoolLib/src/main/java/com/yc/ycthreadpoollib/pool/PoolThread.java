/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.yc.ycthreadpoollib.pool;


import androidx.annotation.NonNull;

import com.yc.ycthreadpoollib.pool.builder.CachedBuilderImpl;
import com.yc.ycthreadpoollib.pool.builder.FixedBuilderImpl;
import com.yc.ycthreadpoollib.pool.builder.ScheduledBuilderImpl;
import com.yc.ycthreadpoollib.pool.builder.SingleBuilderImpl;
import com.yc.ycthreadpoollib.pool.callback.AsyncCallback;
import com.yc.ycthreadpoollib.pool.callback.ThreadCallback;
import com.yc.ycthreadpoollib.pool.config.ThreadConfigs;
import com.yc.ycthreadpoollib.pool.deliver.AndroidDeliver;
import com.yc.ycthreadpoollib.pool.deliver.JavaDeliver;
import com.yc.ycthreadpoollib.pool.factory.MyThreadFactory;
import com.yc.ycthreadpoollib.pool.utils.DelayTaskExecutor;
import com.yc.ycthreadpoollib.pool.utils.ThreadToolUtils;
import com.yc.ycthreadpoollib.pool.wrapper.CallableWrapper;
import com.yc.ycthreadpoollib.pool.wrapper.RunnableWrapper;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://www.jianshu.com/p/53017c3fc75d
 *     time  : 2017/08/22
 *     desc  : 线程池
 *     revise:
 * </pre>
 */
public final class PoolThread implements Executor {

    /**
     * 线程池
     */
    private final ExecutorService pool;
    /**
     * 默认线程名字
     */
    private final String defName;
    /**
     * 默认线程回调
     */
    private final ThreadCallback defCallback;
    /**
     * 默认线程传递
     */
    private final Executor defDeliver;
    /**
     * 确保多线程配置没有冲突
     * ThreadLocal是线程内部的数据存储类，通过它可以在指定线程中存储数据，其他线程则无法获取到
     */
    private ThreadLocal<ThreadConfigs> threadLocal;

    /**
     * 构造对象
     * @param type
     * @param size
     * @param priority
     * @param name
     * @param callback
     * @param deliver
     * @param pool
     */
    private PoolThread(int type, int size, int priority, String name, ThreadCallback callback,
                       Executor deliver, ExecutorService pool) {
        if (pool == null) {
            //创建线程池
            pool = createPool(type, size, priority);
        }
        this.pool = pool;
        this.defName = name;
        this.defCallback = callback;
        this.defDeliver = deliver;
        this.threadLocal = new ThreadLocal<>();
    }


    /**
     * 为当前的任务设置线程名。
     *
     * @param name 线程名字
     * @return PoolThread
     */
    public PoolThread setName(String name) {
        getLocalConfigs().name = name;
        return this;
    }


    /**
     * 设置当前任务的线程回调，如果未设置，则应使用默认回调。
     *
     * @param callback 线程回调
     * @return PoolThread
     */
    public PoolThread setCallback(ThreadCallback callback) {
        getLocalConfigs().callback = callback;
        return this;
    }

    /**
     * 设置当前任务的延迟时间.
     * 只有当您的线程池创建时，它才会产生效果。
     *
     * @param time 时长
     * @param unit time unit
     * @return PoolThread
     */
    public PoolThread setDelay(long time, TimeUnit unit) {
        long delay = unit.toMillis(time);
        getLocalConfigs().delay = Math.max(0, delay);
        return this;
    }

    /**
     * 设置当前任务的线程传递。如果未设置，则应使用默认传递。
     *
     * @param deliver thread deliver
     * @return PoolThread
     */
    public PoolThread setDeliver(Executor deliver) {
        getLocalConfigs().deliver = deliver;
        return this;
    }


    /**
     * 启动任务
     * 这个是实现接口Executor中的execute方法
     * 在将来的某个时间执行给定的命令。该命令可以在新线程、池线程或调用线程中执行，这取决于{@code Executor}实现。
     * 提交任务无返回值
     * 当我们使用execute来提交任务时，由于execute方法没有返回值，所以说我们也就无法判定任务是否被线程池执行成功。
     *
     * @param runnable task，注意添加非空注解
     */
    @Override
    public void execute(@NonNull Runnable runnable) {
        //获取线程thread配置信息
        ThreadConfigs configs = getLocalConfigs();
        if (configs.deliver == null) {
            if (ThreadToolUtils.isIsAndroid()) {
                configs.deliver = AndroidDeliver.getInstance();
            } else {
                configs.deliver = JavaDeliver.getInstance();
            }
        }
        //设置runnable任务
        runnable = new RunnableWrapper(configs).setRunnable(runnable);
        //启动任务
        DelayTaskExecutor.get().postDelay(configs.delay, pool, runnable);
        //重置线程Thread配置
        resetLocalConfigs();
    }


    /**
     * 启动异步任务，回调用于接收可调用任务的结果。
     *
     * @param callable callable
     * @param callback callback
     * @param <T>      type
     */
    public <T> void async(@NonNull Callable<T> callable, AsyncCallback<T> callback) {
        ThreadConfigs configs = getLocalConfigs();
        configs.asyncCallback = callback;
        Runnable runnable = new RunnableWrapper(configs).setCallable(callable);
        DelayTaskExecutor delayTaskExecutor = DelayTaskExecutor.get();
        delayTaskExecutor.postDelay(configs.delay, pool, runnable);
        resetLocalConfigs();
    }

    /**
     * 发射任务
     * 提交任务有返回值
     * 当我们使用submit来提交任务时,它会返回一个future,我们就可以通过这个future来判断任务是否执行成功，
     * 还可以通过future的get方法来获取返回值。如果子线程任务没有完成，get方法会阻塞住直到任务完成，
     * 而使用get(long timeout, TimeUnit unit)方法则会阻塞一段时间后立即返回，这时候有可能任务并没有执行完。
     *
     * @param callable callable
     * @param <T>      type
     * @return {@link Future}
     */
    public <T> Future<T> submit(Callable<T> callable) {
        //把外部传来的callable封装到CallableWrapper对象中
        callable = new CallableWrapper<>(getLocalConfigs(), callable);
        //提交事物
        Future<T> result = pool.submit(callable);
        resetLocalConfigs();
        return result;
    }


    /**
     * 关闭线程池操作
     * shutdown原理：将线程池状态设置成SHUTDOWN状态，然后中断所有没有正在执行任务的线程。
     * shutdownNow原理：将线程池的状态设置成STOP状态，然后中断所有任务(包括正在执行的)的线程，并返回等待执行任务的列表。
     */
    public void stop() {
        try {
            // shutdown只是起到通知的作用
            // 只调用shutdown方法结束线程池是不够的
            pool.shutdown();
            // (所有的任务都结束的时候，返回TRUE)
            if (!pool.awaitTermination(0, TimeUnit.MILLISECONDS)) {
                // 超时的时候向线程池中所有的线程发出中断(interrupted)。
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。
            e.printStackTrace();
        } finally {
            pool.shutdownNow();
            //close();
        }
    }

    /**
     * 获取要创建的线程池。
     *
     * @return 线程池
     */
    public ExecutorService getExecutor() {
        return pool;
    }


    /**
     * 判断线程池是否已关闭
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public boolean isShutDown() {
        //判断线程池是否已关闭
        return pool.isShutdown();
    }

    /**
     * 销毁的时候可以调用这个方法
     */
    public void close() {
        if (threadLocal != null) {
            threadLocal.remove();
            threadLocal = null;
        }
    }

    /**
     * 创建线程池，目前支持以下四种
     *
     * @param type     类型
     * @param size     数量size
     * @param priority 优先级
     * @return
     */
    private synchronized ExecutorService createPool(int type, int size, int priority) {
        MyThreadFactory myThreadFactory = new MyThreadFactory(priority);
        switch (type) {
            case ThreadBuilder.TYPE_CACHE:
                //它是一个数量无限多的线程池，都是非核心线程，适合执行大量耗时小的任务
                return new CachedBuilderImpl().builder(myThreadFactory);
            case ThreadBuilder.TYPE_FIXED:
                //线程数量固定的线程池，全部为核心线程，响应较快，不用担心线程会被回收。
                return new FixedBuilderImpl().setSize(size).builder(myThreadFactory);
            case ThreadBuilder.TYPE_SCHEDULED:
                //有数量固定的核心线程，且有数量无限多的非核心线程，适合用于执行定时任务和固定周期的重复任务
                return new ScheduledBuilderImpl().setSize(size).builder(myThreadFactory);
            case ThreadBuilder.TYPE_SINGLE:
            default:
                //内部只有一个核心线程，所有任务进来都要排队按顺序执行
                return new SingleBuilderImpl().builder(myThreadFactory);
        }
    }


    /**
     * 当启动任务或者发射任务之后需要调用该方法
     * 重置本地配置，置null
     */
    private synchronized void resetLocalConfigs() {
        threadLocal.set(null);
    }


    /**
     * 注意需要用synchronized修饰，解决了多线程的安全问题
     * 获取本地配置参数
     *
     * @return
     */
    private synchronized ThreadConfigs getLocalConfigs() {
        ThreadConfigs configs = threadLocal.get();
        if (configs == null) {
            configs = new ThreadConfigs();
            configs.name = defName;
            configs.callback = defCallback;
            configs.deliver = defDeliver;
            threadLocal.set(configs);
        }
        return configs;
    }


    public static class ThreadBuilder {

        final static int TYPE_CACHE = 0;
        final static int TYPE_FIXED = 1;
        final static int TYPE_SINGLE = 2;
        final static int TYPE_SCHEDULED = 3;

        int type;
        int size;
        int priority = Thread.NORM_PRIORITY;
        String name;
        ThreadCallback callback;
        Executor deliver;
        ExecutorService pool;

        private ThreadBuilder(int size, int type, ExecutorService pool) {
            this.size = Math.max(1, size);
            this.type = type;
            this.pool = pool;
        }

        /**
         * 通过Executors.newSingleThreadExecutor()创建线程池
         * 内部只有一个核心线程，所有任务进来都要排队按顺序执行
         */
        public static ThreadBuilder create(ExecutorService pool) {
            return new ThreadBuilder(1, TYPE_SINGLE, pool);
        }

        /**
         * 通过Executors.newCachedThreadPool()创建线程池
         * 它是一个数量无限多的线程池，都是非核心线程，适合执行大量耗时小的任务
         */
        public static ThreadBuilder createCacheable() {
            return new ThreadBuilder(0, TYPE_CACHE, null);
        }

        /**
         * 通过Executors.newFixedThreadPool()创建线程池
         * 线程数量固定的线程池，全部为核心线程，响应较快，不用担心线程会被回收。
         */
        public static ThreadBuilder createFixed(int size) {
            return new ThreadBuilder(size, TYPE_FIXED, null);
        }

        /**
         * 通过Executors.newScheduledThreadPool()创建线程池
         * 有数量固定的核心线程，且有数量无限多的非核心线程，适合用于执行定时任务和固定周期的重复任务
         */
        public static ThreadBuilder createScheduled(int size) {
            return new ThreadBuilder(size, TYPE_SCHEDULED, null);
        }

        /**
         * 通过Executors.newSingleThreadPool()创建线程池
         * 内部只有一个核心线程，所有任务进来都要排队按顺序执行
         * 和create区别是size数量
         */
        public static ThreadBuilder createSingle() {
            return new ThreadBuilder(0, TYPE_SINGLE, null);
        }

        /**
         * 将默认线程名设置为“已使用”。
         */
        public ThreadBuilder setName(@NonNull String name) {
            if (name.length() > 0) {
                this.name = name;
            }
            return this;
        }

        /**
         * 将默认线程优先级设置为“已使用”。
         */
        public ThreadBuilder setPriority(int priority) {
            this.priority = priority;
            return this;
        }

        /**
         * 将默认线程回调设置为“已使用”。
         */
        public ThreadBuilder setCallback(ThreadCallback callback) {
            this.callback = callback;
            return this;
        }

        /**
         * 设置默认线程交付使用
         */
        public ThreadBuilder setDeliver(Executor deliver) {
            this.deliver = deliver;
            return this;
        }

        /**
         * 创建用于某些配置的线程管理器。
         *
         * @return 对象
         */
        public PoolThread build() {
            //最大值
            priority = Math.max(Thread.MIN_PRIORITY, priority);
            //最小值
            priority = Math.min(Thread.MAX_PRIORITY, priority);

            size = Math.max(1, size);
            if (name == null || name.length() == 0) {
                // 如果没有设置名字，那么就使用下面默认的线程名称
                switch (type) {
                    case TYPE_CACHE:
                        name = "CACHE";
                        break;
                    case TYPE_FIXED:
                        name = "FIXED";
                        break;
                    case TYPE_SINGLE:
                        name = "SINGLE";
                        break;
                    default:
                        name = "POOL_THREAD";
                        break;
                }
            }

            if (deliver == null) {
                if (ThreadToolUtils.isIsAndroid()) {
                    deliver = AndroidDeliver.getInstance();
                } else {
                    deliver = JavaDeliver.getInstance();
                }
            }
            return new PoolThread(type, size, priority, name, callback, deliver, pool);
        }
    }
}
