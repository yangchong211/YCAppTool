package com.yc.socket.websocket;

import java.nio.ByteBuffer;

/**
 * WebSocket 监听器,
 * for user
 * Created by yangchong on 2018/6/8.
 */
public interface ISocketListener {

    /**
     * 连接成功
     */
    void onConnected();

    /**
     * 连接失败
     */
    void onConnectFailed(Throwable e);

    /**
     * 连接断开
     */
    void onDisconnect();


    /**
     * 接收到文本消息
     *
     * @param message 文本消息
     */
    <T> void onMessage(String message);

    /**
     * 接收到二进制消息
     *
     * @param bytes 二进制消息
     */
    <T> void onMessage(ByteBuffer bytes);

}
