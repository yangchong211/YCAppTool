package com.yc.networklib.setting;

import android.net.Network;
import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class DefaultNetworkCallback extends BaseNetworkCallback {

    private final NetRequestHelper onDefaultCallback;

    public DefaultNetworkCallback(NetRequestHelper callback) {
        this.onDefaultCallback = callback;
    }

    /**
     * 对于通过 registerDefaultNetworkCallback() 注册的回调
     * 新网络成为默认网络后，应用会收到对新网络的 onAvailable(Network) 的调用。
     *
     * @param network The {@link Network} of the satisfying network.
     */
    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        if (onDefaultCallback != null) {
            String transportTypeInt = CapabilitiesUtils.getTransportTypeName(network);
            onDefaultCallback.changeDefaultNetworkStatus(transportTypeInt, true);
        }
    }
}
