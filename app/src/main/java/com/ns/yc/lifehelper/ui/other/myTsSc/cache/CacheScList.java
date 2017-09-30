package com.ns.yc.lifehelper.ui.other.myTsSc.cache;

import io.realm.RealmObject;

/**
 * Created by PC on 2017/9/27.
 * 作者：PC
 */

public class CacheScList extends RealmObject {

    private String detailid;
    private String name;
    private String author;

    public String getDetailid() {
        return detailid;
    }

    public void setDetailid(String detailid) {
        this.detailid = detailid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
