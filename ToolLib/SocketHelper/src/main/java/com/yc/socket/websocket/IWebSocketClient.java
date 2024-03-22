package com.yc.socket.websocket;

/**
 * websocket客户端抽象层
 */
public interface IWebSocketClient {

    /**
     * 0-未连接
     * 1-正在连接
     * 2-已连接
     */
    int UNCONNECTED = 0;
    int CONNECTING = 1;
    int CONNECTED = 2;


    void connect(WebSocketSetting webSocketSetting);

    void reconnect();

    boolean send(String text);

    boolean send(byte[] bytes);

    boolean disConnect();

    int getConnectState();

    boolean needAutoRetryConnect();
}

