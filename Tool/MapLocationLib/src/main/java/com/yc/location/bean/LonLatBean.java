package com.yc.location.bean;

public class LonLatBean {

    public double lon;
    public double lat;
    public String source;


    public LonLatBean(double lon, double lat, String source) {
        this.lon = lon;
        this.lat = lat;
        this.source = source;
    }
}
