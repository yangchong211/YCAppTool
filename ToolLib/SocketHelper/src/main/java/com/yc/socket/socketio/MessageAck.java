package com.yc.socket.socketio;

/**
 * 发送消息的回执
 */
public abstract class MessageAck {
    public abstract void call(Object... args);

    public boolean needCallBackOnUiThread() {
        return true;
    }
}
