package com.ns.yc.lifehelper.ui.other.myNews.wyNews.cache;

import io.realm.RealmObject;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/31
 * 描    述：头条新闻类型缓存
 * 修订历史：
 * ================================================
 */
public class CacheTodayChannel extends RealmObject {

    private String channel;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
