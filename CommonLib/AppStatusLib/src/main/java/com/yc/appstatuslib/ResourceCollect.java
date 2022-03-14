package com.yc.appstatuslib;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Build.VERSION;

import com.yc.appstatuslib.info.CollectionInfo;
import com.yc.appstatuslib.listener.TraceLogListener;
import com.yc.appstatuslib.log.CsvFormatLog;
import com.yc.appstatuslib.log.FormatStrategy;

import java.io.File;

class ResourceCollect {

    private Handler mHandler;
    private final AppStatusManager mResourceManager;
    private HandlerThread mHandlerThread;
    private final FormatStrategy mFormatStrategy;
    private final int mInterval;
    private final TraceLogListener mTraceLog;
    private static final int MESSAGE_WHAT = 520;

    ResourceCollect(ResourceCollect.Builder builder) {
        mResourceManager = builder.manager;
        mFormatStrategy = builder.formatStrategy;
        mTraceLog = builder.traceLog;
        mInterval = builder.interval;
    }

    void init() {
        mHandlerThread = new HandlerThread("resource-collect-thread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()) {
            public void handleMessage(Message msg) {
                if (msg!=null && msg.what == MESSAGE_WHAT){
                    ResourceCollect.this.collection();
                }
            }
        };
        Message message = mHandler.obtainMessage();
        message.what = MESSAGE_WHAT;
        mHandler.sendMessageDelayed(message, (long)mInterval);
    }

    private void collection() {
        mHandler.removeCallbacksAndMessages((Object)null);
        CollectionInfo collectionInfo = CollectionInfo.builder(
                mResourceManager.getBatteryInfo(), mResourceManager.getAppStatus());
        mFormatStrategy.log(collectionInfo);
        if (mTraceLog != null) {
            mTraceLog.log(collectionInfo);
        }
        Message message = mHandler.obtainMessage();
        message.what = MESSAGE_WHAT;
        mHandler.sendMessageDelayed(message, (long)mInterval);
    }

    void sendCollectionMsg() {
        mHandler.sendEmptyMessage(MESSAGE_WHAT);
    }

    void destroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages((Object)null);
        }
        if (mHandlerThread != null) {
            if (VERSION.SDK_INT >= 18) {
                mHandlerThread.quitSafely();
            } else {
                mHandlerThread.quit();
            }
        }
    }

    static class Builder {
        private static final int ONE_MINUTE = 60000;
        int interval;
        AppStatusManager manager;
        FormatStrategy formatStrategy;
        File file;
        TraceLogListener traceLog;

        Builder() {
        }

        ResourceCollect.Builder interval(int interval) {
            this.interval = interval;
            return this;
        }

        ResourceCollect.Builder file(File file) {
            this.file = file;
            return this;
        }

        ResourceCollect.Builder manager(AppStatusManager manager) {
            this.manager = manager;
            return this;
        }

        ResourceCollect.Builder formatStrategy(FormatStrategy strategy) {
            this.formatStrategy = strategy;
            return this;
        }

        ResourceCollect.Builder traceLog(TraceLogListener trace) {
            this.traceLog = trace;
            return this;
        }

        ResourceCollect builder() {
            if (interval == 0) {
                interval = 60000;
            }
            if (formatStrategy == null) {
                formatStrategy = new CsvFormatLog(file);
            }
            return new ResourceCollect(this);
        }
    }
}

