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
    private AppStatusManager mResourceManager;
    private HandlerThread mHandlerThread;
    private FormatStrategy mFormatStrategy;
    private int mInterval;
    private TraceLogListener mTraceLog;
    private static final int MESSAGE_WHAT = 520;

    ResourceCollect(ResourceCollect.Builder builder) {
        this.mResourceManager = builder.manager;
        this.mFormatStrategy = builder.formatStrategy;
        this.mTraceLog = builder.traceLog;
        this.mInterval = builder.interval;
    }

    void init() {
        this.mHandlerThread = new HandlerThread("resource-collect-thread");
        this.mHandlerThread.start();
        this.mHandler = new Handler(this.mHandlerThread.getLooper()) {
            public void handleMessage(Message msg) {
                if (msg!=null && msg.what == MESSAGE_WHAT){
                    ResourceCollect.this.collection();
                }
            }
        };
        Message message = this.mHandler.obtainMessage();
        message.what = MESSAGE_WHAT;
        this.mHandler.sendMessageDelayed(message, (long)this.mInterval);
    }

    private void collection() {
        this.mHandler.removeCallbacksAndMessages((Object)null);
        CollectionInfo collectionInfo = CollectionInfo.builder(
                this.mResourceManager.getBatteryInfo(), this.mResourceManager.getAppStatus());
        this.mFormatStrategy.log(collectionInfo);
        if (this.mTraceLog != null) {
            this.mTraceLog.log(collectionInfo);
        }
        Message message = this.mHandler.obtainMessage();
        message.what = MESSAGE_WHAT;
        this.mHandler.sendMessageDelayed(message, (long)this.mInterval);
    }

    void sendCollectionMsg() {
        this.mHandler.sendEmptyMessage(MESSAGE_WHAT);
    }

    void destroy() {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object)null);
        }
        if (this.mHandlerThread != null) {
            if (VERSION.SDK_INT >= 18) {
                this.mHandlerThread.quitSafely();
            } else {
                this.mHandlerThread.quit();
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
            if (this.interval == 0) {
                this.interval = 60000;
            }

            if (this.formatStrategy == null) {
                this.formatStrategy = new CsvFormatLog(this.file);
            }

            return new ResourceCollect(this);
        }
    }
}

