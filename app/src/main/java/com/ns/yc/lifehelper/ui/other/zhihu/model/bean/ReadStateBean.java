package com.ns.yc.lifehelper.ui.other.zhihu.model.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ReadStateBean extends RealmObject {

    @PrimaryKey
    private int id;

    public ReadStateBean() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
