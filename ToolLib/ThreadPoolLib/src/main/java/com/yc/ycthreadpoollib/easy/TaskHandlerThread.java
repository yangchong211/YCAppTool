
package com.yc.ycthreadpoollib.easy;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import java.util.concurrent.ConcurrentHashMap;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2019/5/11
 *     desc   : 抽象task任务类
 *     revise :
 *     GitHub : https://github.com/yangchong211/YCThreadPool
 * </pre>
 */
public final class TaskHandlerThread extends HandlerThread {

    private static final ConcurrentHashMap<String,TaskHandlerThread> S_HASH_MAP
            = new ConcurrentHashMap<>();

    private TaskHandlerThread(String threadName) {
        super(threadName, android.os.Process.THREAD_PRIORITY_BACKGROUND);
    }

    private static void ensureThreadLocked(String threadName) {
        TaskHandlerThread taskHandlerThread = S_HASH_MAP.get(threadName);
        if (taskHandlerThread == null) {
            synchronized (TaskHandlerThread.class) {
                //创建一个HandlerThread对象
                taskHandlerThread = new TaskHandlerThread(threadName);
                taskHandlerThread.start();
                //存储数据
                S_HASH_MAP.put(threadName,taskHandlerThread);
            }
        }
    }

    public static TaskHandlerThread get(String threadName) {
        synchronized (TaskHandlerThread.class) {
            ensureThreadLocked(threadName);
            return S_HASH_MAP.get(threadName);
        }
    }

    public Handler getHandler(String threadName) {
        TaskHandlerThread taskHandlerThread = S_HASH_MAP.get(threadName);
        Handler handler;
        if (taskHandlerThread != null){
            //获取该thread的独有looper对象
            final Looper looper = taskHandlerThread.getLooper();
            handler = new SafeHandler(looper);
        } else {
            //普通的handler
            handler = new SafeHandler();
        }
        return handler;
    }
}
