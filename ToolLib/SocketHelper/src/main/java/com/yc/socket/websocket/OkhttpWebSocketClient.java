package com.yc.socket.websocket;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class OkhttpWebSocketClient implements IWebSocketClient {

    private WebSocket webSocket;
    private WebSocketSetting mWebSocketSetting;
    // 连接状态
    private int connectState = 0;
    /**
     * 是否开启自动重连
     * 非用户主动断开的需要开启，
     */
    boolean needRetryConnect = false;

    @Override
    public void connect(WebSocketSetting webSocketSetting) {
        this.mWebSocketSetting = webSocketSetting;
        disConnect();
        OkHttpClient.Builder build = new OkHttpClient.Builder().pingInterval(webSocketSetting.getConnectionLostTimeout(), TimeUnit.SECONDS);
        Request.Builder requestBuilder = new Request.Builder();
        Map<String, String> headers = webSocketSetting.getHttpHeaders();
        if (headers != null && headers.size() != 0) {
            for (String key : headers.keySet()) {
                requestBuilder.addHeader(key, headers.get(key));
            }
        }
        requestBuilder.url(webSocketSetting.getConnectUrl());
        connectState = CONNECTING;
        needRetryConnect = true;
        this.webSocket = build.build().newWebSocket(requestBuilder.build(), new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                connectState = CONNECTED;
                SocketCallBackManager.getInstance().notifyConnected();
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                SocketCallBackManager.getInstance().notifyReciveString(text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
                SocketCallBackManager.getInstance().notifyReciveBytes(bytes.asByteBuffer());
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                connectState = UNCONNECTED;
                SocketCallBackManager.getInstance().notifyDisConnected();
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                connectState = UNCONNECTED;
                SocketCallBackManager.getInstance().notifyConnectFailed(t);
            }
        });
    }

    @Override
    public void reconnect() {
        if (webSocket != null && connectState != CONNECTING && connectState != CONNECTED) {
            connect(this.mWebSocketSetting);
        }
    }

    @Override
    public boolean send(String text) {
        return webSocket.send(text);
    }

    @Override
    public boolean send(byte[] bytes) {
        return webSocket.send(ByteString.of(bytes));
    }

    @Override
    public boolean disConnect() {
        needRetryConnect = false;
        if (webSocket != null) {
            // 此处不能用cancel方法，因为cacel方法会引起failed回调误触发重连机制导致手动断开后自动重连
            webSocket.close(1000, "用户关闭socket");
        }
        return true;
    }

    @Override
    public int getConnectState() {
        return connectState;
    }

    @Override
    public boolean needAutoRetryConnect() {
        return needRetryConnect && connectState != CONNECTING;
    }

}
