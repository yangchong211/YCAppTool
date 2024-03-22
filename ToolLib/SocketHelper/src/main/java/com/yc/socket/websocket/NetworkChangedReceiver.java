package com.yc.socket.websocket;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * 监听网络变化广播，网络变化时自动重连
 */
public class NetworkChangedReceiver extends BroadcastReceiver {

    private static final String TAG = "NetworkChangedReceiver";

    public NetworkChangedReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager == null) return;
            try {
                if (checkPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)) {
                    NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
                    if (activeNetwork != null) {
                        if (activeNetwork.isConnected()) {
                            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                                WebSocketLog.i(TAG, "网络连接发生变化，当前WiFi连接可用");
                            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                                WebSocketLog.i(TAG, "网络连接发生变化，当前移动连接可用");
                            }
                            if (WebSocketClientManager.getInstance().needAutoRetryConnect()) {
                                WebSocketLog.i(TAG, "需要重连");
                                WebSocketClientManager.getInstance().reconnect();
                            } else {
                                WebSocketLog.i(TAG, "不需要重连");
                            }
                        } else {
                            WebSocketLog.i(TAG, "当前没有可用网络");
                        }
                    }
                }
            } catch (Exception e) {
                WebSocketLog.e(TAG, "网络状态获取错误", e);
            }
        }
    }

    /**
     * 判断是否有权限
     */
    private static boolean checkPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return PackageManager.PERMISSION_GRANTED == context.getPackageManager()
                    .checkPermission(permission, context.getPackageName());
        }
        return true;
    }

}
