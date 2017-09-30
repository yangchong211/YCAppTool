package com.ns.yc.lifehelper.ui.other.weather.bean;

/**
 * Created by PC on 2017/9/14.
 * 作者：PC
 */

public class WeatherAirLive {


    /**
     * citycode : 101010100
     * PM25 : 132
     * time : 2017091410
     * rdesc : Success
     * PM10 : 136
     * SO2 : 3.38
     * o3 : 28.15
     * NO2 : 66.85
     * primary : 细颗粒物(PM2.5)
     * rcode : 200
     * CO : 1.37
     * AQI : 174
     */

    private String citycode;
    private String PM25;
    private String time;
    private String rdesc;
    private String PM10;
    private String SO2;
    private String o3;
    private String NO2;
    private String primary;
    private int rcode;
    private String CO;
    private String AQI;

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getPM25() {
        return PM25;
    }

    public void setPM25(String PM25) {
        this.PM25 = PM25;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRdesc() {
        return rdesc;
    }

    public void setRdesc(String rdesc) {
        this.rdesc = rdesc;
    }

    public String getPM10() {
        return PM10;
    }

    public void setPM10(String PM10) {
        this.PM10 = PM10;
    }

    public String getSO2() {
        return SO2;
    }

    public void setSO2(String SO2) {
        this.SO2 = SO2;
    }

    public String getO3() {
        return o3;
    }

    public void setO3(String o3) {
        this.o3 = o3;
    }

    public String getNO2() {
        return NO2;
    }

    public void setNO2(String NO2) {
        this.NO2 = NO2;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public int getRcode() {
        return rcode;
    }

    public void setRcode(int rcode) {
        this.rcode = rcode;
    }

    public String getCO() {
        return CO;
    }

    public void setCO(String CO) {
        this.CO = CO;
    }

    public String getAQI() {
        return AQI;
    }

    public void setAQI(String AQI) {
        this.AQI = AQI;
    }
}
