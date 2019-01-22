package com.ycbjie.music.model.bean;

import com.google.gson.annotations.SerializedName;


public class ArtistInfo {
    // 星座
    @SerializedName("constellation")
    private String constellation;
    // 体重
    @SerializedName("weight")
    private float weight;
    // 身高
    @SerializedName("stature")
    private float stature;
    // 国籍
    @SerializedName("country")
    private String country;
    // 歌手链接
    @SerializedName("url")
    private String url;
    // 歌手简介
    @SerializedName("intro")
    private String intro;
    // 头像
    @SerializedName("avatar_s1000")
    private String avatar_s1000;
    // 姓名
    @SerializedName("name")
    private String name;
    // 生日
    @SerializedName("birth")
    private String birth;

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getStature() {
        return stature;
    }

    public void setStature(float stature) {
        this.stature = stature;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getAvatar_s1000() {
        return avatar_s1000;
    }

    public void setAvatar_s1000(String avatar_s1000) {
        this.avatar_s1000 = avatar_s1000;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }
}
