package com.yc.socket.socketio;

/**
 * 需要发送的消息
 */
public class MessageEntity {
    private String methodName;
    private Object[] data;
    private MessageAck ack;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getData() {
        return data;
    }

    public void setData(Object[] data) {
        this.data = data;
    }

    public MessageAck getAck() {
        return ack;
    }

    public void setAck(MessageAck ack) {
        this.ack = ack;
    }

    public MessageEntity(String methodName) {
        this.methodName = methodName;
    }

    public MessageEntity(String methodName, Object... data) {
        this.methodName = methodName;
        this.data = data;
    }

    public MessageEntity(String methodName, Object[] data, MessageAck ack) {
        this.methodName = methodName;
        this.data = data;
        this.ack = ack;
    }
}
