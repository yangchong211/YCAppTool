package com.yc.appwifilib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.yc.appcontextlib.AppToolUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static android.content.Context.WIFI_SERVICE;
import static android.os.Build.VERSION_CODES.P;

public class WifiHelper extends BaseWifiManager{

    private static final String TAG = "NetWork-Wi-Fi";

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

    /**
     * 请求修改系统设置Code
     */
    public static final int REQUEST_WRITE_SETTING_CODE = 1;

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private WifiReceiver wifiReceiver;

    /**
     * 判断 wifi 是否打开
     * <p>需添加权限
     * {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />}</p>
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public boolean isWifiEnable() {
        return getWifiManager().isWifiEnabled();
    }

    /**
     * 打开 wifi
     * <p>需添加权限
     * {@code <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />}</p>
     */
    public void openWifi() {
        //对启用和停用 WLAN 实施了限制
        //Android 10 或更高版本为目标平台的应用无法启用或停用 WLAN。WifiManager.setWifiEnabled() 方法始终返回 false。
        //如果您需要提示用户启用或停用 WLAN，请使用设置面板。
        //对直接访问已配置的 WLAN 网络实施了限制
        getWifiManager().setWifiEnabled(true);
    }

    /**
     * 关闭 wifi
     * <p>需添加权限
     * {@code <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />}</p>
     */
    public void closeWifi() {
        //对启用和停用 WLAN 实施了限制
        //Android 10 或更高版本为目标平台的应用无法启用或停用 WLAN。WifiManager.setWifiEnabled() 方法始终返回 false。
        //如果您需要提示用户启用或停用 WLAN，请使用设置面板。
        //对直接访问已配置的 WLAN 网络实施了限制
        getWifiManager().setWifiEnabled(false);
    }

