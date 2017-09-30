package com.ns.yc.lifehelper.ui.other.myPicture.bean;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by PC on 2017/9/4.
 * 作者：PC
 */

public class PicBeautifulContentBean extends RealmObject implements Serializable {

    private int imagewidth;
    private int imageheight;
    private String url;
    private int order;
    private String groupid;
    private String title;


    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }


    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }


    public int getImageheight() {
        return imageheight;
    }

    public void setImageheight(int imageheight) {
        this.imageheight = imageheight;
    }

    public int getImagewidth() {
        return imagewidth;
    }

    public void setImagewidth(int imagewidth) {
        this.imagewidth = imagewidth;
    }


    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



}
