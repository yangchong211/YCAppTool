package com.yc.networklib.wifi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.IntentFilter;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.yc.toolutils.context.AppToolUtils;
import com.yc.toolutils.system.AppLogUtils;

import java.util.ArrayList;
import java.util.List;

public class WifiHelper extends BaseWifiManager implements IWifiFeature {

    public static final String TAG = "NetWork-Wi-Fi";
    private List<WifiStateListener> mListener;
    private WifiReceiver wifiReceiver;

    private WifiHelper() {
        super(AppToolUtils.getApp());
    }

    public static WifiHelper getInstance() {
        return SingletonHelper.INSTANCE;
    }

    private static class SingletonHelper {
        @SuppressLint("StaticFieldLeak")
        private final static WifiHelper INSTANCE = new WifiHelper();
    }

    public void init() {
        mListener = new ArrayList<>();
        registerWifiBroadcast();
    }

    /**
     * 判断 wifi 是否打开
     * <p>需添加权限
     * {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />}</p>
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    @Override
    public boolean isWifiEnable() {
        return getWifiManager().isWifiEnabled();
    }

    /**
     * 打开 wifi
     * <p>需添加权限
     * {@code <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />}</p>
     */
    @Override
    public boolean openWifi() {
        if (!isWifiEnable()) {
            //对启用和停用 WLAN 实施了限制
            //Android 10 或更高版本为目标平台的应用无法启用或停用 WLAN。WifiManager.setWifiEnabled() 方法始终返回 false。
            //如果您需要提示用户启用或停用 WLAN，请使用设置面板。
            //对直接访问已配置的 WLAN 网络实施了限制
            return getWifiManager().setWifiEnabled(true);
        }
        return true;
    }

    /**
     * 关闭 wifi
     * <p>需添加权限
     * {@code <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />}</p>
     */
    @Override
    public boolean closeWifi() {
        //对启用和停用 WLAN 实施了限制
        //Android 10 或更高版本为目标平台的应用无法启用或停用 WLAN。WifiManager.setWifiEnabled() 方法始终返回 false。
        //如果您需要提示用户启用或停用 WLAN，请使用设置面板。
        //对直接访问已配置的 WLAN 网络实施了限制
        return getWifiManager().setWifiEnabled(false);
    }

    /**
     * 连接加密网络：使用ssid + pwd连接
     *
     * @param ssid Wi-Fi名称
     * @param pwd  Wi-Fi密码
     * @return true表示连接上
     */
    @Override
    public boolean connect(String ssid, String pwd) {
        return connectWEPNetwork(ssid, pwd);
    }

    @Override
    public boolean connectWpa(String ssid, String pwd) {
        return connectWPA2Network(ssid, pwd);
    }

    /**
     * 连接开放网络：使用ssid
     *
     * @param ssid Wi-Fi名称
     * @return true表示连接上Wi-Fi
     */
    @Override
    public boolean connect(String ssid) {
        return connectOpenNetwork(ssid);
    }

    /**
     * 判断是否有某个Wi-Fi
     *
     * @param ssid 热点名
     * @return 是否有该热点
     */
    @Override
    public boolean isHaveWifi(@NonNull String ssid) {
        return getConfigFromConfiguredNetworksBySsid(ssid) != null;
    }

    /**
     * 是否连接着指定WiFi,通过已连接的SSID判断
     *
     * @param ssid Wi-Fi名称ssid
     * @return true表示有该Wi-Fi已经连接
     */
    @Override
    public boolean isConnectedTargetSsid(String ssid) {
        return ssid.equals(getSsid());
    }

    /**
     * 扫描附近的WIFI
     */
    @Override
    public boolean startScan() {
        //使用 WifiManager.startScan() 请求扫描。请务必检查方法的返回状态，因为调用可能因以下任一原因失败：
        //由于短时间扫描过多，扫描请求可能遭到节流。
        //设备处于空闲状态，扫描已停用。
        //WLAN 硬件报告扫描失败。
        return getWifiManager().startScan();
    }

