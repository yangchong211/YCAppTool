package com.yc.netreceiver;

public interface IStatusListener {

    void registerNetStatusListener(OnNetStatusListener listener);

    boolean unregisterNetStatusListener(OnNetStatusListener listener);

    void destroy();
}
