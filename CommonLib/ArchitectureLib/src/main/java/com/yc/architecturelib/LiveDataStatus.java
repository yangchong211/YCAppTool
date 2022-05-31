package com.yc.architecturelib;

public enum LiveDataStatus {

    /**
     * 数据状态发生改变之前
     * 当进行数据处理操作(请求网络，查询数据库...）操作发生之前
     */
    START,

    /**
     * 数据状态改变成功
     * 当数据成功获取并赋值成功
     * 调用[androidx.lifecycle.MutableLiveData.setValue]时的状态
     */
    SUCCESS,

    /**
     * 数据状态改变失败
     * 当数据处理操作(请求网络，查询数据库...）执行失败时，
     * 不可调用 [androidx.lifecycle.MutableLiveData.setValue]
     */
    ERROR,

    /**
     * 完成状态 仅当数据改变成功后由SUCCESS状态改变成COMPLETE
     */
    COMPLETE,

    /**
     * 数据处于初始化状态，数据需要重新进行改变，用于reload数据
     */
    RESET;

    private String message;

    public LiveDataStatus setMessage(String msg) {
        this.message = msg;
        return this;
    }

    public String getMessage() {
        return message;
    }
}
