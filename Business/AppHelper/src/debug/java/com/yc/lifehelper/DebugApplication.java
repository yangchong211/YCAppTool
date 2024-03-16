package com.yc.lifehelper;

import android.app.Application;
import android.util.Log;

import com.yc.ycthreadpoollib.debug.IThreadChangedCallback;
import com.yc.ycthreadpoollib.debug.IThreadDebugger;
import com.yc.ycthreadpoollib.debug.ThreadDebugger;
import com.yc.ycthreadpoollib.debug.ThreadDebuggers;

public class DebugApplication extends Application {

    private static final String TAG = "DebugApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        ThreadDebugger.NEED_PRINT_COST = true;

        IThreadDebugger threadDebugger = ThreadDebuggers.createWithCommonThreadKey()
                .add("IO", "IO")
                .add("computation", "computation")
                .add("network", "network")
                .add("test1", "test1")
                .add("test2", "test2")
                .add("test3", "test3")
                .add("test4", "test4");
        IThreadChangedCallback threadChangedCallback = new IThreadChangedCallback() {
            @Override
            public void onChanged(IThreadDebugger debugger) {
                // callback this method when the threads in this application has changed.
                Log.d(TAG, debugger.drawUpEachThreadInfoDiff());
                Log.d(TAG, debugger.drawUpEachThreadSizeDiff());
                Log.d(TAG, debugger.drawUpEachThreadInfo());
                Log.d(TAG, debugger.drawUpEachThreadSize());
                Log.d(TAG, debugger.drawUpUnknownInfo());
            }
        };
        ThreadDebugger.install(threadDebugger, 1000, threadChangedCallback);
    }
}
