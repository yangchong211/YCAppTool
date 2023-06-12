package com.yc.anrtoollib.tool;

import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yc.anrtoollib.watch.ANRError;
import com.yc.anrtoollib.watch.ANRInterceptor;
import com.yc.anrtoollib.watch.ANRListener;
import com.yc.anrtoollib.watch.InterruptionListener;

public class ANRWatchDog extends Thread {

    /**
     * 默认ANR超时时间，这里默认时间是以activity为主的。
     */
    private static final int DEFAULT_ANR_TIMEOUT = 5000;

    private static final ANRListener DEFAULT_ANR_LISTENER = new ANRListener() {
        @Override
        public void onAppNotResponding(@NonNull ANRError error) {
            //处理方式简单粗暴哈，直接把ANRError丢出去，
            //这样APP就直接崩溃了
            throw error;
        }
    };

    private static final ANRInterceptor DEFAULT_ANR_INTERCEPTOR = new ANRInterceptor() {
        @Override
        public long intercept(long duration) {
            return 0;
        }
    };

    private static final InterruptionListener DEFAULT_INTERRUPTION_LISTENER = new InterruptionListener() {
        @Override
        public void onInterrupted(@NonNull InterruptedException exception) {
            Log.w("ANRWatchdog", "Interrupted: " + exception.getMessage());
        }
    };

    private ANRListener anrListener = DEFAULT_ANR_LISTENER;
    private ANRInterceptor anrInterceptor = DEFAULT_ANR_INTERCEPTOR;
    private InterruptionListener interruptionListener = DEFAULT_INTERRUPTION_LISTENER;

    /**
     * 绑定了主线程Looper的Handler
     */
    private final Handler uiHandler = new Handler(Looper.getMainLooper());
    private final int timeoutInterval;

    private String namePrefix = "";
    private boolean logThreadsWithoutStackTrace = false;
    private boolean ignoreDebugger = false;

    private volatile long tick = 0;
    private volatile boolean reported = false;

    private final Runnable ticker = new Runnable() {
        @Override public void run() {
            //_ticker中的run()，再一次将_tick置零；
            //只要_ticker不被处理，其run()便不会执行，_tick就不会被置零，
            tick = 0;
            reported = false;
        }
    };

    /**
     * Constructs a watchdog that checks the ui thread every {@value #DEFAULT_ANR_TIMEOUT} milliseconds
     */
    public ANRWatchDog() {
        this(DEFAULT_ANR_TIMEOUT);
    }

    /**
     * Constructs a watchdog that checks the ui thread every given interval
     *
     * @param timeoutInterval The interval, in milliseconds, between to checks of the UI thread.
     *                        It is therefore the maximum time the UI may freeze before being reported as ANR.
     */
    public ANRWatchDog(int timeoutInterval) {
        super();
        this.timeoutInterval = timeoutInterval;
    }

    /**
     * @return The interval the WatchDog
     */
    public int getTimeoutInterval() {
        return timeoutInterval;
    }

    /**
     * Sets an interface for when an ANR is detected.
     * If not set, the default behavior is to throw an error and crash the application.
     *
     * @param listener The new listener or null
     * @return itself for chaining.
     */
    @NonNull
    public ANRWatchDog setANRListener(@Nullable ANRListener listener) {
        if (listener == null) {
            //开发者不定制，则使用框架自带的默认处理方式
            //处理方式简单粗暴哈，直接把ANRError丢出去，
            //这样APP就直接崩溃了
            anrListener = DEFAULT_ANR_LISTENER;
        } else {
            anrListener = listener;
        }
        return this;
    }

    /**
     * Sets an interface to intercept ANRs before they are reported.
     * If set, you can define if, given the current duration of the detected ANR and external context, it is necessary to report the ANR.
     *
     * @param interceptor The new interceptor or null
     * @return itself for chaining.
     */
    @NonNull
    public ANRWatchDog setANRInterceptor(@Nullable ANRInterceptor interceptor) {
        if (interceptor == null) {
            anrInterceptor = DEFAULT_ANR_INTERCEPTOR;
        } else {
            anrInterceptor = interceptor;
        }
        return this;
    }

