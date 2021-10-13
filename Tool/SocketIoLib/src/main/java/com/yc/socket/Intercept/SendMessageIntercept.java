package com.yc.socket.Intercept;

import com.yc.socket.socketio.Message.MessageEntity;

/**
 * 发送消息的拦截器
 */
public interface SendMessageIntercept {
    MessageEntity intercept(MessageEntity messageEntity);
}
