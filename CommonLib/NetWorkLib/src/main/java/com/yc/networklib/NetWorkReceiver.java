package com.yc.networklib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetWorkReceiver extends BroadcastReceiver {

    private static final String TAG = "NetWork-Receiver";
    public int NET_ETHERNET = 1;
    public int NET_WIFI = 2;
    public int NET_MOBILE = 3;
    public int NET_NONE = 0;

    private final NetWorkManager mManager;

    public NetWorkReceiver(NetWorkManager resourceManager) {
        this.mManager = resourceManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int networkAvailable = isNetworkAvailable(context);
            Log.i(TAG, "网络类型：" + networkAvailable);
            switch (networkAvailable) {
                case 1:
                    Log.d(TAG, " network 有网线");
                    mManager.changeNetStatus(NET_ETHERNET, AppNetworkUtils.isAvailable());
                    break;
                case 2:
                    Log.d(TAG, " network Wi-Fi");
                    mManager.changeNetStatus(NET_WIFI, AppNetworkUtils.isAvailable());
                    break;
                case 3:
                    Log.d(TAG, " network 移动数据");
                    mManager.changeNetStatus(NET_MOBILE, AppNetworkUtils.isAvailable());
                    break;
                default:
                    mManager.changeNetStatus(NET_NONE, AppNetworkUtils.isAvailable());
                    break;
            }
        }
    }

    private int isNetworkAvailable(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ethNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
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
}
