package com.ycbjie.library.db.cache;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by PC on 2017/9/15.
 * 作者：PC
 */

public class CacheToDoDetail extends RealmObject implements Serializable{

    private String title;               //标题
    private String content;             //内容
    private String time;                //时间
    private String dayOfWeek;           //周几？
    private int icon;                   //图片
    private String priority;            //重要级分类

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
