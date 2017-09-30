package com.ns.yc.lifehelper.ui.other.myNote.bean;

/**
 * Created by PC on 2017/9/12.
 * 作者：PC
 */

public class NoteDetail {

    private String time;                //时间
    private String content;             //内容
    private int id;                     //id值，区分

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
