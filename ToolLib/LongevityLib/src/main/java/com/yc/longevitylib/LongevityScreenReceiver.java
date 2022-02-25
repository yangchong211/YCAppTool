package com.yc.longevitylib;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/01/30
 *     desc  : 广播
 *     revise:
 * </pre>
 */
public class LongevityScreenReceiver extends BroadcastReceiver {

    private static final String TAG = "LongevityScreenReceiver";
    private int mLastScreenType = -1;

    public LongevityScreenReceiver() {
        this.log("LongevityScreenReceiver" );
    }

    public static void register(Context context) {
        if (context != null) {
            LongevityScreenReceiver receiver = new LongevityScreenReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.intent.action.SCREEN_ON");
            filter.addAction("android.intent.action.SCREEN_OFF");
            filter.addAction("android.intent.action.USER_PRESENT");
            context.registerReceiver(receiver, filter);
            LongevityMonitor.sLogger.log("LongevityScreenReceiver is register" );
        }
    }

    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            this.log("onReceive intent null");
        } else {
            this.log("onReceive " + intent.toString() + ", lastScreenType " + this.mLastScreenType);
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                this.log("onReceive action null");
            } else {
                if (!"android.intent.action.SCREEN_ON".equals(action)
                        && !"android.intent.action.USER_PRESENT".equals(action)) {
                    if ("android.intent.action.SCREEN_OFF".equals(action)) {
                        if (this.mLastScreenType == 2) {
                            return;
                        }

                        this.mLastScreenType = 2;
                        this.log("onReceive screen off");
                        LongevityMonitor.sCurrentScreenState = 2;
                    }
                } else {
                    this.mLastScreenType = 1;
                    this.log("onReceive screen on");
                    LongevityMonitor.sCurrentScreenState = 1;
                }
            }
        }
    }

    private void log(String log) {
        if (LongevityMonitor.sLogger != null) {
            LongevityMonitor.sLogger.log(log);
        }

    }
}

