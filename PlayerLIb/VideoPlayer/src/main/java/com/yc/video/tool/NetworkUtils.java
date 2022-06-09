package com.yc.video.tool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/5/21
 *     desc  : 网络状态监听工具类
 *     revise:
 * </pre>
 */
public class NetworkUtils {


    /**
     * 标记当前网络状态，分别是：移动数据、Wifi、未连接、网络状态已公布
     */
    public enum State {
        MOBILE, WIFI, UN_CONNECTED, PUBLISHED
    }

    /**
     * 为了避免因多次接收到广播反复提醒的情况而设置的标志位，用于缓存收到新的广播前的网络状态
     */
    private static State tempState;

    /**
     * 获取当前网络连接状态
     *
     * @param context Context
     * @return 网络状态
     */
    public static State getConnectState(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (manager != null) {
            networkInfo = manager.getActiveNetworkInfo();
        }
        State state = State.UN_CONNECTED;
        if (networkInfo != null && networkInfo.isAvailable()) {
            if (isMobileConnected(context)) {
                state = State.MOBILE;
            } else if (isWifiConnected(context)) {
                state = State.WIFI;
            }
        }
        if (state.equals(tempState)) {
            return State.PUBLISHED;
        }
        tempState = state;
        return state;
    }

    private static boolean isMobileConnected(Context context) {
        return isConnected(context, ConnectivityManager.TYPE_MOBILE);
    }

    private static boolean isWifiConnected(Context context) {
        return isConnected(context, ConnectivityManager.TYPE_WIFI);
    }

    private static boolean isConnected(Context context, int type) {
        //getAllNetworkInfo() 在 API 23 中被弃用
        //getAllNetworks() 在 API 21 中才添加
        ConnectivityManager manager = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            NetworkInfo[] allNetworkInfo = new NetworkInfo[0];
            if (manager != null) {
                allNetworkInfo = manager.getAllNetworkInfo();
            }
            for (NetworkInfo info : allNetworkInfo) {
                if (info.getType() == type) {
                    return info.isAvailable();
                }
            }
        } else {
            Network[] networks = new Network[0];
            if (manager != null) {
                networks = manager.getAllNetworks();
            }
            for (Network network : networks) {
                NetworkInfo networkInfo = manager.getNetworkInfo(network);
                if (networkInfo.getType() == type) {
                    return networkInfo.isAvailable();
                }
            }
        }
        return false;
    }


    public static final int NO_NETWORK = 0;
    public static final int NETWORK_CLOSED = 1;
    public static final int NETWORK_ETHERNET = 2;
    public static final int NETWORK_WIFI = 3;
    public static final int NETWORK_MOBILE = 4;
    public static final int NETWORK_UNKNOWN = -1;

    /**
     * 判断当前网络类型
     */
    public static int getNetworkType(Context context) {
        //改为context.getApplicationContext()，防止在Android 6.0上发生内存泄漏
        ConnectivityManager connectMgr = (ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectMgr == null) {
            return NO_NETWORK;
        }

        NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            // 没有任何网络
            return NO_NETWORK;
        }
        if (!networkInfo.isConnected()) {
            // 网络断开或关闭
            return NETWORK_CLOSED;
        }
        if (networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
            // 以太网网络
            return NETWORK_ETHERNET;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            // wifi网络，当激活时，默认情况下，所有的数据流量将使用此连接
            return NETWORK_WIFI;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            // 移动数据连接,不能与连接共存,如果wifi打开，则自动关闭
            switch (networkInfo.getSubtype()) {
                // 2G
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    // 3G
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    // 4G
                case TelephonyManager.NETWORK_TYPE_LTE:
                    // 5G
                    return NETWORK_MOBILE;
            }
        }
        // 未知网络
        return NETWORK_UNKNOWN;
    }

}
