package com.ycbjie.library.db.cache;


import java.io.Serializable;

import io.realm.RealmObject;

public class CacheTaskDetailEntity extends RealmObject implements Serializable {

    private static final long serialVersionUID = 1L;
    private int dayOfWeek;
    private String title;
    private String content;
    private int icon;
    private long timeStamp;
    private int state;
    private int priority;

    public CacheTaskDetailEntity() {
    }

    public CacheTaskDetailEntity(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

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

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof CacheTaskDetailEntity)) return false;
        CacheTaskDetailEntity o = (CacheTaskDetailEntity) obj;


        if ((o.title == title || o.title.equals(title))
                && (o.content == content || o.content.equals(content))
                && o.state == state
                && (o.icon == icon)
//                && o.timeStamp == timeStamp
                && o.dayOfWeek == dayOfWeek
                && o.priority == priority)
            return true;
        return false;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {

        CacheTaskDetailEntity entity = (CacheTaskDetailEntity) super.clone();
        entity.dayOfWeek = dayOfWeek;
        entity.title = title;
        entity.content = content;
        entity.icon = icon;
        entity.timeStamp = timeStamp;
        entity.state = state;
        entity.priority = priority;
        return entity;
    }

    public CacheTaskDetailEntity cloneObj() {
        CacheTaskDetailEntity entity = new CacheTaskDetailEntity(dayOfWeek);
        entity.title = title;
        entity.content = content;
        entity.icon = icon;
        entity.timeStamp = timeStamp;
        entity.state = state;
        entity.priority = priority;
        return entity;
    }


    public void setTaskDetailEntity(CacheTaskDetailEntity e) {
        this.title = e.title;
        this.content = e.content;
        this.icon = e.icon;
        this.timeStamp = e.timeStamp;
        this.state = e.state;
        this.priority = e.priority;
        this.dayOfWeek = e.dayOfWeek;
    }


    @Override
    public String toString() {
        return "TaskDetailEntity{" +
                "dayOfWeek=" + dayOfWeek +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", icon='" + icon + '\'' +
                ", timeStamp=" + timeStamp +
                ", state=" + state +
                ", priority=" + priority +
                '}';
    }

}
