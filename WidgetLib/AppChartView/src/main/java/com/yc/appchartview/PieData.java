package com.yc.appchartview;


public class PieData {

    private String mColor;
    private String title;
    private float percentage;


    public PieData(String title, float percentage, String color) {
        this.mColor = color;
        this.title = title;
        this.percentage = percentage;
    }

    public String getGetColor() {
        return mColor;
    }

    public void setGetColor(String getColor) {
        this.mColor = getColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }
}