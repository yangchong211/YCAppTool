package com.ns.yc.lifehelper.ui.other.weather.bean;


public class WeatherDetail {

    private int iconResourceId;
    private String key;
    private String value;

    public WeatherDetail(int iconResourceId, String key, String value) {
        this.iconResourceId = iconResourceId;
        this.key = key;
        this.value = value;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public void setIconResourceId(int iconResourceId) {
        this.iconResourceId = iconResourceId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
