package com.yc.looperthread;

public interface IDoAction {

    /**
     * 启动线程
     */
    void startThread();

    /**
     * 开始循环
     */
    void beginLoop();

    /**
     * 暂停循环
     */
    void endLoop();

    /**
     * 释放操作
     */
    void release();

    /**
     * 循环体做的事情，子类必须实现
     */
    void doAction();

}