    /**
     * 连接到开放网络
     * WIFI 连接
     * 需要用到的 API
     * addNetwork(WifiConfiguration config)
     * enableNetwork(int netId, boolean attemptConnect)
     *
     * @param ssid 热点名
     * @return 配置是否成功
     */
    private boolean connectOpenNetwork(@NonNull String ssid) {
        // 获取networkId
        int networkId = setOpenNetwork(ssid);
        //将配置好的特定WIFI密码信息添加,添加完成后默认是不激活状态，成功返回ID，否则为-1
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
     * @param ssid     热点名
     * @param password 密码
     * @return 配置是否成功
     */
    private boolean connectWEPNetwork(@NonNull String ssid, @NonNull String password) {
        // 获取networkId
        int networkId = setWEPNetwork(ssid, password);
        AppLogUtils.d("Network-Wifi:" , "connectWEPNetwork " + networkId);
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
     * @param ssid     热点名
     * @param password 密码
     * @return 配置是否成功
     */
    private boolean connectWPA2Network(@NonNull String ssid, @NonNull String password) {
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

    /**
     * 开启便携热点
     *
     * @param activity activity
     * @param ssid     便携热点SSID
     * @param password 便携热点密码
     */
    public void openAp(Activity activity, String ssid, String password) {
        WifiToolUtils.openAp(activity, ssid, password);
    }


    /**
     * 获取当前WiFi名称
     * android.permission.ACCESS_FINE_LOCATION这个权限主要getConnectionInfo()这个方法需要
     * 这个方法是用于获取处于活跃状态的WiFi信息，包括已经连接和连接中两种状态。
     * 此外获取wifi列表的的方法中也需要，这里只介绍getConnectionInfo()
     *
     * @return 返回不含双引号的SSID
     */
    @Override
    public String getSsid() {
        /*if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            if (!isGrantedLocationPermission()) {
                return "Version Android O+ get ssid need Location permission!";
            }
        }*/
        String ssid = getWifiManager().getConnectionInfo().getSSID();
        if (TextUtils.isEmpty(ssid)) {
            return "";
        }
        if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
            return ssid.subSequence(1, ssid.length() - 1).toString();
        }
        return ssid;
    }

    /**
     * 获取被连接网络的mac地址
     *
     * @return
     */
    public String getBssid() {
        String bSsid = getWifiManager().getConnectionInfo().getBSSID();
        if (TextUtils.isEmpty(bSsid)) {
            return "";
        }
        return bSsid;
    }

    /**
     * 获取wifi的ip
     *
     * @return
     */
    public String getWifiIpStr() {
        if (getWifiManager() != null) {
            int wifiIp = WifiToolUtils.getWifiIp();
            return WifiToolUtils.intToIp(wifiIp);
        }
        return null;
    }

    /**
     * 获取wifi的强弱
     *
     * @return 信号level
     */
    public String getWifiLevel() {
        if (isWifiEnable()) {
            WifiInfo mWifiInfo = getWifiManager().getConnectionInfo();
            int wifi = mWifiInfo.getRssi();
            return WifiToolUtils.getWifiLevel(wifi);
        }
        return "无wifi连接";
    }


    /**
     * 使用 WifiManager.getScanResults() 获取扫描结果。
     *
     * @return 获取WiFi列表
     */
    public List<ScanResult> getWifiList() {
        return WifiToolUtils.getWifiList();
    }

    /**
     * 获取开启便携热点后自身热点IP地址
     *
     * @return ip地址
     */
    public String getLocalIp() {
        DhcpInfo dhcpInfo = getWifiManager().getDhcpInfo();
        if (dhcpInfo != null) {
            int address = dhcpInfo.serverAddress;
            return ((address & 0xFF)
                    + "." + ((address >> 8) & 0xFF)
                    + "." + ((address >> 16) & 0xFF)
                    + "." + ((address >> 24) & 0xFF));
        }
        return null;
    }

    /**
     * wifi设置
     *
     * @param ssid     WIFI名称
     * @param pws      WIFI密码
     * @param isHasPws 是否有密码
     */
    WifiConfiguration getWifiConfig(String ssid, String pws, boolean isHasPws) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + ssid + "\"";

        WifiConfiguration tempConfig = isExist(ssid);
        if (tempConfig != null) {
            getWifiManager().removeNetwork(tempConfig.networkId);
        }
        if (isHasPws) {
            config.preSharedKey = "\"" + pws + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        } else {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        return config;
    }

    public boolean isApEnable() {
        return WifiToolUtils.isApEnable();
    }

    /**
     * 注册Wifi广播
     */
    private void registerWifiBroadcast() {
        // 刚注册广播时会立即收到一条当前状态的广播
        if (wifiReceiver == null) {
            IntentFilter filter = new IntentFilter();
            // WIFI状态发生变化
            filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            // 热点的状态发生变化
            filter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
            // 处理WIFI连接请求状态发生改变
            filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
            // 处理Wi-Fi连接状态
            filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            // 注册一个广播监听器
            // 系统会在完成扫描请求时调用此监听器，提供其成功/失败状态
            // 对于搭载 Android 10及更高版本的设备，系统将针对平台或其他应用在设备上执行的所有完整 WLAN 扫描发送此广播。
            filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            wifiReceiver = new WifiReceiver(this);
            AppToolUtils.getApp().registerReceiver(wifiReceiver, filter);
            Log.i(TAG, "registerWifiBroadcast");
        }
    }

    /**
     * 解除Wifi广播
     */
    private void unregisterWifiBroadcast() {
        if (wifiReceiver != null) {
            AppToolUtils.getApp().unregisterReceiver(wifiReceiver);
            wifiReceiver = null;
            Log.i(TAG, "unregisterWifiBroadcast");
        }
    }

    @Override
    public void registerWifiListener(WifiStateListener listener) {
        if (this.mListener != null && !mListener.contains(listener)) {
            this.mListener.add(listener);
        }
    }

    @Override
    public boolean unregisterWifiListener(WifiStateListener listener) {
        return mListener != null && this.mListener.remove(listener);
    }

    void setWifiState(boolean state) {
        for (WifiStateListener listener : mListener) {
            listener.onWifiEnabled(state);
        }
    }

    void setHotpotState(boolean state) {
        for (WifiStateListener listener : mListener) {
            listener.onHotpotEnabled(state);
        }
    }

    void setWifiConnectState(String ssid, boolean state) {
        for (WifiStateListener listener : mListener) {
            listener.onWiFiConnectState(ssid, state);
        }
    }

    void setWifiScanList(boolean success, List<ScanResult> wifiListData) {
        for (WifiStateListener listener : mListener) {
            listener.onScanResults(success, wifiListData);
        }
    }

    /**
     * 资源释放
     */
    @Override
    public void release() {
        super.release();
        if (mListener != null) {
            mListener.clear();
        }
        unregisterWifiBroadcast();
        System.gc();
    }

}
