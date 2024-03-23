package com.yc.networklib.wifi;

import static android.os.Build.VERSION_CODES.P;
import static com.yc.networklib.wifi.WifiHelper.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.yc.toolutils.context.AppToolUtils;
import com.yc.toolutils.system.AppLogUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class WifiToolUtils {
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    /**
     * 请求修改系统设置Code
     */
    public static final int REQUEST_WRITE_SETTING_CODE = 1001;

    /**
     * 查看授权情况, 开启热点需要申请系统设置修改权限，如有必要，可提前申请
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    static void requestWriteSettings(Activity activity) {
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
    static boolean isGrantedWriteSettings(Context context) {
        return Settings.System.canWrite(context);
    }

    /**
     * 获取Wi-Fi信号强度
     *
     * @param wifiRssi 信号强度
     * @return 信号强度
     */
    static String getWifiLevel(int wifiRssi) {
        //获取wifi信号强度
        if (wifiRssi > -50 && wifiRssi < 0) {
            //最强
            return "最强";
        } else if (wifiRssi > -70 && wifiRssi < -50) {
            //较强
            return "较强";
        } else if (wifiRssi > -80 && wifiRssi < -70) {
            //较弱
            return "较弱";
        } else if (wifiRssi > -100 && wifiRssi < -80) {
            //微弱
            return "微弱";
        } else {
            return "微弱";
        }
    }

    public static WifiModeEnum getSecurityMode(String ssid) {
        List<ScanResult> wifiList = getWifiList();
        if (wifiList.size() > 0) {
            for (ScanResult scanResult : wifiList) {
                if (scanResult == null) {
                    continue;
                }
                if (ssid.equals(scanResult.SSID)) {
                    return getSecurityMode(scanResult);
                }
            }
        }
        return WifiModeEnum.OPEN;
    }

    /**
     * 获取WIFI的加密方式
     *
     * @param scanResult WIFI信息
     * @return 加密方式
     */
    public static WifiModeEnum getSecurityMode(ScanResult scanResult) {
        if (scanResult == null) {
            return WifiModeEnum.OPEN;
        }
        String capabilities = scanResult.capabilities;
        AppLogUtils.d("Network-Wifi:", "getSecurityMode " + capabilities);
        if (capabilities == null || capabilities.isEmpty()) {
            return WifiModeEnum.OPEN;
        }
        //通过判断capabilities字段是否包含对应的string来判断属于何种保护方式
        if (capabilities.contains("WPA2")) {
            return WifiModeEnum.WPA2;
        } else if (capabilities.contains("WPA")) {
            return WifiModeEnum.WPA;
        } else if (capabilities.contains("WEP")) {
            return WifiModeEnum.WEP;
        } else if (capabilities.contains("EAP")) {
            return WifiModeEnum.WEP;
        } else {
            // 没有加密
            return WifiModeEnum.OPEN;
        }
    }


    /**
     * 添加双引号
     * 这里需要注意的是：
     * WifiConfiguration里封装的wifi的SSID是包含双引号的，而ScanResult的SSID是不包含双引号的，这里比较时需要注意！
     *
     * @param text 待处理字符串
     * @return 处理后字符串
     */
    public static String addDoubleQuotation(String text) {
        //直接判断equals(ssid)肯定是不存在的，因为“XXX_WIFI”才是配置中的SSID，它外面包了双引号
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        return "\"" + text + "\"";
    }


    /**
     * 利用HashSet不能添加重复数据的特性
     *
     * @param list list集合
     */
    public static List<ScanResult> removeDuplicate(List<ScanResult> list) {
        HashSet<ScanResult> set = new HashSet<>(list.size());
        List<ScanResult> result = new ArrayList<>(list.size());
        for (ScanResult item : list) {
            if (set.add(item)) {
                result.add(item);
            }
        }
        list.clear();
        list.addAll(result);
        return list;
    }


    /**
     * 关闭便携热点
     */
    public static void closeAp(Activity activity) {
        // 6.0+申请修改系统设置权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!WifiToolUtils.isGrantedWriteSettings(AppToolUtils.getApp())) {
                WifiToolUtils.requestWriteSettings(activity);
            }
        }
        // 8.0以上的关闭方式不一样
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopTethering();
        } else {
            try {
                WifiHelper instance = WifiHelper.getInstance();
                Method method = instance.getWifiManager().getClass().getMethod("setWifiApEnabled",
                        WifiConfiguration.class, boolean.class);
                method.setAccessible(true);
                method.invoke(instance.getWifiManager(), null, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * android8.0以上关闭手机热点
     */
    private static void stopTethering() {
        try {
            WifiHelper instance = WifiHelper.getInstance();
            Method stopTethering = instance.getConnectivityManager()
                    .getClass().getDeclaredMethod("stopTethering", int.class);
            stopTethering.invoke(instance.getConnectivityManager(), 0);
        } catch (Exception e) {
            Log.e(TAG, "关闭热点失败");
            e.printStackTrace();
        }
    }


    /**
     * 开启便携热点
     *
     * @param activity activity
     * @param ssid     便携热点SSID
     * @param password 便携热点密码
     */
    public static void openAp(Activity activity, String ssid, String password) {
        WifiHelper instance = WifiHelper.getInstance();
        // 6.0+申请修改系统设置权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!WifiToolUtils.isGrantedWriteSettings(activity)) {
                WifiToolUtils.requestWriteSettings(activity);
            }
        }
        // 9.0以下版本不支持热点和WiFi共存
        if (Build.VERSION.SDK_INT <= P) {
            // 关闭WiFi
            if (instance.isWifiEnable()) {
                instance.getWifiManager().setWifiEnabled(false);
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
                Method method = instance.getWifiManager().getClass().getMethod("setWifiApEnabled",
                        WifiConfiguration.class, Boolean.TYPE);
                // 返回热点打开状态
                method.invoke(instance.getWifiManager(), config, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * android8.0以上开启手机热点
     */
    private static void startTethering() {
        try {
            WifiHelper instance = WifiHelper.getInstance();
            Class classOnStartTetheringCallback = Class.forName("android.net.ConnectivityManager$OnStartTetheringCallback");
            Method startTethering = instance.getConnectivityManager().getClass()
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
            startTethering.invoke(instance.getConnectivityManager(), 0, false, proxy);
        } catch (Exception e) {
            Log.e(TAG, "打开热点失败");
            e.printStackTrace();
        }
    }


    /**
     * 连接WIFI,密码为空则不使用密码连接
     * 9.0以下设备需要关闭热点
     *
     * @param ssid     wifi名称
     * @param password wifi密码
     */
    public static void connectWifi(Activity activity, String ssid, String password) {
        WifiHelper instance = WifiHelper.getInstance();
        // 9.0以下系统不支持关闭热点和WiFi共存
        if (Build.VERSION.SDK_INT <= P) {
            if (isApEnable()) {
                WifiToolUtils.closeAp(activity);
            }
        }
        // 打开WiFi
        if (!instance.isWifiEnable()) {
            instance.openWifi();
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
                while (!instance.getWifiManager().isWifiEnabled()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                instance.getWifiManager().disableNetwork(
                        instance.getWifiManager().getConnectionInfo().getNetworkId());
                int netId = instance.getWifiManager().addNetwork(
                        instance.getWifiConfig(ssid, password, !TextUtils.isEmpty(password)));
                instance.getWifiManager().enableNetwork(netId, true);
            }
        });
    }


    /**
     * 便携热点是否开启
     */
    public static boolean isApEnable() {
        WifiHelper instance = WifiHelper.getInstance();
        try {
            Method method = instance.getWifiManager().getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (boolean) method.invoke(instance.getWifiManager());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 使用 WifiManager.getScanResults() 获取扫描结果。
     *
     * @return 获取WiFi列表
     */
    static List<ScanResult> getWifiList() {
        WifiHelper instance = WifiHelper.getInstance();
        List<ScanResult> resultList = new ArrayList<>();
        //系统返回的扫描结果为最近更新的结果，但如果当前扫描尚未完成或成功，可能会返回以前扫描的结果。
        //也就是说，如果在收到成功的 SCAN_RESULTS_AVAILABLE_ACTION 广播前调用此方法，您可能会获得较旧的扫描结果。
        if (instance.getWifiManager() != null && instance.isWifiEnable()
                && instance.getWifiManager().getScanResults() != null) {
            //resultList.addAll(getWifiManager().getScanResults());
            //去掉空数据
            for (ScanResult scanResult : instance.getWifiManager().getScanResults()) {
                if (scanResult == null) {
                    continue;
                }
                //使用List集合contains方法循环遍历
                boolean contains = resultList.contains(scanResult);
                if (!contains && !scanResult.SSID.isEmpty()) {
                    resultList.add(scanResult);
                }
            }
        }
        return resultList;
    }

    /**
     * 获取wifi的ip
     *
     * @return ip
     */
    static int getWifiIp() {
        WifiHelper instance = WifiHelper.getInstance();
        if (instance.getWifiManager() != null) {
            WifiInfo wifiInfo = instance.getWifiManager().getConnectionInfo();
            return wifiInfo.getIpAddress();
        }
        return -1;
    }


    /**
     * 将ip数值，转为字符串
     *
     * @param paramInt ip整数
     * @return 字符串ip地址
     */
    public static String intToIp(int paramInt) {
        return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "."
                + (0xFF & paramInt >> 16) + "." + (0xFF & paramInt >> 24);
    }


}
