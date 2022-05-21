package com.yc.longalive;


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
public class LongAliveScreenReceiver extends BroadcastReceiver {

    private static final String TAG = "LongevityScreenReceiver";
    private int mLastScreenType = -1;

    public LongAliveScreenReceiver() {
        this.log("LongevityScreenReceiver");
    }

    public static void register(Context context) {
        if (context != null) {
            LongAliveScreenReceiver receiver = new LongAliveScreenReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_USER_PRESENT);
            context.registerReceiver(receiver, filter);
            LongAliveMonitor.sLogger.log("LongevityScreenReceiver is register" );
        }
    }

    @Override
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
                        // 锁屏
                        this.mLastScreenType = 2;
                        this.log("onReceive screen off");
                        LongAliveMonitor.sCurrentScreenState = LongAliveConstant.LONGEVITY_SCREEN_STATE_OFF;
                    }
                } else {
                    // 开屏
                    this.mLastScreenType = 1;
                    this.log("onReceive screen on");
                    LongAliveMonitor.sCurrentScreenState = LongAliveConstant.LONGEVITY_SCREEN_STATE_ON;
                }
            }
        }
    }

    private void log(String log) {
        if (LongAliveMonitor.sLogger != null) {
            LongAliveMonitor.sLogger.log(log);
        }

    }
}

