package com.yc.appwifilib.wifilibrary;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.yc.appwifilib.wifilibrary.listener.OnWifiConnectListener;
import com.yc.appwifilib.wifilibrary.listener.OnWifiEnabledListener;
import com.yc.appwifilib.wifilibrary.listener.OnWifiScanResultsListener;

import java.util.List;


/**
 * Created by kqw on 2016/8/2.
 * WifiManager : https://developer.android.com/reference/android/net/wifi/WifiManager.html
 * WifiConfiguration : https://developer.android.com/reference/android/net/wifi/WifiConfiguration.html
 * ScanResult : https://developer.android.com/reference/android/net/wifi/ScanResult.html
 * WifiInfo : https://developer.android.com/reference/android/net/wifi/WifiInfo.html
 * Wifi管理
 */
public class WiFiManager extends BaseWiFiManager {

    private static final String TAG = "WiFiManager";
    private static WiFiManager mWiFiManager;
    private static CallBackHandler mCallBackHandler = new CallBackHandler();
    private static final int WIFI_STATE_ENABLED = 0;
    private static final int WIFI_STATE_DISABLED = 1;
    private static final int SCAN_RESULTS_UPDATED = 3;
    private static final int WIFI_CONNECT_LOG = 4;
    private static final int WIFI_CONNECT_SUCCESS = 5;
    private static final int WIFI_CONNECT_FAILURE = 6;

    private WiFiManager(Context context) {
        super(context);
    }

    public static WiFiManager getInstance(Context context) {
        if (null == mWiFiManager) {
            synchronized (WiFiManager.class) {
                if (null == mWiFiManager) {
                    mWiFiManager = new WiFiManager(context);
                }
            }
        }
        return mWiFiManager;
    }

    /**
     * 打开Wifi
     */
    public void openWiFi() {
        if (!isWifiEnabled() && null != mWifiManager) {
            mWifiManager.setWifiEnabled(true);
        }
    }

    /**
     * 关闭Wifi
     */
    public void closeWiFi() {
        if (isWifiEnabled() && null != mWifiManager) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    /**
     * 连接到开放网络
     *
     * @param ssid 热点名
     * @return 配置是否成功
     */
    public boolean connectOpenNetwork(@NonNull String ssid) {
        // 获取networkId
        int networkId = setOpenNetwork(ssid);
        if (-1 != networkId) {
            // 保存配置
            boolean isSave = saveConfiguration();
            // 连接网络
            boolean isEnable = enableNetwork(networkId);

            return isSave && isEnable;
        }
        return false;
    }

    /**
     * 连接到WEP网络
     *
     * @param ssid 热点名
     * @param password 密码
     * @return 配置是否成功
     */
    public boolean connectWEPNetwork(@NonNull String ssid, @NonNull String password) {
        // 获取networkId
        int networkId = setWEPNetwork(ssid, password);
        if (-1 != networkId) {
            // 保存配置
            boolean isSave = saveConfiguration();
            // 连接网络
            boolean isEnable = enableNetwork(networkId);

            return isSave && isEnable;
        }
        return false;
    }

    /**
     * 连接到WPA2网络
     *
     * @param ssid 热点名
     * @param password 密码
     * @return 配置是否成功
     */
    public boolean connectWPA2Network(@NonNull String ssid, @NonNull String password) {
        // 获取networkId
        int networkId = setWPA2Network(ssid, password);
        if (-1 != networkId) {
            // 保存配置
            boolean isSave = saveConfiguration();
            // 连接网络
            boolean isEnable = enableNetwork(networkId);

            return isSave && isEnable;
        }
        return false;
    }

    /* *******************************************************************************************/


    /**
     * 广播接收者
     */
    public static class NetworkBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);
            if (intent.getAction() != null) {
                switch (intent.getAction()) {
                    case WifiManager.WIFI_STATE_CHANGED_ACTION: // WIFI状态发生变化
                        switch (intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)) {
                            case WifiManager.WIFI_STATE_ENABLING:
                                Log.i(TAG, "onReceive: 正在打开 WIFI...");
                                break;
                            case WifiManager.WIFI_STATE_ENABLED:
                                Log.i(TAG, "onReceive: WIFI 已打开");
                                mCallBackHandler.sendEmptyMessage(WIFI_STATE_ENABLED);
                                break;
                            case WifiManager.WIFI_STATE_DISABLING:
                                Log.i(TAG, "onReceive: 正在关闭 WIFI...");
                                break;
                            case WifiManager.WIFI_STATE_DISABLED:
                                Log.i(TAG, "onReceive: WIFI 已关闭");
                                mCallBackHandler.sendEmptyMessage(WIFI_STATE_DISABLED);
                                break;
                            case WifiManager.WIFI_STATE_UNKNOWN:
                            default:
                                Log.i(TAG, "onReceive: WIFI 状态未知!");
                                break;
                        }
                        break;
                    case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION: // WIFI扫描完成
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            boolean isUpdated = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
                            Log.i(TAG, "onReceive: WIFI扫描  " + (isUpdated ? "完成" : "未完成"));
                        } else {
                            Log.i(TAG, "onReceive: WIFI扫描完成");
                        }

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                                    == PackageManager.PERMISSION_GRANTED
                                    || context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                                    == PackageManager.PERMISSION_GRANTED) {
                                Message scanResultsMessage = Message.obtain();
                                scanResultsMessage.what = SCAN_RESULTS_UPDATED;
                                scanResultsMessage.obj = wifiManager.getScanResults();
                                mCallBackHandler.sendMessage(scanResultsMessage);
                            }
                        } else {
                            Message scanResultsMessage = Message.obtain();
                            scanResultsMessage.what = SCAN_RESULTS_UPDATED;
                            scanResultsMessage.obj = wifiManager.getScanResults();
                            mCallBackHandler.sendMessage(scanResultsMessage);
                        }

                        break;
                    case WifiManager.NETWORK_STATE_CHANGED_ACTION: // WIFI连接状态发生改变
