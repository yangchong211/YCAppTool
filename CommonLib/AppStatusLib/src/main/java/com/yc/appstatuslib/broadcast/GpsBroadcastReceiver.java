package com.yc.appstatuslib.broadcast;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings.System;
import android.text.TextUtils;

import com.yc.appstatuslib.AppStatusManager;
import com.yc.baseclasslib.receiver.BaseReceiver;


/**
 * <pre>
 *     @author: yangchong
 *     email  : yangchong211@163.com
 *     time   : 2017/5/18
 *     desc   : GPS监听广播
 *     revise :
 * </pre>
 */
public final class GpsBroadcastReceiver extends BaseReceiver {

    private final AppStatusManager mManager;
    private boolean isGpsEnable = false;

    public GpsBroadcastReceiver(AppStatusManager mManager) {
        this.mManager = mManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        String action1 = LocationManager.PROVIDERS_CHANGED_ACTION;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            String action2 = LocationManager.MODE_CHANGED_ACTION;
            if (intentAction.equals(action1) || intentAction.equals(action2)) {
                //fix 部分手机连续调用多次广播问题
                if (isGpsEnabled(context) != isGpsEnable) {
                    this.notifyGpsSwitchState(isGpsEnabled(context));
                }
                isGpsEnable = isGpsEnabled(context);
            }
        } else {
            if (intentAction.equals(action1)) {
                if (isGpsEnabled(context) != isGpsEnable) {
                    this.notifyGpsSwitchState(isGpsEnabled(context));
                }
                isGpsEnable = isGpsEnabled(context);
            }
        }
    }

    /**
     * 判断gps是否关闭
     *
     * @param context 上下文
     * @return 返回true表示打开
     */
    public static boolean isGpsEnabled(Context context) {
        String allowed = System.LOCATION_PROVIDERS_ALLOWED;
        String gps = System.getString(context.getContentResolver(), allowed);
        return !TextUtils.isEmpty(gps) && gps.contains(LocationManager.GPS_PROVIDER);
    }

    private void notifyGpsSwitchState(boolean state) {
        if (this.mManager != null) {
            this.mManager.dispatcherGpsState(state);
        }
    }
}

