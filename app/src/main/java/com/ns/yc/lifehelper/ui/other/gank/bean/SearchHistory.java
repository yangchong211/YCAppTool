package com.ns.yc.lifehelper.ui.other.gank.bean;

import io.realm.RealmObject;


public class SearchHistory extends RealmObject {

    private long createTimeMill;
    private String content;

    public long getCreateTimeMill() {
        return createTimeMill;
    }

    public void setCreateTimeMill(long createTimeMill) {
        this.createTimeMill = createTimeMill;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
