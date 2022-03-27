package com.yc.library.bean;

import java.io.Serializable;

public class ListNewsData implements Serializable {

    private String title;
    private String url;
    private int image;

    public ListNewsData(String title, String url, int image) {
        this.title = title;
        this.url = url;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
