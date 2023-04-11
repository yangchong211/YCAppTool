package com.yc.socket;


/**
 * 发送消息的拦截器
 */
public interface SendMessageIntercept {
    MessageEntity intercept(MessageEntity messageEntity);
}
