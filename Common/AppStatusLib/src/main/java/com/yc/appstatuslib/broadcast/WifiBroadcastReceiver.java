package com.yc.appstatuslib.broadcast;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yc.appstatuslib.AppStatusManager;

public class WifiBroadcastReceiver extends BroadcastReceiver {

    private final AppStatusManager mManager;

    public WifiBroadcastReceiver(AppStatusManager mManager) {
        this.mManager = mManager;
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED")) {
            switch(intent.getIntExtra("wifi_state", 4)) {
                case 0:
                case 2:
                case 4:
                default:
                    break;
                case 1:
                    this.notifyGpsSwitchState(false);
                    break;
                case 3:
                    this.notifyGpsSwitchState(true);
            }
        }
    }

    private void notifyGpsSwitchState(boolean state) {
        if (this.mManager != null) {
            this.mManager.dispatcherWifiState(state);
        }
    }
}
