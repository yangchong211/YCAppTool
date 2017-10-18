package com.ns.yc.lifehelper.utils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by PC on 2017/9/27.
 * 作者：PC
 */

public class EventBusUtils {

    public EventBusUtils() {}

    /**取消订阅*/
    public static void unregister(Object subscriber){
        EventBus.getDefault().unregister(subscriber);     //取消注册
    }

    /**订阅事件*/
    public static void register(Object subscriber){
        EventBus.getDefault().register(subscriber);       //开始订阅
    }

    public static void post(Object event){
        EventBus.getDefault().post(event);
    }

}
