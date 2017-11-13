package com.ns.yc.lifehelper.ui.other.weather.view.weather;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;

public abstract class BaseDrawer {


    public enum Type {CLEAR_D, RAIN_D, FOG_D, SNOW_D, CLOUDY_D,
        OVERCAST,CLEAR_N,SNOW_N,CLOUDY_N,OVERCAST_N,DEFAULT}


    public static BaseDrawer makeDrawerByType(Context context, Type type) {
        switch (type) {
            case CLEAR_D:
                return new SunnyDrawer(context);
            case RAIN_D:
                return new RainDayDrawer(context,false);
            case FOG_D:
                return new FogDayDrawer(context,false);
            case SNOW_D:
                return new SnowDayDrawer(context,false);
            case CLOUDY_D:
                return new CloudyDayDrawer(context,false);
            case OVERCAST:
                return new OvercastDayDrawer(context,false);
            case CLEAR_N:
                return new SunnyNightDrawer(context,true);
            case SNOW_N:
                return new SnowNightDrawer(context,true);
            case CLOUDY_N:
                return new CloudyNightDrawer(context,true);
            case OVERCAST_N:
                return new OvercastNightDrawer(context,true);
            case DEFAULT:
            default:
                return new SunnyDrawer(context);
        }
    }

    protected Context context;
    private final float desity;
    protected int width, height;
    private GradientDrawable skyDrawable;
    protected final boolean isNight;

    public BaseDrawer(Context context, boolean isNight) {
        super();
        this.context = context;
        this.desity = context.getResources().getDisplayMetrics().density;
        this.isNight = isNight;
    }


    public boolean draw(Canvas canvas, float alpha) {
        boolean needDrawNextFrame = drawWeather(canvas, alpha);
        return needDrawNextFrame;
    }

    public abstract boolean drawWeather(Canvas canvas, float alpha);// return

    public void setSize(int width, int height) {
        if(this.width != width && this.height != height){
            this.width = width;
            this.height = height;
            if (skyDrawable != null) {
                skyDrawable.setBounds(0, 0, width, height);
            }
        }
    }

    public static int convertAlphaColor(float percent,final int originalColor) {
        int newAlpha = (int) (percent * 255) & 0xFF;
        return (newAlpha << 24) | (originalColor & 0xFFFFFF);
    }

}
