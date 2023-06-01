package com.yc.appwifilib;


import static android.content.Context.WIFI_SERVICE;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.List;

public class BaseWifiManager {

    Context context;
    private WifiManager wifiManager;
    private ConnectivityManager connectivityManager;

    public BaseWifiManager(Context context) {
        this.context = context;
        // 取得WifiManager对象
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    WifiManager getWifiManager() {
        if (context == null) {
            throw new RuntimeException("please init first");
        }
        if (wifiManager == null) {
            wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        }
        return wifiManager;
    }

    ConnectivityManager getConnectivityManager() {
        if (context == null) {
            throw new RuntimeException("please init first");
        }
        if (connectivityManager == null) {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        return connectivityManager;
    }


    /**
     * 添加开放网络配置
     *
     * @param ssid SSID
     * @return NetworkId
     */
    int setOpenNetwork(@NonNull String ssid) {
        if (TextUtils.isEmpty(ssid)) {
            return -1;
        }
        WifiConfiguration wifiConfiguration = getConfigFromConfiguredNetworksBySsid(ssid);
        if (null == wifiConfiguration) {
            // 生成配置
            WifiConfiguration wifiConfig = new WifiConfiguration();
            wifiConfig.SSID = addDoubleQuotation(ssid);
            wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            wifiConfig.allowedAuthAlgorithms.clear();
            wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            // 添加配置并返回NetworkID
            return addNetwork(wifiConfig);
        } else {
            //这里的wifiManager已经存在了要连接的wifi的networkId，所以不用重新调用wifiManager的addNetwork方法
            // 返回NetworkID
            return wifiConfiguration.networkId;
        }
    }


    /**
     * 添加WEP网络配置
     *
     * @param ssid SSID
     * @param password 密码
     * @return NetworkId
     */
    int setWEPNetwork(@NonNull String ssid, @NonNull String password) {
        if (TextUtils.isEmpty(ssid) || TextUtils.isEmpty(password)) {
            return -1;
        }
        WifiConfiguration wifiConfiguration = getConfigFromConfiguredNetworksBySsid(ssid);
        if (null == wifiConfiguration) {
            // 添加配置
            WifiConfiguration wifiConfig = new WifiConfiguration();
            wifiConfig.SSID = addDoubleQuotation(ssid);
            wifiConfig.wepKeys[0] = "\"" + password + "\"";
            wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            // 添加配置并返回NetworkID
            return addNetwork(wifiConfig);
        } else {
            // 更新配置并返回NetworkID
            wifiConfiguration.wepKeys[0] = "\"" + password + "\"";
            return updateNetwork(wifiConfiguration);
        }
    }

    /**
     * 添加WPA网络配置
     *
     * @param ssid SSID
     * @param password 密码
     * @return NetworkId
     */
    int setWPA2Network(@NonNull String ssid, @NonNull String password) {
        if (TextUtils.isEmpty(ssid) || TextUtils.isEmpty(password)) {
            return -1;
        }
        WifiConfiguration wifiConfiguration = getConfigFromConfiguredNetworksBySsid(ssid);
        if (null == wifiConfiguration) {
            WifiConfiguration wifiConfig = new WifiConfiguration();
            wifiConfig.SSID = addDoubleQuotation(ssid);
            wifiConfig.preSharedKey = "\"" + password + "\"";
            wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            wifiConfig.status = WifiConfiguration.Status.ENABLED;
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            // 添加配置并返回NetworkID
            return addNetwork(wifiConfig);
        } else {
            // 更新配置并返回NetworkID
            wifiConfiguration.preSharedKey = "\"" + password + "\"";
            return updateNetwork(wifiConfiguration);
        }
    }

    /**
     * 通过热点名获取热点配置
     *
     * @param ssid 热点名
     * @return 配置信息
     */
    WifiConfiguration getConfigFromConfiguredNetworksBySsid(@NonNull String ssid) {
        ssid = addDoubleQuotation(ssid);
        //检查是否已经配置过该wifi，通过WifiManager获取已经配置的wifi列表
        List<WifiConfiguration> existingConfigs = getConfiguredNetworks();
        if (null != existingConfigs) {
            for (WifiConfiguration existingConfig : existingConfigs) {
                if (existingConfig.SSID.equals(ssid)) {
                    return existingConfig;
                }
            }
        }
        return null;
    }

    /**
     * 添加网络配置
     *
     * @param wifiConfig 配置信息
     * @return NetworkId
     */
    private int addNetwork(WifiConfiguration wifiConfig) {
        if (null != wifiManager) {
            int networkId = wifiManager.addNetwork(wifiConfig);
            if (-1 != networkId) {
                boolean isSave = wifiManager.saveConfiguration();
                if (isSave) {
                    return networkId;
                }
            }
        }
        return -1;
    }


    /**
     * 获取配置过的WIFI信息
     *
     * @return 配置信息
     */
    List<WifiConfiguration> getConfiguredNetworks() {
        if (null != wifiManager) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return wifiManager.getConfiguredNetworks();
            }
        }
        return null;
    }

