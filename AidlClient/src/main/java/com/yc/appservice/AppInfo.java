package com.yc.appservice;


import android.os.Parcel;
import android.os.Parcelable;

public class AppInfo implements Parcelable {

    private String key;
    private String value;

    public AppInfo(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.value);
    }

    public AppInfo() {
    }

    protected AppInfo(Parcel in) {
        this.key = in.readString();
        this.value = in.readString();
    }

    public static final Creator<AppInfo> CREATOR = new Creator<AppInfo>() {
        @Override
        public AppInfo createFromParcel(Parcel source) {
            return new AppInfo(source);
        }

        @Override
        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };

    @Override
    public String toString() {
        return "AppInfo{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

