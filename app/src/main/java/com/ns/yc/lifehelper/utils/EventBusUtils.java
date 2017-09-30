package com.ns.yc.lifehelper.utils;

import android.app.Activity;

import com.ns.yc.lifehelper.ui.other.bookReader.bean.support.RefreshCollectionIconEvent;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.support.RefreshCollectionListEvent;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.support.SubEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by PC on 2017/9/27.
 * 作者：PC
 */

public class EventBusUtils {

    public EventBusUtils() {}

    /**取消订阅*/
    public static void unregister(Activity activity){
        EventBus.getDefault().unregister(activity);     //取消注册
    }

    /**订阅事件*/
    public static void register(Activity activity){
        EventBus.getDefault().register(activity);       //开始订阅
    }

    public static void refreshCollectionList() {
        EventBus.getDefault().post(new RefreshCollectionListEvent());
    }

    public static void refreshCollectionIcon() {
        EventBus.getDefault().post(new RefreshCollectionIconEvent());
    }

    public static void refreshSubCategory(String minor, String type) {
        EventBus.getDefault().post(new SubEvent(minor, type));
    }

}