    /**
     * Sets an interface for when the watchdog thread is interrupted.
     * If not set, the default behavior is to just log the interruption message.
     *
     * @param listener The new listener or null.
     * @return itself for chaining.
     */
    @NonNull
    public ANRWatchDog setInterruptionListener(@Nullable InterruptionListener listener) {
        if (listener == null) {
            interruptionListener = DEFAULT_INTERRUPTION_LISTENER;
        } else {
            interruptionListener = listener;
        }
        return this;
    }

    /**
     * Set the prefix that a thread's name must have for the thread to be reported.
     * Note that the main thread is always reported.
     * Default "".
     *
     * @param prefix The thread name's prefix for a thread to be reported.
     * @return itself for chaining.
     */
    @NonNull
    public ANRWatchDog setReportThreadNamePrefix(@Nullable String prefix) {
        if (prefix == null) {
            prefix = "";
        }
        namePrefix = prefix;
        return this;
    }

    /**
     * Set that only the main thread will be reported.
     *
     * @return itself for chaining.
     */
    @NonNull
    public ANRWatchDog setReportMainThreadOnly() {
        namePrefix = null;
        return this;
    }

    /**
     * Set that all threads will be reported (default behaviour).
     *
     * @return itself for chaining.
     */
    @NonNull
    public ANRWatchDog setReportAllThreads() {
        namePrefix = "";
        return this;
    }

    /**
     * Set that all running threads will be reported,
     * even those from which no stack trace could be extracted.
     * Default false.
     *
     * @param logThreadsWithoutStackTrace Whether or not all running threads should be reported
     * @return itself for chaining.
     */
    @NonNull
    public ANRWatchDog setLogThreadsWithoutStackTrace(boolean logThreadsWithoutStackTrace) {
        this.logThreadsWithoutStackTrace = logThreadsWithoutStackTrace;
        return this;
    }

    /**
     * Set whether to ignore the debugger when detecting ANRs.
     * When ignoring the debugger, ANRWatchdog will detect ANRs even if the debugger is connected.
     * By default, it does not, to avoid interpreting debugging pauses as ANRs.
     * Default false.
     *
     * @param ignoreDebugger Whether to ignore the debugger.
     * @return itself for chaining.
     */
    @NonNull
    public ANRWatchDog setIgnoreDebugger(boolean ignoreDebugger) {
        this.ignoreDebugger = ignoreDebugger;
        return this;
    }

    @Override
    public synchronized void start() {
        super.start();
        Log.i("ANRWatchDog" , " start() ");
    }

    @Override
    public void run() {
        setName("|ANR-WatchDog|");
        long interval = timeoutInterval;
        while (!isInterrupted()) {
            //一开始就_tick为0的话说明_tick还没被post
            boolean needPost = tick == 0;
            //没有便将_tick=加上卡顿周期，之后post了_ticker
            tick += interval;
            if (needPost) {
                uiHandler.post(ticker);
            }

            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                interruptionListener.onInterrupted(e);
                return ;
            }

            //根据_tick的值可以判断_ticker是否被处理了；
            //_tick重新归零则主线程处理了_ticker，
            //_tick不为零则判定主线程卡顿，它没处理_tick
            if (tick != 0 && !reported) {
                //noinspection ConstantConditions
                if (!ignoreDebugger && (Debug.isDebuggerConnected() || Debug.waitingForDebugger())) {
                    Log.w("ANRWatchdog", "An ANR was detected but ignored " +
                            "because the debugger is connected " +
                            "(you can prevent this with setIgnoreDebugger(true))");
                    reported = true;
                    continue ;
                }

                interval = anrInterceptor.intercept(tick);
                if (interval > 0) {
                    continue;
                }

                final ANRError error;
                if (namePrefix != null) {
                    error = ANRError.New(tick, namePrefix, logThreadsWithoutStackTrace);
                } else {
                    error = ANRError.NewMainOnly(tick);
                }
                //通过_anrListener.onAppNotResponding(error);回调机制处理ANRError实例
                anrListener.onAppNotResponding(error);
                interval = timeoutInterval;
                reported = true;
            }
        }
    }

}
