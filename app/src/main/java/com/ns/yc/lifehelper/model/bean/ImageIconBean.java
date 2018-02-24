package com.ns.yc.lifehelper.model.bean;

import java.io.Serializable;

public class ImageIconBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private int url;
    private int id;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ImageIconBean(String name, int url , int id) {
        super();
        this.name = name;
        this.url = url;
        this.id = id;
    }

}