//                    Log.i(TAG, "onReceive: WIFI连接状态发生改变");
//                    NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
//                    if (null != networkInfo && ConnectivityManager.TYPE_WIFI == networkInfo.getType()) {
//                    }
                        WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
                        if (null != wifiInfo && wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
                            String ssid = wifiInfo.getSSID();
                            Log.i(TAG, "onReceive: 网络连接成功 ssid = " + ssid);
                        }
                        break;
                    case WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION:
                        boolean isConnected = intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false);
                        Log.i(TAG, "onReceive: SUPPLICANT_CONNECTION_CHANGE_ACTION  isConnected = " + isConnected);
                        break;
                    case WifiManager.SUPPLICANT_STATE_CHANGED_ACTION: // WIFI连接请求状态发生改变
                        // 获取连接状态
                        SupplicantState supplicantState = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);

                        Message logMessage = Message.obtain();
                        logMessage.what = WIFI_CONNECT_LOG;
                        logMessage.obj = supplicantState.toString();
                        logMessage.obj = supplicantState.toString();
                        mCallBackHandler.sendMessage(logMessage);

                        switch (supplicantState) {
                            case INTERFACE_DISABLED: // 接口禁用
                                Log.i(TAG, "onReceive: INTERFACE_DISABLED 接口禁用");
                                break;
                            case DISCONNECTED:// 断开连接
//                            Log.i(TAG, "onReceive: DISCONNECTED:// 断开连接");
//                            break;
                            case INACTIVE: // 不活跃的
                                WifiInfo connectFailureInfo = wifiManager.getConnectionInfo();
                                Log.i(TAG, "onReceive: INACTIVE 不活跃的  connectFailureInfo = " + connectFailureInfo);
                                if (null != connectFailureInfo) {
                                    Message wifiConnectFailureMessage = Message.obtain();
                                    wifiConnectFailureMessage.what = WIFI_CONNECT_FAILURE;
                                    wifiConnectFailureMessage.obj = connectFailureInfo.getSSID();
                                    mCallBackHandler.sendMessage(wifiConnectFailureMessage);
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
                                Log.i(TAG, "onReceive: AUTHENTICATING: // 正在验证");
                                break;
                            case ASSOCIATING: // 正在关联
                                Log.i(TAG, "onReceive: ASSOCIATING: // 正在关联");
                                break;
                            case ASSOCIATED: // 已经关联
                                Log.i(TAG, "onReceive: ASSOCIATED: // 已经关联");
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
                                    Message wifiConnectSuccessMessage = Message.obtain();
                                    wifiConnectSuccessMessage.what = WIFI_CONNECT_SUCCESS;
                                    wifiConnectSuccessMessage.obj = connectSuccessInfo.getSSID();
                                    mCallBackHandler.sendMessage(wifiConnectSuccessMessage);
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
                        break;
                    default:
                        break;
                }
            }
        }
    }


    private static class CallBackHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WIFI_STATE_ENABLED: // WIFI已经打开
                    if (null != mOnWifiEnabledListener) {
                        mOnWifiEnabledListener.onWifiEnabled(true);
                    }
                    break;
                case WIFI_STATE_DISABLED: // WIFI已经关闭
                    if (null != mOnWifiEnabledListener) {
                        mOnWifiEnabledListener.onWifiEnabled(false);
                    }
                    break;
                case SCAN_RESULTS_UPDATED: // WIFI扫描完成
                    if (null != mOnWifiScanResultsListener) {
                        @SuppressWarnings("unchecked")
                        List<ScanResult> scanResults = (List<ScanResult>) msg.obj;
                        mOnWifiScanResultsListener.onScanResults(scanResults);
                    }
                    break;
                case WIFI_CONNECT_LOG: // WIFI连接完成
                    if (null != mOnWifiConnectListener) {
                        String log = (String) msg.obj;
                        mOnWifiConnectListener.onWiFiConnectLog(log);
                    }
                    break;
                case WIFI_CONNECT_SUCCESS: // WIFI连接完成
                    if (null != mOnWifiConnectListener) {
                        String ssid = (String) msg.obj;
                        mOnWifiConnectListener.onWiFiConnectSuccess(ssid);
                    }
                    break;
                case WIFI_CONNECT_FAILURE: // WIFI连接完成
                    if (null != mOnWifiConnectListener) {
                        String ssid = (String) msg.obj;
                        mOnWifiConnectListener.onWiFiConnectFailure(ssid);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private static OnWifiEnabledListener mOnWifiEnabledListener;

    private static OnWifiScanResultsListener mOnWifiScanResultsListener;

    private static OnWifiConnectListener mOnWifiConnectListener;

    public void setOnWifiEnabledListener(OnWifiEnabledListener listener) {
        mOnWifiEnabledListener = listener;
    }

    public void removeOnWifiEnabledListener() {
        mOnWifiEnabledListener = null;
    }

    public void setOnWifiScanResultsListener(OnWifiScanResultsListener listener) {
        mOnWifiScanResultsListener = listener;
    }

    public void removeOnWifiScanResultsListener() {
        mOnWifiScanResultsListener = null;
    }

    public void setOnWifiConnectListener(OnWifiConnectListener listener) {
        mOnWifiConnectListener = listener;
    }

    public void removeOnWifiConnectListener() {
        mOnWifiConnectListener = null;
    }
}
