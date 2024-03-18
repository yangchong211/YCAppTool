package com.yc.ycthreadpoollib.easy;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2019/5/11
 *     desc   : 安全版本的handler
 *     revise :
 *     GitHub : https://github.com/yangchong211/YCThreadPool
 * </pre>
 */
public final class SafeHandler extends Handler {

    private static final String TAG = "SafeHandler";

    public SafeHandler(Looper looper) {
        super(looper);
    }

    public SafeHandler() {
        //默认是主线程
        super(Looper.getMainLooper());
    }

    @Override
    public void dispatchMessage(@Nullable Message msg) {
        if (msg == null){
            Log.d(TAG, "msg is null , return");
            return;
        }
        try {
            super.dispatchMessage(msg);
        } catch (Exception e) {
            Log.d(TAG, "dispatchMessage Exception " + msg + " , " + e);
        } catch (Error error) {
            Log.d(TAG, "dispatchMessage error " + msg + " , " + error);
        }
    }
}
