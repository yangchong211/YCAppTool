package com.yc.mocklocationlib.gpsmock.bean;
import java.io.Serializable;

public class LatLng implements Serializable {

    public double latitude;
    public double longitude;

    public LatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
