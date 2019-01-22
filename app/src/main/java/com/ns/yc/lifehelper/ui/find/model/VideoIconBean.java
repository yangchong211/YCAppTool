package com.ns.yc.lifehelper.ui.find.model;

import java.io.Serializable;


public class VideoIconBean implements Serializable {

    private String name;
    private int resId;
    private int id;

    public VideoIconBean(String name, int resId , int id) {
        //super();
        this.name = name;
        this.resId = resId;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
