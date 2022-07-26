package com.yc.appchartview;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     GitHub : https://github.com/yangchong211/YCWidgetLib
 *     time  : 2018/11/9
 *     desc  : 圆形图表控件data体
 *     revise:
 * </pre>
 */
public final class PieFlagData {

    private String mColor;
    private String title;
    private float percentage;


    public PieFlagData(String title, float percentage, String color) {
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