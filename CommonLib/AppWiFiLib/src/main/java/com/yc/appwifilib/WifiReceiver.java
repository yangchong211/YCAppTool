package com.yc.appwifilib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiReceiver extends BroadcastReceiver {

    private static final String TAG = "WifiReceiver:";
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
            // WIFI状态发生变化
            int intExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
            switch (intExtra) {
                case WifiManager.WIFI_STATE_DISABLED:
                    Log.i(TAG, "onReceive: WIFI 已关闭");
                    setWifiState(false);
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    Log.i(TAG, "onReceive: WIFI 已打开");
                    setWifiState(true);
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    Log.i(TAG, "onReceive: 正在关闭 WIFI...");
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    Log.i(TAG, "onReceive: 正在打开 WIFI...");
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                default:
                    Log.i(TAG, "onReceive: WIFI 状态未知!");
                    break;

            }
        }

        //处理Wi-Fi连接状态
        if (intent.getAction() != null &&
                intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            // WIFI连接状态发生改变
            int intExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);

        }

        //WifiManager WIFI_AP_STATE_CHANGED_ACTION 系统
        if (intent.getAction() != null &&
                "android.net.wifi.WIFI_AP_STATE_CHANGED".equals(intent.getAction())) {
            //热点的状态为：10---正在关闭；11---已关闭；12---正在开启；13---已开启
            int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
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
                    setHotpotState(false);
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
            wifiStateListener.onWifiEnabled(state);
        }
    }

    private void setHotpotState(boolean state) {
        if (wifiStateListener != null) {
            wifiStateListener.onHotpotEnabled(state);
        }
    }
}
