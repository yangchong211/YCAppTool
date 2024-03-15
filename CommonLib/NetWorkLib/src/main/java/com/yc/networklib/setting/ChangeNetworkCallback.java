package com.yc.networklib.setting;

import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ChangeNetworkCallback extends BaseNetworkCallback {

    private final NetRequestHelper onNetCallback;

    public ChangeNetworkCallback(NetRequestHelper callback) {
        this.onNetCallback = callback;
    }

    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        if (onNetCallback != null) {
            String transportTypeInt = CapabilitiesUtils.getTransportTypeName(network);
            onNetCallback.changeNetStatus(transportTypeInt, true);
        }
    }

    @Override
    public void onLost(Network network) {
        super.onLost(network);
        if (onNetCallback != null) {
            String transportTypeInt = CapabilitiesUtils.getTransportTypeName(network);
            onNetCallback.changeNetStatus(transportTypeInt, false);
        }
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            int transportType = CapabilitiesUtils.getTransportTypeInt(networkCapabilities);
            String type;
            if (transportType == NetworkCapabilities.TRANSPORT_WIFI) {
                type = NetRequestHelper.WIFI;
            } else if (transportType == NetworkCapabilities.TRANSPORT_CELLULAR) {
                type = NetRequestHelper.MOBILE;
            } else if (transportType == NetworkCapabilities.TRANSPORT_ETHERNET) {
                type = NetRequestHelper.ETHERNET;
            } else {
                type = NetRequestHelper.UNKNOWN;
            }
            if (onNetCallback != null) {
                onNetCallback.changeNetStatus(type, true);
            }
        }
    }
}
