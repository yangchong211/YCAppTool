package com.yc.networklib.setting;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.yc.toolutils.context.AppToolUtils;

import java.util.ArrayList;
import java.util.List;

public final class CapabilitiesUtils {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static List<NetworkCapabilities> getAllNetworkCapabilities() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                AppToolUtils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        Network[] networks = connectivityManager.getAllNetworks();
        List<NetworkCapabilities> list = new ArrayList<>();
        for (Network network : networks) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            list.add(capabilities);
        }
        return list;
    }

    public static String getNetworkName(int transportType) {
        if (transportType == NetworkCapabilities.TRANSPORT_WIFI) {
            return "WIFI";
        } else if (transportType == NetworkCapabilities.TRANSPORT_CELLULAR) {
            return "MOBILE";
        } else if (transportType == NetworkCapabilities.TRANSPORT_ETHERNET) {
            return "ETHERNET";
        } else {
            return "UNKNOWN";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static int getTransportTypeInt(NetworkCapabilities networkCapabilities) {
        if (networkCapabilities == null) {
            return -1;
        }
        //NetworkCapabilities 传输是网络运行的物理媒介的抽象形式。
        //常见的传输示例包括以太网、Wi-Fi 和移动网络。VPN 和点对点 Wi-Fi 也可以传输。
        //若要查找某个网络是否具有特定的传输
        //如下所示：请使用 hasTransport(int) 方法和其中一个 NetworkCapabilities.TRANSPORT_* 常量。
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return NetworkCapabilities.TRANSPORT_WIFI;
        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            return NetworkCapabilities.TRANSPORT_CELLULAR;
        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            return NetworkCapabilities.TRANSPORT_ETHERNET;
        }
        return -1;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static String getTransportTypeName(Network network) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                AppToolUtils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null || network == null) {
            return NetRequestHelper.UNKNOWN;
        }
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        if (networkCapabilities == null) {
            return NetRequestHelper.UNKNOWN;
        }
        int transportType = getTransportTypeInt(networkCapabilities);
        return getNetworkName(transportType);
    }

    /**
     * 判断是否是移动流量
     *
     * @return true表示是
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isActivityCellular() {
        return getTransportType() == NetworkCapabilities.TRANSPORT_CELLULAR;
    }

    /**
     * 判断是否是Wi-Fi
     *
     * @return true表示是
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isActivityWifi() {
        return getTransportType() == NetworkCapabilities.TRANSPORT_WIFI;
    }

    /**
     * 判断是否是以太网
     *
     * @return true表示是
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isActivityEthernet() {
        return getTransportType() == NetworkCapabilities.TRANSPORT_ETHERNET;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static int getTransportType() {
        int transportType = -2;
        ConnectivityManager connectivityManager = (ConnectivityManager)
                AppToolUtils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return transportType;
        }
        //Network 类表示设备连接到的一个网络。
        //如果网络连接中断，Network 对象将不再可用。即使设备之后重新连接到同一设备，新的 Network 对象也将表示新网络。
        //getActiveNetwork 使用此实例获取对应用当前默认网络的引用
        Network activeNetwork = connectivityManager.getActiveNetwork();
        if (activeNetwork == null) {
            return transportType;
        }
        //通过对网络的引用，您的应用可以请求有关网络的信息
        //NetworkCapabilities 对象包含有关网络属性的信息，例如传输方式（Wi-Fi、移动网络、蓝牙）以及网络能力。
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
        if (networkCapabilities != null) {
            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                transportType = getTransportTypeInt(networkCapabilities);
            }
        }
        return transportType;
    }

    public static boolean isConnected() {
        ConnectivityManager manager =
                (ConnectivityManager) AppToolUtils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        //NetworkInfo：描述指定类型的网络接口的状态（目前为移动网络或 Wi-Fi）。
        NetworkInfo info = manager.getActiveNetworkInfo();
        //确定这些网络接口是否可用（即，是否可以建立网络连接）
        return info != null && info.isConnected();
    }

    // PING命令 使用新进程使用默认网络 不会使用 networkCallback 绑定的通道  用来判断以太网或者WiFi是否可上外网非常不错
    public static boolean ping() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process p = runtime.exec("ping -c 1 -W 1 www.baidu.com");
            int ret = p.waitFor();
            return ret == 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}