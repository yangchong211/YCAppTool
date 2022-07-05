package com.yc.logclient.state;

/**
 * LogService连接状态
 */
public enum ConnectingState {
    /**
     * 未连接
     */
    NotConnected(0),
    /**
     * 连接中
     */
    Connecting(1),
    /**
     * 连接成功
     */
    Connected(2);

    int value;

    ConnectingState(int v) {
        this.value = v;
    }

}
