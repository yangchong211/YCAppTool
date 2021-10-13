package com.ycbjie.music.model.bean;

import com.google.gson.annotations.SerializedName;

public class MusicLrc {

    @SerializedName("lrcContent")
    private String lrcContent;

    public String getLrcContent() {
        return lrcContent;
    }

    public void setLrcContent(String lrcContent) {
        this.lrcContent = lrcContent;
    }
}
