package com.ns.yc.lifehelper.ui.other.myVideo.bean;

import java.io.Serializable;


public class VideoIconBean implements Serializable {

    private String name;
    private int resId;

    public VideoIconBean(String name, int resId) {
        //super();
        this.name = name;
        this.resId = resId;
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
}
