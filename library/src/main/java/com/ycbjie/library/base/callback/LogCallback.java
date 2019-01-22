package com.ycbjie.library.base.callback;

import com.blankj.utilcode.util.LogUtils;

import cn.ycbjie.ycthreadpoollib.callback.ThreadCallback;



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
        LogUtils.e("LogCallback"+"------onError");
        LogUtils.e(TAG, String.format("[任务线程%s]/[回调线程%s]执行失败: %s", name, Thread.currentThread(), t.getMessage()), t);
    }

    @Override
    public void onCompleted(String name) {
        LogUtils.e("LogCallback"+"------onCompleted");
        LogUtils.e(TAG, String.format("[任务线程%s]/[回调线程%s]执行完毕：", name, Thread.currentThread()));
    }

    @Override
    public void onStart(String name) {
        LogUtils.e("LogCallback"+"------onStart");
        LogUtils.e(TAG, String.format("[任务线程%s]/[回调线程%s]执行开始：", name, Thread.currentThread()));
    }

}
