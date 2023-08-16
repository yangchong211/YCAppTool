package com.yc.netsettinglib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.yc.appcontextlib.AppToolUtils;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {

    private static final String TAG = "NetWork-Callback";
    private final NetRequestHelper onNetCallback;

    public NetworkCallbackImpl(NetRequestHelper callback) {
        this.onNetCallback = callback;
    }

    /**
     * 当网络连接可用时回调
     *
     * @param network The {@link Network} of the satisfying network.
     */
    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        //网络连接
        String transportTypeInt = getTransportTypeInt(network);
        String transportTypeName = CapabilitiesUtils.getTransportTypeName(network);
        Log.i(TAG, "onAvailable 网络类型：" + transportTypeInt + " , " + transportTypeName);
        if (onNetCallback != null) {
            onNetCallback.changeNetStatus(transportTypeInt, true);
        }
    }

    /**
     * 当网络断开时回调
     *
     * @param network The {@link Network} lost.
     */
    @Override
    public void onLost(Network network) {
        super.onLost(network);
        //网络已断开
        String transportTypeInt = getTransportTypeInt(network);
        String transportTypeName = CapabilitiesUtils.getTransportTypeName(network);
        Log.i(TAG, "onLost 网络类型：" + transportTypeInt + " , " + transportTypeName);
        if (onNetCallback != null) {
            onNetCallback.changeNetStatus(transportTypeInt, false);
        }
    }

    /**
     * 当网络属性变化时回调
     * @param network The {@link Network} whose capabilities have changed.
     * @param networkCapabilities The new {@link android.net.NetworkCapabilities} for this
     *                            network.
     */
    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        String transportTypeName =  CapabilitiesUtils.getTransportTypeName(network);
        int transportTypeInt =  CapabilitiesUtils.getTransportTypeInt(networkCapabilities);
        String networkName =  CapabilitiesUtils.getNetworkName(transportTypeInt);
        Log.i(TAG, "onCapabilitiesChanged 网络类型 " + transportTypeName + " , " + networkName);
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            int transportType = getTransportType(networkCapabilities);
            String type;
            if (transportType == NetworkCapabilities.TRANSPORT_WIFI) {
                type =  NetRequestHelper.WIFI;
            } else if (transportType == NetworkCapabilities.TRANSPORT_CELLULAR) {
                type =  NetRequestHelper.MOBILE;
            } else if (transportType == NetworkCapabilities.TRANSPORT_ETHERNET) {
                type =  NetRequestHelper.ETHERNET;
            } else {
                type =  NetRequestHelper.UNKNOWN;
            }
            Log.i(TAG, "onCapabilitiesChanged type：" + type + " , " + network.toString());
            if (onNetCallback != null) {
                onNetCallback.changeNetStatus(type, true);
            }
        }
    }

    @Override
    public void onUnavailable() {
        super.onUnavailable();
        Log.i(TAG, "onUnavailable");
    }

    @Override
    public void onLinkPropertiesChanged(@NonNull Network network, @NonNull LinkProperties linkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static String getTransportTypeInt(Network network) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                AppToolUtils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        int transportTypeInt = getTransportType(networkCapabilities);
        if (transportTypeInt == NetworkCapabilities.TRANSPORT_WIFI) {
            return NetRequestHelper.WIFI;
        } else if (transportTypeInt == NetworkCapabilities.TRANSPORT_CELLULAR) {
            return NetRequestHelper.MOBILE;
        } else if (transportTypeInt == NetworkCapabilities.TRANSPORT_ETHERNET) {
            return NetRequestHelper.ETHERNET;
        }
        return NetRequestHelper.UNKNOWN;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static int getTransportType(NetworkCapabilities networkCapabilities) {
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
}
