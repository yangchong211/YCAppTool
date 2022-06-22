package com.yc.appstatuslib.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yc.appstatuslib.AppStatusManager;

/**
 * <pre>
 *     @author: yangchong
 *     email  : yangchong211@163.com
 *     time   : 2017/5/18
 *     desc   : 屏幕监听广播
 *     revise :
 * </pre>
 */
public class ScreenBroadcastReceiver extends BroadcastReceiver {

    private AppStatusManager mManager;

    public ScreenBroadcastReceiver(AppStatusManager resourceManager) {
        this.mManager = resourceManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (this.mManager != null) {
            String action = intent.getAction();
            if ("android.intent.action.SCREEN_ON".equals(action)) {
                this.notifyScreenSwitchState(true);
            } else if ("android.intent.action.SCREEN_OFF".equals(action)) {
                this.notifyScreenSwitchState(false);
            } else if ("android.intent.action.USER_PRESENT".equals(action)) {
                this.mManager.dispatcherUserPresent();
            }
        }
    }

    private void notifyScreenSwitchState(boolean state) {
        if (this.mManager != null) {
            this.mManager.dispatcherScreenState(state);
        }
    }
}
