package com.yc.location.bean;


import com.yc.location.constant.Constants;

import java.io.Serializable;

public class LocCache implements Serializable {

    //double lon;
    //double lat;
    public LonLatBean lonlat;
    public int accuracy;
    public double confidence;
    public long timestamp;
    public int coordinateType;

    public int speed;
    public double transprob;

    public double altitude;
    public float bearing;
    public String provider;
    public float gps_speed;
    public long ret_time;

    public LocCache(double plon, double plat, int paccuracy, double pconfidence, int pspeed, long ptimestamp, String source, int coordinateType) {
        lonlat = new LonLatBean(plon, plat, source);
        //lon = plon;
        //lat = plat;
        accuracy = paccuracy;
        confidence = pconfidence;
        speed = pspeed;
        timestamp = ptimestamp;
        this.coordinateType = coordinateType;
    }

    public void setAltitude(double a) {
        altitude = a;
    }
    public void setBearing(float b) {
        bearing = b;
    }
    public void setProvider(String p) {
        provider = p;
    }
    public void setGps_speed(float s) {
        gps_speed = s;
    }
    public void setRet_time(long t) {
        ret_time = t;
    }
    public void setCoordinateType(int ct) { coordinateType = ct; }

    public String toJson() {
        return Constants.joLeft
                + "\"lon\"" + Constants.jsAssi + Constants.formatDouble(lonlat.lon, Constants.lonlatDots) + Constants.jsSepr
                + "\"lat\"" + Constants.jsAssi + Constants.formatDouble(lonlat.lat, Constants.lonlatDots) + Constants.jsSepr
                + "\"accuracy\"" + Constants.jsAssi + accuracy + Constants.jsSepr
                + "\"confidence\"" + Constants.jsAssi + Constants.formatDouble(confidence, Constants.confiprobDots) + Constants.jsSepr
                + "\"timestamp\"" + Constants.jsAssi + timestamp + Constants.jsSepr
                + "\"speed\"" + Constants.jsAssi + speed + Constants.jsSepr
                + "\"coordinateType\"" + Constants.jsAssi + coordinateType + Constants.jsSepr
                + "\"transprob\"" + Constants.jsAssi + Constants.formatDouble(transprob, Constants.confiprobDots)
                + Constants.joRight;
    }

}

