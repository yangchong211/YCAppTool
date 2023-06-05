package com.yc.appwifilib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

public class WifiReceiver extends BroadcastReceiver {

    private static final int WIFI_AP_STATE_DISABLING = 10;
    private static final int WIFI_AP_STATE_DISABLED = 11;
    private static final int WIFI_AP_STATE_ENABLING = 12;
    private static final int WIFI_AP_STATE_ENABLED = 13;
    private static final int WIFI_AP_STATE_FAILED = 14;

    private WifiStateListener wifiStateListener;

    public WifiStateListener getWifiStateListener() {
        return wifiStateListener;
    }

    public void setWifiStateListener(WifiStateListener wifiStateListener) {
        this.wifiStateListener = wifiStateListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null &&
                intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            switch (intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)) {
                case WifiManager.WIFI_STATE_DISABLED:
                    setWifiState(false);
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    setWifiState(true);
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                case WifiManager.WIFI_STATE_ENABLING:
                case WifiManager.WIFI_STATE_UNKNOWN:
                default:
                    break;

            }
        }
        if (intent.getAction() != null &&
                "android.net.wifi.WIFI_AP_STATE_CHANGED".equals(intent.getAction())) {
            //热点的状态为：10---正在关闭；11---已关闭；12---正在开启；13---已开启
            int state = intent.getIntExtra("wifi_state", 0);
            switch (state) {
                // 热点状态：已关闭
                case WIFI_AP_STATE_DISABLED:
                    setHotpotState(false);
                    break;
                // 热点状态：已开启
                case WIFI_AP_STATE_ENABLED:
                    setHotpotState(true);
                    break;
                // 打开热点异常
                case WIFI_AP_STATE_FAILED:
                    if (wifiStateListener != null) {
                        wifiStateListener.onHotpotOpenError();
                    }
                    break;
                case WIFI_AP_STATE_DISABLING:
                case WIFI_AP_STATE_ENABLING:
                default:
                    break;
            }
        }
    }

    private void setWifiState(boolean state) {
        if (wifiStateListener != null) {
            if (state) {
                wifiStateListener.onWifiOpen();
            } else {
                wifiStateListener.onWifiClose();
            }
        }
    }

    private void setHotpotState(boolean state) {
        if (wifiStateListener != null) {
            if (state) {
                wifiStateListener.onHotpotOpen();
            } else {
                wifiStateListener.onHotpotClose();
            }
        }
    }
}
