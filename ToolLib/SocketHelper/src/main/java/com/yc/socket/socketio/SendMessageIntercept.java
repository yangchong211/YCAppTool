package com.yc.socket.socketio;


/**
 * 发送消息的拦截器
 */
public interface SendMessageIntercept {
    MessageEntity intercept(MessageEntity messageEntity);
}
