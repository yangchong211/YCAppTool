package com.yc.looperthread.looper;

import android.util.Log;

import com.yc.looperthread.abs.IDoAction;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 使用Thread.sleep实现轮询。一直做while循环
 */
public class ThreadLoopThread extends Thread implements IDoAction {

    private static final String TAG = ThreadLoopThread.class.getSimpleName();
    private final AtomicLong count = new AtomicLong(0);
    private volatile boolean beginRead = false;
    private final AtomicBoolean isStart = new AtomicBoolean(false);

    @Override
    public void run() {
        super.run();
        //判断是否是非中断
        while (!interrupted()) {
            //如果已经开始并且支持循环，则执行循环体逻辑
            while (beginRead && isLoop()) {
                try {
                    doAction();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        //线程轮训的方式中延时操作阻塞了线程。
                        //思考：线程阻塞延迟有几种方式？第一种：sleep；第二种：wait；第三种：无
                        Thread.sleep(getSleepTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 启动线程
     */
    @Override
    public void startThread() {
        //防止多次启动
        if (!isStart.get()) {
            Log.v(TAG, "startThread");
            //线程只能启动一次，如果启动多次则会抛出异常
            start();
            isStart.set(true);
        }
    }


    /**
     * 开始循环
     */
    @Override
    public void beginLoop() {
        Log.v(TAG, "beginLoop");
        //每次开启循环的时候都要检测一下线程是否需要跑起来
        startThread();
        beginRead = true;
    }

    /**
     * 暂停循环
     */
    @Override
    public void endLoop() {
        Log.v(TAG, "endRead");
        beginRead = false;
    }

    /**
     * 释放线程。如果线程释放了，则没有办法再次start，除非是创建新的线程
     */
    @Override
    public void release() {
        Log.v(TAG, "release");
        beginRead = false;
        //中断可以理解为线程的一个标志位，它表示了一个运行中的线程是否被其他线程进行了中断操作。
        interrupt();
        //使用stop方法强行终止线程（不推荐使用，可能发生不可预料的结果）
        //stop();
    }

    @Override
    public void doAction() {
        Log.v(TAG, "doAction-> " + count.getAndIncrement());
    }

    /**
     * 默认条件是true
     *
     * @return 布尔值
     */
    public boolean isLoop() {
        return true;
    }

    /**
     * 默认睡眠时间是100毫秒，子类可以重写该方法
     *
     * @return 时间
     */
    public long getSleepTime() {
        return 100;
    }

}
