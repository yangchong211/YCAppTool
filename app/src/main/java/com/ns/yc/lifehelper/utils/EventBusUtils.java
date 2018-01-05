package com.ns.yc.lifehelper.utils;

import org.greenrobot.eventbus.EventBus;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/4/31
 * 描    述：EventBus事件总线工具类
 * 修订历史：
 * ================================================
 */
public class EventBusUtils {

    public EventBusUtils() {}

    /**取消订阅*/
    public static void unregister(Object subscriber){
        EventBus.getDefault().unregister(subscriber);
    }

    /**订阅事件*/
    public static void register(Object subscriber){
        EventBus.getDefault().register(subscriber);
    }

    /**发送事件*/
    public static void post(Object event){
        EventBus.getDefault().post(event);
    }

}
