package com.yc.socket.websocket;

import android.Manifest;

import java.util.Map;

/**
 * WebSocket 使用配置
 * Created by yangchong on 2018/6/26.
 */
public class WebSocketSetting {

    /**
     * WebSocket 连接地址
     */
    private String connectUrl;
    /**
     * 设置网络连接变化后是否自动重连。
     */
    private boolean reconnectWithNetworkChanged = true;
    /**
     * 设置心跳间隔时间
     */
    private int connectionLostTimeout = 50;
    /**
     * 设置连接的请求头
     */
    private Map<String, String> httpHeaders;
    /**
     * 重连次数，默认为：100 次
     */
    private int reconnectFrequency = 100;

    /**
     * 获取 WebSocket 链接地址
     */
    public String getConnectUrl() {
        return this.connectUrl;
    }

    /**
     * 设置 WebSocket 连接地址，必须设置项。
     */
    public void setConnectUrl(String connectUrl) {
        this.connectUrl = connectUrl;
    }


    /**
     * @see #setReconnectWithNetworkChanged(boolean)
     */
    public boolean reconnectWithNetworkChanged() {
        return this.reconnectWithNetworkChanged;
    }

    /**
     * 设置网络连接变化后是否自动重连。</br>
     * 如果设置 true 则需要添加申请 {@link Manifest.permission#ACCESS_NETWORK_STATE} 权限。
     * 需要注意的是，如果希望网络连接发生变化后重新连接，
     * 需要注册监听网络变化的广播，框架中已经实现了这个广播：{@link NetworkChangedReceiver}。
     * 但是需要手动注册，
     * 你可以调用  方法注册，
     * 也可以在 manifest 中注册，或者自己注册。
     */
    public void setReconnectWithNetworkChanged(boolean reconnectWithNetworkChanged) {
        this.reconnectWithNetworkChanged = reconnectWithNetworkChanged;
    }

    /**
     * 获取心跳间隔时间
     */
    public int getConnectionLostTimeout() {
        return connectionLostTimeout;
    }

    /**
     * 设置心跳间隔时间，单位为秒；
     * 默认为 60 s。
     */
    public void setConnectionLostTimeout(int connectionLostTimeout) {
        this.connectionLostTimeout = connectionLostTimeout;
    }


    /**
     * @see #setReconnectFrequency(int)
     */
    public int getReconnectFrequency() {
        return reconnectFrequency;
    }

    /**
     * 设置连接断开后的重连次数，
     * 默认为 10 次
     */
    public void setReconnectFrequency(int reconnectFrequency) {
        this.reconnectFrequency = reconnectFrequency;
    }

    /**
     * @see #setHttpHeaders(Map)
     */
    public Map<String, String> getHttpHeaders() {
        return httpHeaders;
    }

    /**
     * 设置 WebSocket 连接的请求头信息
     */
    public void setHttpHeaders(Map<String, String> httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

}
