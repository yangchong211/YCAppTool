package com.yc.networklib.setting;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.yc.toolutils.context.AppToolUtils;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class RequestNetworkCallback extends BaseNetworkCallback {

    private final NetRequestHelper onRequestCallback;
    private boolean isBindNetwork = false;

    public RequestNetworkCallback(NetRequestHelper callback) {
        this.onRequestCallback = callback;
    }

    /**
     * 新网络成为默认网络后，应用会收到对新网络的 onAvailable(Network) 的调用。
     *
     * @param network The {@link Network} of the satisfying network.
     */
    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        String transportTypeName = CapabilitiesUtils.getTransportTypeName(network);
        Log.i(TAG, "onAvailable 找到合适的网络 " + transportTypeName);
        //使用所选网络
        if (Build.VERSION.SDK_INT >= 23) {
            //获取ConnectivityManager
            final ConnectivityManager connectivityManager = (ConnectivityManager)
                    AppToolUtils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
            boolean processToNetwork = connectivityManager.bindProcessToNetwork(network);
            isBindNetwork = processToNetwork;
            Log.i(TAG, "bindProcessToNetwork , " + processToNetwork);
        } else {
            // 23后这个方法舍弃了
            boolean defaultNetwork = ConnectivityManager.setProcessDefaultNetwork(network);
            isBindNetwork = defaultNetwork;
            Log.i(TAG, "setProcessDefaultNetwork , " + defaultNetwork);
        }
    }

    public boolean isBindNetwork() {
        return isBindNetwork;
    }
}
