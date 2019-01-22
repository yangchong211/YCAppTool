package com.ycbjie.gank.bean.cache;

import io.realm.RealmObject;


public class CacheSearchHistory extends RealmObject {

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
