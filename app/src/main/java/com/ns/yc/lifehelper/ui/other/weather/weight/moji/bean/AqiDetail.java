package com.ns.yc.lifehelper.ui.other.weather.weight.moji.bean;


public class AqiDetail {

    private double co;

    private int so2;

    private String area;

    private int o3;

    private int no2;

    private String area_code;

    private String quality;

    private int aqi;

    private int pm10;

    private int pm2_5;

    private int o3_8h;

    private String primary_pollutant;

    public void setCo(double co){
        this.co = co;
    }
    public double getCo(){
        return this.co;
    }
    public void setSo2(int so2){
        this.so2 = so2;
    }
    public int getSo2(){
        return this.so2;
    }
    public void setArea(String area){
        this.area = area;
    }
    public String getArea(){
        return this.area;
    }
    public void setO3(int o3){
        this.o3 = o3;
    }
    public int getO3(){
        return this.o3;
    }
    public void setNo2(int no2){
        this.no2 = no2;
    }
    public int getNo2(){
        return this.no2;
    }
    public void setArea_code(String area_code){
        this.area_code = area_code;
    }
    public String getArea_code(){
        return this.area_code;
    }
    public void setQuality(String quality){
        this.quality = quality;
    }
    public String getQuality(){
        return this.quality;
    }
    public void setAqi(int aqi){
        this.aqi = aqi;
    }
    public int getAqi(){
        return this.aqi;
    }
    public void setPm10(int pm10){
        this.pm10 = pm10;
    }
    public int getPm10(){
        return this.pm10;
    }
    public void setPm2_5(int pm2_5){
        this.pm2_5 = pm2_5;
    }
    public int getPm2_5(){
        return this.pm2_5;
    }
    public void setO3_8h(int o3_8h){
        this.o3_8h = o3_8h;
    }
    public int getO3_8h(){
        return this.o3_8h;
    }
    public void setPrimary_pollutant(String primary_pollutant){
        this.primary_pollutant = primary_pollutant;
    }
    public String getPrimary_pollutant(){
        return this.primary_pollutant;
    }

}
