package com.yc.netreceiver.callback;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.yc.appcontextlib.AppToolUtils;
import com.yc.netreceiver.OnNetStatusListener;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {

    private final NetworkHelper onNetCallback;

    public NetworkCallbackImpl(NetworkHelper callback) {
        this.onNetCallback = callback;
    }

    /**
     * 网络已经连接了，但不一定可用
     *
     * @param network The {@link Network} of the satisfying network.
     */
    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        //网络连接
        int transportTypeInt = getTransportTypeInt(network);
        if (onNetCallback != null) {
            onNetCallback.changeNetStatus(transportTypeInt,false);
        }
    }

    /**
     * 网络断开
     *
     * @param network The {@link Network} lost.
     */
    @Override
    public void onLost(Network network) {
        super.onLost(network);
        //网络已断开
        int transportTypeInt = getTransportTypeInt(network);
        if (onNetCallback != null) {
            onNetCallback.changeNetStatus(transportTypeInt,false);
        }
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                //wifi已经连接
                if (onNetCallback != null) {
                    onNetCallback.changeNetStatus(OnNetStatusListener.NET_WIFI,true);
                }
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                //数据流量已经连接
                if (onNetCallback != null) {
                    onNetCallback.changeNetStatus(OnNetStatusListener.NET_MOBILE,true);
                }
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                //以太网已经连接
                if (onNetCallback != null) {
                    onNetCallback.changeNetStatus(OnNetStatusListener.NET_ETHERNET,true);
                }
            } else {
                //其他网络
                if (onNetCallback != null) {
                    onNetCallback.changeNetStatus(OnNetStatusListener.NET_NONE,true);
                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static int getTransportTypeInt(Network network) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                AppToolUtils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return OnNetStatusListener.NET_WIFI;
        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            return OnNetStatusListener.NET_MOBILE;
        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            return OnNetStatusListener.NET_ETHERNET;
        }
        return OnNetStatusListener.NET_NONE;
    }

}
