package com.yc.logclient.bean;


import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.yc.logclient.constant.LogConstant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


/**
 * Log实体类，用于对日志进行管理,跨进程传递。
 */
public class AppLogBean implements Parcelable {
    /**
     * 日志类型,见LogType枚举,分为以下几种:
     * app(10),//普通日志
     * crash(20),//crash日志
     * statistics(30),//统计日志
     * anr(40);//ANR 日志
     */
    private int type;
    //日志等级
    private int leve;
    //package ??
    private String key;
    //tag 标签
    private String tag;
    //日志内容
    private String msg;
    //throwable对象
    private ThrowableBean throwableBean;


    //格式化日志
    //09-20 17:21:06.246 I
    public static SimpleDateFormat S_D_FORMAT = new SimpleDateFormat("MM-dd HH:mm:ss.SSS ");

    static {
        S_D_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    }

    /**
     * 格式化日志输出:日期+日志级别+日志信息
     * 如下: 03-04 20:32:32.857 D [(AppContext.java:63)#OnCreate]
     *
     * @param msg
     * @param leve
     * @return
     */
    private static String getFormatMsg(String msg, int leve) {
        //Log.i("zcb","[getFormatMsg]ID:"+S_D_FORMAT.getTimeZone().getID());
        return S_D_FORMAT.format(new Date()) + getLevel(leve) + msg;
    }

    /**
     * 日志level 转换成字符串
     *
     * @param leve
     * @return
     */
    private static String getLevel(int leve) {
        String str = "";
        switch (leve) {
            case LogConstant.Log_Level_verbose:
                str = "V ";
                break;
            case LogConstant.Log_Level_debug:
                str = "D ";
                break;
            case LogConstant.Log_Level_info:
                str = "I ";
                break;
            case LogConstant.Log_Level_warn:
                str = "W ";
                break;
            case LogConstant.Log_Level_error:
                str = "E ";
                break;
            default:
                break;
        }
        return str;
    }


    public AppLogBean(int type, int lv, String key, String tag, String m) {
        this.type = type;
        this.leve = lv;
        this.key = key;
        this.tag = tag;
        this.msg = getFormatMsg(m, lv);
    }

    public AppLogBean(int type, int lv, String key, String tag, String m, ThrowableBean bean) {
        this.type = type;
        this.leve = lv;
        this.key = key;
        this.tag = tag;
        this.msg = getFormatMsg(m, lv);
        this.throwableBean = bean;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLeve() {
        return leve;
    }

    public void setLeve(int leve) {
        this.leve = leve;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public ThrowableBean getThrowableBean() {
        return throwableBean;
    }

    public void setThrowableBean(ThrowableBean throwableBean) {
        this.throwableBean = throwableBean;
    }


    // =============序列化 与反序列化
    protected AppLogBean(Parcel in) {
        type = in.readInt();
        leve = in.readInt();
        key = in.readString();
        tag = in.readString();
        msg = in.readString();
        String throwableValue = in.readString();
        if (!TextUtils.isEmpty(throwableValue)) {
            throwableBean = new Gson().fromJson(throwableValue, ThrowableBean.class);
        }
    }

    public static final Creator<AppLogBean> CREATOR = new Creator<AppLogBean>() {
        @Override
        public AppLogBean createFromParcel(Parcel source) {
            return new AppLogBean(source);
        }

        @Override
        public AppLogBean[] newArray(int size) {
            return new AppLogBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeInt(leve);
        dest.writeString(key);
        dest.writeString(tag);
        dest.writeString(msg);
        String throwableValue = "";
        if (throwableBean != null) {
            throwableValue = new Gson().toJson(throwableBean);
        }
        dest.writeString(throwableValue);
    }
}
