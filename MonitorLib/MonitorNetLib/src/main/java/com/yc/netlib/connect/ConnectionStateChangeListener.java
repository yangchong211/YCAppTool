package com.yc.netlib.connect;

public interface ConnectionStateChangeListener {
    void onBandwidthStateChange(ConnectionQuality bandwidthState);
}
