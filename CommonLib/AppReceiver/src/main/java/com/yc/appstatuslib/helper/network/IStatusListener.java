package com.yc.appstatuslib.helper.network;

public interface IStatusListener {

    void registerNetStatusListener(OnNetStatusListener listener);

    boolean unregisterNetStatusListener(OnNetStatusListener listener);

    void destroy();
}
