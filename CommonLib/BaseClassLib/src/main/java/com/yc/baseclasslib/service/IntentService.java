package com.yc.baseclasslib.service;

import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time  : 2018/11/9
 *     desc  : 父类Service，没有界面的后台服务
 *     revise:
 * </pre>
 */
public abstract class IntentService extends BaseService {

    private volatile Looper mServiceLooper;
    private volatile ServiceHandler mServiceHandler;
    private final String mName;
    private boolean mRedelivery;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg != null) {
                try {
                    onHandleIntent((Intent) msg.obj);
                } finally {
                    //fix bug，保证stop一定一会执行
                    stopSelf(msg.arg1);
                }
            }
        }
    }

    /**
     * 创建一个IntentService。由子类的构造函数调用
     *
     * @param name 用于命名工作线程，仅在调试时
     */
    public IntentService(String name) {
        super();
        mName = name;
    }

    /**
     * 设置意图重传首选项。通常使用首选语义从构造函数调用。
     */
    public void setIntentRedelivery(boolean enabled) {
        mRedelivery = enabled;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread("IntentService[" + mName + "]");
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        msg.obj = intent;
        mServiceHandler.sendMessage(msg);
    }

    /**
     * 你不应该为你的IntentService重写这个方法。相反，覆盖{@link #onHandleIntent}，
     * 当IntentService接收到启动请求时，系统会调用它。
     *
     * @see android.app.Service#onStartCommand
     */
    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        onStart(intent, startId);
        return mRedelivery ? START_REDELIVER_INTENT : START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        mServiceLooper.quit();
    }

    /**
     * 除非为服务提供绑定，否则不需要实现此方法，因为默认实现返回null。
     *
     * @see android.app.Service#onBind
     */
    @Override
    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 这个方法是在工作线程上调用的，有一个要处理的请求。
     * 一次只处理一个Intent，但是这个处理发生在独立于其他应用程序逻辑运行的工作线程上。
     * 如果这段代码花了很长时间，它会阻止其他请求到同一个IntentService，但它不会阻止其他任何请求。
     * 当所有请求都被处理后，IntentService会停止自己，所以你不应该调用{@link #stopSelf}。
     *
     * @param intent intent意图
     */
    @WorkerThread
    protected abstract void onHandleIntent(@Nullable Intent intent);
}

