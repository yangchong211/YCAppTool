package com.yc.networklib.wifi;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

class WifiReceiver extends BroadcastReceiver {

    private static final String TAG = "Network-WifiReceiver:";
    private static final int WIFI_AP_STATE_DISABLING = 10;
    private static final int WIFI_AP_STATE_DISABLED = 11;
    private static final int WIFI_AP_STATE_ENABLING = 12;
    private static final int WIFI_AP_STATE_ENABLED = 13;
    private static final int WIFI_AP_STATE_FAILED = 14;

    private final WifiHelper wifiHelper;

    public WifiReceiver(WifiHelper wifiHelper) {
        this.wifiHelper = wifiHelper;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "WifiReceiver: " + intent.getAction());

        // WIFI_STATE_CHANGED_ACTION：当WiFi被打开、关闭、正在打开、正在关闭或者位置状态即wifi状态发生改变时系统会自动发送该广播
        // 该广播会附带有两个值，一个是int型表示改变后的state，可通过字段EXTRA_WIFI_STATE获取
        // 测试OK
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

        // 处理Wi-Fi连接状态发生改变监听
        // WiFi连接发生改变时系统会发送该广播，通过字段EXTRA_NETWORK_INFO可以获取到WiFi连接的状态，
        // 如果是已连接的状态，则会有额外的两个字段，字段EXTRA_BSSID可以获取到所连接的WiFi的bssid，
        // 字段EXTRA_WIFI_INFO可以获取到所连接的WiFi的信息获取到wifiInfo实例
        if (intent.getAction() != null &&
                intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            // WIFI连接状态发生改变
            WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
            if (null != wifiInfo && wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
                String ssid = wifiInfo.getSSID();
                Log.i(TAG, "onReceive: 网络连接成功 ssid = " + ssid);
            }
        }

        if (intent.getAction() != null &&
                intent.getAction().equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
            boolean isConnected = intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false);
            Log.i(TAG, "onReceive: SUPPLICANT_CONNECTION_CHANGE_ACTION  isConnected = " + isConnected);
        }

        //处理WIFI连接请求状态发生改变
        //正在建立的连接状态已经改变，该广播会携带两个值
        WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        if (intent.getAction() != null &&
                intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
            // 获取连接状态
            SupplicantState supplicantState = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
            switch (supplicantState) {
                case INTERFACE_DISABLED: // 接口禁用
                    Log.i(TAG, "onReceive: INTERFACE_DISABLED 接口禁用");
                    break;
                case DISCONNECTED:// 断开连接
                    Log.i(TAG, "onReceive: DISCONNECTED: 断开连接");
                    break;
                case INACTIVE: // 不活跃的
                    WifiInfo connectFailureInfo = wifiManager.getConnectionInfo();
                    Log.i(TAG, "onReceive: INACTIVE 不活跃的  connectFailureInfo = " + connectFailureInfo);
                    if (null != connectFailureInfo) {
                        String ssid = connectFailureInfo.getSSID();
                        setWifiConnectState(ssid, false);
                        // 断开连接
                        int networkId = connectFailureInfo.getNetworkId();
                        boolean isDisable = wifiManager.disableNetwork(networkId);
                        boolean isDisconnect = wifiManager.disconnect();
                        Log.i(TAG, "onReceive: 断开连接  =  " + (isDisable && isDisconnect));
                    }
                    break;
                case SCANNING: // 正在扫描
                    Log.i(TAG, "onReceive: SCANNING 正在扫描");
                    break;
                case AUTHENTICATING: // 正在验证
                    Log.i(TAG, "onReceive: AUTHENTICATING: 正在验证");
                    break;
                case ASSOCIATING: // 正在关联
                    Log.i(TAG, "onReceive: ASSOCIATING: 正在关联");
                    break;
                case ASSOCIATED: // 已经关联
                    Log.i(TAG, "onReceive: ASSOCIATED: 已经关联");
                    break;
                case FOUR_WAY_HANDSHAKE:
                    Log.i(TAG, "onReceive: FOUR_WAY_HANDSHAKE:");
                    break;
                case GROUP_HANDSHAKE:
                    Log.i(TAG, "onReceive: GROUP_HANDSHAKE:");
                    break;
                case COMPLETED: // 完成
                    Log.i(TAG, "onReceive: WIFI_CONNECT_SUCCESS: // 完成");
                    WifiInfo connectSuccessInfo = wifiManager.getConnectionInfo();
                    if (null != connectSuccessInfo) {
                        String ssid = connectSuccessInfo.getSSID();
                        setWifiConnectState(ssid, true);
                    }
                    break;
                case DORMANT:
                    Log.i(TAG, "onReceive: DORMANT:");
                    break;
                case UNINITIALIZED: // 未初始化
                    Log.i(TAG, "onReceive: UNINITIALIZED: // 未初始化");
                    break;
                case INVALID: // 无效的
                    Log.i(TAG, "onReceive: INVALID: // 无效的");
                    break;
                default:
                    break;
            }
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
                    Log.i(TAG, "onReceive: 热点已关闭");
                    break;
                // 热点状态：已开启
                case WIFI_AP_STATE_ENABLED:
                    setHotpotState(true);
                    Log.i(TAG, "onReceive: 热点已开启");
                    break;
                // 打开热点异常
                case WIFI_AP_STATE_FAILED:
                    setHotpotState(false);
                    Log.i(TAG, "onReceive: 热点异常");
                    break;
                case WIFI_AP_STATE_DISABLING:
                case WIFI_AP_STATE_ENABLING:
                default:
                    Log.i(TAG, "onReceive: 热点其他case");
                    break;
            }
        }


        // 注册一个广播监听器，系统会在完成扫描请求时调用此监听器，提供其成功/失败状态。
        // 用户可以监听该广播通过调用WifiManager的getScanResults方法来获取到扫描结果
        // 测试OK
        if (intent.getAction() != null &&
                intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
            boolean success;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
                Log.i(TAG, "onReceive: WIFI扫描请求 " + success);
            } else {
                Log.i(TAG, "onReceive: WIFI扫描完成");
                success = true;
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED
                        || context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    List<ScanResult> wifiListData = WifiHelper.getInstance().getWifiList();
                    setWifiScanList(success, wifiListData);
                }
            } else {
                List<ScanResult> wifiListData = WifiHelper.getInstance().getWifiList();
                setWifiScanList(success, wifiListData);
            }
        }
    }

    private void setWifiState(boolean state) {
        if (wifiHelper != null) {
            wifiHelper.setWifiState(state);
        }
    }

    private void setHotpotState(boolean state) {
        if (wifiHelper != null) {
            wifiHelper.setHotpotState(state);
        }
    }

    private void setWifiConnectState(String ssid, boolean state) {
        if (wifiHelper != null) {
            wifiHelper.setWifiConnectState(ssid, state);
        }
    }

    private void setWifiScanList(boolean success, List<ScanResult> wifiListData) {
        if (wifiHelper != null) {
            wifiHelper.setWifiScanList(success, wifiListData);
        }
    }
}
