package com.yc.appstatuslib.broadcast;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.yc.appstatuslib.AppStatusManager;

/**
 * <pre>
 *     @author: yangchong
 *     email  : yangchong211@163.com
 *     time   : 2017/5/18
 *     desc   : 网络监听广播
 *     revise :
 * </pre>
 */
public class NetWorkBroadcastReceiver extends BroadcastReceiver {

    private final AppStatusManager mManager;

    public NetWorkBroadcastReceiver(AppStatusManager resourceManager) {
        this.mManager = resourceManager;
    }

    public boolean isNetworkEnable(Context context) {
        if (context == null) {
            return false;
        } else {
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager == null) {
                return false;
            } else {
                NetworkInfo aActiveInfo = connectivityManager.getActiveNetworkInfo();
                return aActiveInfo != null && aActiveInfo.isAvailable();
            }
        }
    }


    public void onReceive(Context context, Intent intent) {
        if (this.mManager != null) {
            if (!this.isNetworkEnable(context)) {
                this.mManager.dispatcherNetworkState(false);
            } else {
                this.mManager.dispatcherNetworkState(true);
            }
        }
    }
}