    /**
     * 更新网络配置
     *
     * @param wifiConfig 配置信息
     * @return NetworkId
     */
    private int updateNetwork(WifiConfiguration wifiConfig) {
        if (null != wifiManager) {
            int networkId = wifiManager.updateNetwork(wifiConfig);
            if (-1 != networkId) {
                boolean isSave = wifiManager.saveConfiguration();
                if (isSave) {
                    return networkId;
                }
            }
        }
        return -1;
    }

    /**
     * 保持配置
     *
     * @return 保持是否成功
     */
    boolean saveConfiguration() {
        return null != wifiManager && wifiManager.saveConfiguration();
    }

    /**
     * 连接到网络
     *
     * @param networkId NetworkId
     * @return 连接结果
     */
    boolean enableNetwork(int networkId) {
        if (null != wifiManager) {
            boolean isDisconnect = disconnectCurrentWifi();
            boolean isEnableNetwork = wifiManager.enableNetwork(networkId, true);
            boolean isSave = wifiManager.saveConfiguration();
            boolean isReconnect = wifiManager.reconnect();
            return isDisconnect && isEnableNetwork && isSave && isReconnect;
        }
        return false;
    }

    /**
     * 断开当前的WIFI
     *
     * @return 是否断开成功
     */
    private boolean disconnectCurrentWifi() {
        WifiInfo wifiInfo = getConnectionInfo();
        if (null != wifiInfo) {
            int networkId = wifiInfo.getNetworkId();
            return disconnectWifi(networkId);
        } else {
            // 断开状态
            return true;
        }
    }

    /**
     * 断开指定 WIFI
     *
     * @param netId netId
     * @return 是否断开
     */
    private boolean disconnectWifi(int netId) {
        if (null != wifiManager) {
            boolean isDisable = wifiManager.disableNetwork(netId);
            boolean isDisconnect = wifiManager.disconnect();
            return isDisable && isDisconnect;
        }
        return false;
    }

    /**
     * 获取当前正在连接的WIFI信息
     *
     * @return 当前正在连接的WIFI信息
     */
    public WifiInfo getConnectionInfo() {
        if (null != wifiManager) {
            return wifiManager.getConnectionInfo();
        }
        return null;
    }

    /**
     * 添加双引号
     * 这里需要注意的是：
     * WifiConfiguration里封装的wifi的SSID是包含双引号的，而ScanResult的SSID是不包含双引号的，这里比较时需要注意！
     *
     * @param text 待处理字符串
     * @return 处理后字符串
     */
    public String addDoubleQuotation(String text) {
        //直接判断equals(ssid)肯定是不存在的，因为“XXX_WIFI”才是配置中的SSID，它外面包了双引号
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        return "\"" + text + "\"";
    }

    public void release() {
        context = null;
        wifiManager = null;
        connectivityManager = null;
    }
}
