package com.yc.logclient.client;

import android.os.Build;

import com.yc.logclient.bean.AppLogBean;
import com.yc.logclient.inter.OnFlushListener;
import com.yc.logclient.inter.OnEnqueueListener;
import com.yc.logclient.inter.ILogQueue;
import com.yc.logclient.inter.ILogSend;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * 职责描述: LogBean对队列实现。
 */
public class LogQueueThread extends Thread implements ILogQueue {

    private IPCLargeProcessor largeProcessor;
    private final ArrayDeque<AppLogBean> mLogQueue = new ArrayDeque<>();
    private final Object mLogLock = new Object();
    private boolean mLogRunning = true;
    private final LogCache mCache;
    private final ILogSend mSender;

    public LogQueueThread(ILogSend sender, LogCache cache) {
        mSender = sender;
        mCache = cache;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCache.setFlushListener(new OnFlushListener() {
                @Override
                public void doFlush(ArrayList<AppLogBean> logBeans) {
                    mSender.sendLogToService(logBeans);
                }
            });
        }
    }

    /**
     * 两个队列:mLogQeque 日志队列 和 mLogCacheBeans日志缓存队列.
     * mLogQeque 为操作TMLogBean的第一级队列
     * (1)线程启动时,当前队列取空时: 若当前日志没有缓存,启动LogService,然后wait(); 若当前日志有缓存，则写日志到LogService
     * (2)向mLogQeque快速添加日志时（快于LogThread处理能力），日志TMLogBean 会被缓存。添加TMLogBean变慢 或者停止添加MLogBean时，会flush到logService
     * (3)慢速添加MLogBean时，添加一个，就会flush一个 到LogService
     */
    @Override
    public void run() {
        while (mLogRunning) {
            synchronized (mLogLock) {
                AppLogBean logBean = mLogQueue.poll();
                //正常入队列的日志，首先会加入logCache缓存,缓存满了之后再发送到LogService
                //AppLogUtils.d("LogQueue.poll():"+logBean);
                if (logBean != null) {
                    mCache.put(logBean);
                } else {
                    //队列空闲时,尝试发送日志到LogService
                    try {
                        mCache.doFlush();
                        mLogLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        mLogRunning = false;
                    }
                }
            }
        }
    }

    @Override
    public void put(AppLogBean bean) {
        if (largeProcessor != null) {
            largeProcessor.enqueue(bean, new OnEnqueueListener() {
                @Override
                public void doQueue(AppLogBean bean) {
                    innerPut(bean);
                }
            });
        } else {
            innerPut(bean);
        }
    }

    /**
     * 具体的入队操作
     *
     * @param bean
     */
    public void innerPut(AppLogBean bean) {
        synchronized (mLogLock) {
            mLogQueue.add(bean);
            mLogLock.notifyAll();
        }
    }

    public void setLargeProcessor(IPCLargeProcessor processor) {
        largeProcessor = processor;
    }

    public void flushCache() {
        mCache.doFlush();
    }

}