    /**
     * 判断是否有某个Wi-Fi
     * @param ssid  热点名
     * @return 是否有该热点
     */
    public boolean isHaveWifi(@NonNull String ssid){
        return getConfigFromConfiguredNetworksBySsid(ssid) != null;
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

    /**
     * 连接WIFI,密码为空则不使用密码连接
     * 9.0以下设备需要关闭热点
     *
     * @param ssid     wifi名称
     * @param password wifi密码
     */
    public void connectWifi(Activity activity, String ssid, String password) {
        // 9.0以下系统不支持关闭热点和WiFi共存
        if (Build.VERSION.SDK_INT <= P) {
            if (isApEnable()) {
                closeAp(activity);
            }
        }
        // 打开WiFi
        if (!isWifiEnable()) {
            openWifi();
        }
        // 需要等待WiFi开启再去连接
        new ThreadPoolExecutor(CPU_COUNT
                , 2 * CPU_COUNT + 1
                , 60
                , TimeUnit.SECONDS
                , new LinkedBlockingQueue<>()
                , new ConnectWiFiThread()).execute(new Runnable() {
            @Override
            public void run() {
                while (!getWifiManager().isWifiEnabled()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                getWifiManager().disableNetwork(getWifiManager().getConnectionInfo().getNetworkId());
                int netId = getWifiManager().addNetwork(getWifiConfig(ssid, password, !TextUtils.isEmpty(password)));
                getWifiManager().enableNetwork(netId, true);
            }
        });
    }

    /**
     * 便携热点是否开启
     */
    public boolean isApEnable() {
        try {
            Method method = getWifiManager().getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (boolean) method.invoke(getWifiManager());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 关闭便携热点
     */
    public void closeAp(Activity activity) {
        // 6.0+申请修改系统设置权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isGrantedWriteSettings(activity)) {
                requestWriteSettings(activity);
            }
        }
        // 8.0以上的关闭方式不一样
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopTethering();
        } else {
            try {
                Method method = getWifiManager().getClass().getMethod("setWifiApEnabled",
                        WifiConfiguration.class, boolean.class);
                method.setAccessible(true);
                method.invoke(getWifiManager(), null, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 开启便携热点
     *
     * @param activity activity
     * @param ssid     便携热点SSID
     * @param password 便携热点密码
     */
    public void openAp(Activity activity, String ssid, String password) {
        // 6.0+申请修改系统设置权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isGrantedWriteSettings(activity)) {
                requestWriteSettings(activity);
            }
        }
        // 9.0以下版本不支持热点和WiFi共存
        if (Build.VERSION.SDK_INT <= P) {
            // 关闭WiFi
            if (isWifiEnable()) {
                getWifiManager().setWifiEnabled(false);
            }
        }
        // 8.0以下的开启方式不一样
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startTethering();
        } else {
            try {
                // 热点的配置类
                WifiConfiguration config = new WifiConfiguration();
                // 配置热点的名称(可以在名字后面加点随机数什么的)
                config.SSID = ssid;
                config.preSharedKey = password;
                //是否隐藏网络
                config.hiddenSSID = false;
                //开放系统认证
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                config.status = WifiConfiguration.Status.ENABLED;
                // 调用反射打开热点
                Method method = getWifiManager().getClass().getMethod("setWifiApEnabled",
                        WifiConfiguration.class, Boolean.TYPE);
                // 返回热点打开状态
                method.invoke(getWifiManager(), config, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查看授权情况, 开启热点需要申请系统设置修改权限，如有必要，可提前申请
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestWriteSettings(Activity activity) {
        if (isGrantedWriteSettings(activity)) {
            Log.d(TAG, "已授权修改系统设置权限");
            return;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(intent, REQUEST_WRITE_SETTING_CODE);
    }

    /**
     * 返回应用程序是否可以修改系统设置
     *
     * @return {@code true}: yes
     * {@code false}: no
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isGrantedWriteSettings(Context context) {
        return Settings.System.canWrite(context);
    }

    /**
     * 是否连接着指定WiFi,通过已连接的SSID判断
     *
     * @param ssid 是否连接着wifi
     */
    public boolean isConnectedTargetSsid(String ssid) {
        return ssid.equals(getSsid());
    }

    /**
     * 获取当前WiFi名称
     * android.permission.ACCESS_FINE_LOCATION这个权限主要getConnectionInfo()这个方法需要
     * 这个方法是用于获取处于活跃状态的WiFi信息，包括已经连接和连接中两种状态。
     * 此外获取wifi列表的的方法中也需要，这里只介绍getConnectionInfo()
     *
     *
     * @return 返回不含双引号的SSID
     */
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
     * @return
     */
    public int getWifiIp(){
        if (getWifiManager() != null) {
            WifiInfo wifiInfo = getWifiManager().getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            return ipAddress;
        }
        return -1;
    }


    /**
     * 获取wifi的强弱
     * @return
     */
    public String getWifiState(){
        if (isWifiEnable()) {
            WifiInfo mWifiInfo = getWifiManager().getConnectionInfo();
            int wifi = mWifiInfo.getRssi();
            //获取wifi信号强度
            if (wifi > -50 && wifi < 0) {
                //最强
                return "最强";
            } else if (wifi > -70 && wifi < -50) {
                //较强
                return "较强";
            } else if (wifi > -80 && wifi < -70) {
                //较弱
                return "较弱";
            } else if (wifi > -100 && wifi < -80) {
                //微弱
                return "微弱";
            } else {
                return "微弱";
            }
        }
        return "无wifi连接";
    }

    /**
     * @return 获取WiFi列表
     */
    public List<ScanResult> getWifiList() {
        List<ScanResult> resultList = new ArrayList<>();
        if (getWifiManager() != null && isWifiEnable()) {
            resultList.addAll(getWifiManager().getScanResults());
        }
        return resultList;
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
     * android8.0以上开启手机热点
     */
    private void startTethering() {
        try {
            Class classOnStartTetheringCallback = Class.forName("android.net.ConnectivityManager$OnStartTetheringCallback");
            Method startTethering = getConnectivityManager().getClass()
                    .getDeclaredMethod("startTethering", int.class, boolean.class, classOnStartTetheringCallback);
            /*Object proxy = ProxyBuilder.forClass(classOnStartTetheringCallback)
                    .handler(new InvocationHandler() {
                        @Override
                        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                            return null;
                        }
                    }).build();*/
            //todo
            Object proxy = null;
            startTethering.invoke(getConnectivityManager(), 0, false, proxy);
        } catch (Exception e) {
            Log.e(TAG, "打开热点失败");
            e.printStackTrace();
        }
    }

    /**
     * android8.0以上关闭手机热点
     */
    private void stopTethering() {
        try {
            Method stopTethering = getConnectivityManager()
                    .getClass().getDeclaredMethod("stopTethering", int.class);
            stopTethering.invoke(getConnectivityManager(), 0);
        } catch (Exception e) {
            Log.e(TAG, "关闭热点失败");
            e.printStackTrace();
        }
    }

    /**
     * wifi设置
     *
     * @param ssid     WIFI名称
     * @param pws      WIFI密码
     * @param isHasPws 是否有密码
     */
    private WifiConfiguration getWifiConfig(String ssid, String pws, boolean isHasPws) {
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

    /**
     * 得到配置好的网络连接
     *
     * @param ssid
     * @return
     */
    @SuppressLint("MissingPermission")
    private WifiConfiguration isExist(String ssid) {
        List<WifiConfiguration> configs = getConfiguredNetworks();
        if (configs == null) {
            Log.e(TAG, "isExist: null");
            return null;
        }
        for (WifiConfiguration config : configs) {
            if (config.SSID.equals("\"" + ssid + "\"")) {
                return config;
            }
        }
        return null;
    }

    private boolean isRegisterWifiBroadcast() {
        return wifiReceiver != null;
    }

    /**
     * 注册Wifi广播
     */
    public void registerWifiBroadcast(WifiStateListener wifiStateListener) {
        // 刚注册广播时会立即收到一条当前状态的广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
        wifiReceiver = new WifiReceiver();
        AppToolUtils.getApp().registerReceiver(wifiReceiver, filter);
        wifiReceiver.setWifiStateListener(wifiStateListener);
    }

    /**
     * 解除Wifi广播
     */
    public void unregisterWifiBroadcast() {
        if (isRegisterWifiBroadcast()) {
            AppToolUtils.getApp().unregisterReceiver(wifiReceiver);
            wifiReceiver.setWifiStateListener(null);
            wifiReceiver = null;
        }
    }

    /**
     * 资源释放
     */
    public void release() {
        super.release();
        wifiReceiver = null;
        System.gc();
    }

}
