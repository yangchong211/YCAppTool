package com.yc.taskscheduler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;


public class SafeHandler extends Handler {

    private static final String TAG = "SafeSchedulerHandler";

    SafeHandler(Looper looper) {
        super(looper);
    }

    SafeHandler() {
        super();
    }

    @Override
    public void dispatchMessage(Message msg) {
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
