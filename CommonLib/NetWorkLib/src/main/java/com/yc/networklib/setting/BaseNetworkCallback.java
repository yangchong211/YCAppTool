package com.yc.networklib.setting;

import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class BaseNetworkCallback extends ConnectivityManager.NetworkCallback {

    protected static final String TAG = "NetWork-Callback";


    /**
     * 当网络连接可用时回调
     *
     * @param network The {@link Network} of the satisfying network.
     */
    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        //网络连接
        String transportTypeInt = CapabilitiesUtils.getTransportTypeName(network);
        String transportTypeName = CapabilitiesUtils.getTransportTypeName(network);
        Log.i(TAG, "onAvailable 网络类型：" + transportTypeInt + " , "
                + transportTypeName + " , " + network);
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
        String transportTypeInt = CapabilitiesUtils.getTransportTypeName(network);
        String transportTypeName = CapabilitiesUtils.getTransportTypeName(network);
        Log.i(TAG, "onLost 网络类型：" + transportTypeInt + " , "
                + transportTypeName + " , " + network);
    }

    /**
     * 当网络属性变化时回调
     *
     * @param network             The {@link Network} whose capabilities have changed.
     * @param networkCapabilities The new {@link android.net.NetworkCapabilities} for this
     *                            network.
     */
    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        String transportTypeName = CapabilitiesUtils.getTransportTypeName(network);
        int transportTypeInt = CapabilitiesUtils.getTransportTypeInt(networkCapabilities);
        String networkName = CapabilitiesUtils.getNetworkName(transportTypeInt);
        //Log.i(TAG, "onCapabilitiesChanged 网络类型 " + transportTypeName + " , "
        //        + networkName + " , " + network);
    }

    @Override
    public void onUnavailable() {
        super.onUnavailable();
        Log.i(TAG, "onUnavailable");
    }

    @Override
    public void onLinkPropertiesChanged(@NonNull Network network,
                                        @NonNull LinkProperties linkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties);
        Log.i(TAG, "onLinkPropertiesChanged" + " , " + network);
    }
}
