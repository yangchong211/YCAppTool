package com.yc.networklib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.annotation.StringDef;

import com.yc.appcontextlib.AppToolUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class NetWorkUtils {

    public static final String TAG = NetWorkUtils.class.getSimpleName();
    public static volatile boolean isActivityConnected = true;
    public static volatile boolean isConnected = true;
    private static final ScheduledExecutorService executorService =
            Executors.newSingleThreadScheduledExecutor();

    public static final String UNKNOWN = "UNKNOWN";
    public static final String MOBILE = "MOBILE";
    public static final String ETHERNET = "ETHERNET";
    public static final String WIFI = "WIFI";

    @StringDef({UNKNOWN, MOBILE, ETHERNET, WIFI})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NetworkType {
    }

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
    private static int getTransportTypeInt(NetworkCapabilities networkCapabilities) {
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return NetworkCapabilities.TRANSPORT_WIFI;
            //post(NetType.WIFI);
        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            return NetworkCapabilities.TRANSPORT_CELLULAR;
            //post(NetType.CMWAP);
        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            return NetworkCapabilities.TRANSPORT_ETHERNET;
            //post(NetType.AUTO);
        }
        return -1;
    }

    /**
     * 以太网连上但不能上外网时，程序已经绑定了蜂窝网通道上网，此方法判断是否实际使用蜂窝网上网
     *
     * @return true表示是
     */
    public static boolean isRealCellular() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean activityCellular = isActivityCellular();
            if (activityCellular) {
                return true;
            }
            return !isActivityConnected;
        }
        return true;
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
        Network activeNetwork = connectivityManager.getActiveNetwork();
        if (activeNetwork == null) {
            isConnected = false;
        } else {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                isConnected = true;
                transportType = getTransportTypeInt(networkCapabilities);
            } else {
                isConnected = false;
            }
        }
        return transportType;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static String getTransportTypeName() {
        int transportType = getTransportType();
        return getNetworkName(transportType);
    }

    /**
     * 指定某个请求采用指定的网络进行发送
     *
     * @param type 类型
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void requestNetwork(@NetworkType String type) {
        final ConnectivityManager connectivityManager = (ConnectivityManager)
                AppToolUtils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        if (MOBILE.equals(type)) {
            // 设置指定的网络传输类型(蜂窝传输) 等于手机网络
            builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);
        } else if (WIFI.equals(type)) {
            // 设置指定的网络传输类型是Wi-Fi
            builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
        } else if (ETHERNET.equals(type)) {
            // 设置指定的网络传输类型是以太网
            builder.addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET);
        }
        // 设置感兴趣的网络功能
        //builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        // 设置感兴趣的网络：计费网络
        //builder.addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED);
        NetworkRequest networkRequest = builder.build();
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                Log.i(TAG, "已根据功能和传输类型找到合适的网络");
                if (Build.VERSION.SDK_INT >= 23) {
                    connectivityManager.bindProcessToNetwork(network);
                } else {
                    // 23后这个方法舍弃了
                    ConnectivityManager.setProcessDefaultNetwork(network);
                }
            }
        };
        final boolean[] chanelFlag = {true};
        executorService.scheduleWithFixedDelay(() -> {
            try {
                //网络都没连接直接返回
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (isActivityCellular()) {
                        return;
                    }
                }
                if (!isConnected) {
                    return;
                }
                isActivityConnected = ping();
                if (isActivityConnected) {
                    if (chanelFlag[0]) {
                        return;
                    }
                    chanelFlag[0] = true;
                    //1.以太网络可上外网时改为默认优先级上网
                    if (Build.VERSION.SDK_INT >= 23) {
                        connectivityManager.bindProcessToNetwork(null);
                    } else {
                        ConnectivityManager.setProcessDefaultNetwork(null);
                    }
                    connectivityManager.unregisterNetworkCallback(networkCallback);
                } else if (chanelFlag[0]) {
                    //2.以太网络不可上外网时自动切换蜂窝网
                    connectivityManager.requestNetwork(networkRequest, networkCallback);
                    chanelFlag[0] = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 1, 5, TimeUnit.SECONDS);
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