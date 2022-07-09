package com.yc.monitorspeed;

public interface ConnectionStateChangeListener {
    void onBandwidthStateChange(ConnectionQuality bandwidthState);
}
