package com.yc.looperthread.looper;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.yc.looperthread.abs.AbsPollingThread;

/**
 * 使用Handler不断发送消息来实现轮训
 */
public class HandlerLoopThread extends AbsPollingThread {

    private static final int MSG_GET_COMPARE_RESULT = 520;
    private Handler handler;

    @Override
    public void release() {
        super.release();
        if (handler != null){
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    @Override
    public void beginLoop() {
        super.beginLoop();
        handler.removeMessages(MSG_GET_COMPARE_RESULT);
        handler.sendEmptyMessageDelayed(MSG_GET_COMPARE_RESULT, 0);
    }

    @Override
    public void startPolling() {
        HandlerThread handlerThread = new HandlerThread("LooperThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MSG_GET_COMPARE_RESULT) {
                    // 轮询的代码
                    if (beginRead && isLoop()) {
                        doAction();
                        //向MessageQueue中添加延时消息，后重复执行以上任务
                        handler.sendEmptyMessageDelayed(MSG_GET_COMPARE_RESULT, getSleepTime());
                    }
                }
            }
        };
    }

    @Override
    public void doAction() {
        Log.v(TAG, "handler doAction-> " + count.getAndIncrement());
    }
}
