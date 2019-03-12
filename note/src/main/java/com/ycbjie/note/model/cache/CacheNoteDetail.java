package com.ycbjie.note.model.cache;

import io.realm.RealmObject;

/**
 * Created by PC on 2017/9/12.
 * 作者：PC
 */

public class CacheNoteDetail extends RealmObject {

    private String time;
    private String content;
    private int id;

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
