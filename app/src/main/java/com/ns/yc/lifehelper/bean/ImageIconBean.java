package com.ns.yc.lifehelper.bean;

import java.io.Serializable;

public class ImageIconBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private int url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUrl() {
        return url;
    }

    public void setUrl(int url) {
        this.url = url;
    }

    public ImageIconBean(String name, int url) {
        super();
        this.name = name;
        this.url = url;
    }

}
