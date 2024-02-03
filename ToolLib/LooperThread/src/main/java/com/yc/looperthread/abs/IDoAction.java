package com.yc.looperthread.abs;

/**
 * 轮询方案统一接口
 * 第一种方案：使用Thread.sleep实现轮询。一直做while循环
 * 第二种方案：使用ScheduledExecutorService实现轮询
 * 第三种方案：使用Timer实现定时性周期性任务轮训
 * 第四种方案：使用Handler不断发送消息来实现轮训
 */
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
