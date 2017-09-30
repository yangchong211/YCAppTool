package com.ns.yc.lifehelper.ui.other.myNews.wxNews.cache;

import io.realm.RealmObject;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/31
 * 描    述：微信新闻类型缓存
 * 修订历史：
 * ================================================
 */
public class CacheWxChannel extends RealmObject {


    /**
     * channelid : 15
     * channel : 职场
     */

    private String channelid;
    private String channel;

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
