package com.ns.yc.lifehelper.model.bean;

import org.json.JSONObject;


public class ItemEntity {

    private String country;
    private String temperature;
    private String coverImageUrl;
    private String address;
    private String description;
    private String time;
    private String mapImageUrl;

    public ItemEntity(){}

    public ItemEntity(JSONObject jsonObject) {
        this.country = jsonObject.optString("country");
        this.temperature = jsonObject.optString("temperature");
        this.coverImageUrl = jsonObject.optString("coverImageUrl");
        this.address = jsonObject.optString("address");
        this.description = jsonObject.optString("description");
        this.time = jsonObject.optString("time");
        this.mapImageUrl = jsonObject.optString("mapImageUrl");
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMapImageUrl() {
        return mapImageUrl;
    }

    public void setMapImageUrl(String mapImageUrl) {
        this.mapImageUrl = mapImageUrl;
    }

}
