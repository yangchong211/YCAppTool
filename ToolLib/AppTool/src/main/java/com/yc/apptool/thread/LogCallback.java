package com.yc.apptool.thread;

import android.util.Log;

import cn.ycbjie.ycthreadpoollib.callback.ThreadCallback;


/**
 * <pre>
 *     @author: yangchong
 *     blog  : www.pedaily.cn
 *     time  : 2017/08/22
 *     desc  : 回调数据
 *     revise:
 * </pre>
 */
public class LogCallback implements ThreadCallback {

    private final String TAG = "LogCallback";

    @Override
    public void onError(String name, Throwable t) {
        Log.e(TAG, "LogCallback"+"------onError"+"-----"+name+"----"+Thread.currentThread()+"----"+t.getMessage());
    }

    @Override
    public void onCompleted(String name) {
        Log.e(TAG, "LogCallback"+"------onCompleted"+"-----"+name+"----"+Thread.currentThread());
    }

    @Override
    public void onStart(String name) {
        Log.e(TAG, "LogCallback"+"------onStart"+"-----"+name+"----"+Thread.currentThread());
    }
}
