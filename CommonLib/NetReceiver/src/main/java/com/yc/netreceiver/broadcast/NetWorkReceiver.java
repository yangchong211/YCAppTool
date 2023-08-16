package com.yc.netreceiver.broadcast;

import static com.yc.netreceiver.OnNetStatusListener.NET_ETHERNET;
import static com.yc.netreceiver.OnNetStatusListener.NET_MOBILE;
import static com.yc.netreceiver.OnNetStatusListener.NET_NONE;
import static com.yc.netreceiver.OnNetStatusListener.NET_WIFI;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.yc.appcontextlib.AppToolUtils;

public class NetWorkReceiver extends BroadcastReceiver {

    private static final String TAG = "NetWork-Receiver";
    private ConnectivityManager connectivityManager;
    private final NetWorkManager mManager;

    public NetWorkReceiver(NetWorkManager resourceManager) {
        this.mManager = resourceManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int networkAvailable = isNetworkAvailable();
            Log.i(TAG, "网络类型：" + networkAvailable);
            switch (networkAvailable) {
                case 1:
                    mManager.changeNetStatus(NET_ETHERNET, isAvailable());
                    break;
                case 2:
                    mManager.changeNetStatus(NET_WIFI, isAvailable());
                    break;
                case 3:
                    mManager.changeNetStatus(NET_MOBILE, isAvailable());
                    break;
                default:
                    mManager.changeNetStatus(NET_NONE, isAvailable());
                    break;
            }
        }
    }

    private int isNetworkAvailable() {
        if (connectivityManager == null) {
            connectivityManager = (ConnectivityManager)
                    AppToolUtils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        NetworkInfo ethNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (ethNetInfo != null && ethNetInfo.isConnected()) {
            return NET_ETHERNET;
        } else if (wifiNetInfo != null && wifiNetInfo.isConnected()) {
            return NET_WIFI;
        } else if (mobileNetInfo != null && mobileNetInfo.isConnected()) {
            return NET_MOBILE;
        } else {
            return NET_NONE;
        }
    }

    /**
     * 返回网络状态，判断网络是否可用
     *
     * @return 是否可用
     */
    private boolean isAvailable() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }

    /**
     * 获取活动网络信息
     * <p>需添加权限
     * {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}</p>
     *
     * @return NetworkInfo
     */
    @SuppressLint("MissingPermission")
    private NetworkInfo getActiveNetworkInfo() {
        if (connectivityManager == null) {
            connectivityManager = (ConnectivityManager)
                    AppToolUtils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        if (connectivityManager == null) {
            return null;
        }
        return connectivityManager.getActiveNetworkInfo();
    }
}
