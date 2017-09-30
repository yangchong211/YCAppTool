package com.ns.yc.lifehelper.ui.other.myTsSc.cache;

import io.realm.RealmObject;

/**
 * Created by PC on 2017/8/28.
 * 作者：PC
 */

public class TsSearchHis extends RealmObject {

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
