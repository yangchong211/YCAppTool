package com.yc.library.base.callback;


import com.yc.toolutils.system.AppLogUtils;

import com.yc.ycthreadpoollib.pool.callback.ThreadCallback;



/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/5/14
 *     desc  : 回调数据
 *     revise:
 * </pre>
 */
public class LogCallback implements ThreadCallback {

    private final String TAG = "LogCallback";

    @Override
    public void onError(String name, Throwable t) {
        AppLogUtils.e("LogCallback"+"------onError");
        AppLogUtils.e(TAG, String.format("[任务线程%s]/[回调线程%s]执行失败: %s", name, Thread.currentThread(), t.getMessage()));
    }

    @Override
    public void onCompleted(String name) {
        AppLogUtils.e("LogCallback"+"------onCompleted");
        AppLogUtils.e(TAG, String.format("[任务线程%s]/[回调线程%s]执行完毕：", name, Thread.currentThread()));
    }

    @Override
    public void onStart(String name) {
        AppLogUtils.e("LogCallback"+"------onStart");
        AppLogUtils.e(TAG, String.format("[任务线程%s]/[回调线程%s]执行开始：", name, Thread.currentThread()));
    }

}
