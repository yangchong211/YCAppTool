package com.yc.appstatuslib.info;


import android.content.res.Resources;
import android.os.Build;

import com.yc.appstatuslib.AppStatusManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class CollectionInfo {

    private static final SimpleDateFormat sDateFormat;
    private static final String pattern = "yyyy-MM-dd HH:mm:ss:SSS";
    public BatteryInfo batteryInfo = new BatteryInfo();
    public CpuInfo cpuInfo = new CpuInfo();
    public String currentTime;
    public int appStatus;

    static {
        Locale sysLocale = getSysLocale();
        sDateFormat = new SimpleDateFormat(pattern, sysLocale);
    }

    public CollectionInfo() {

    }

    public static CollectionInfo builder(BatteryInfo batteryInfo,
                                         int appStatus) {
        CollectionInfo collectionInfo = new CollectionInfo();
        collectionInfo.batteryInfo = batteryInfo;
        collectionInfo.cpuInfo = CpuInfo.builder();
        collectionInfo.appStatus = appStatus;
        collectionInfo.currentTime = sDateFormat.format(new Date());
        return collectionInfo;
    }

    @Override
    public String toString() {
        return "CollectionInfo{" +
                "batteryInfo=" + batteryInfo +
                ", cpuInfo=" + cpuInfo +
                ", currentTime='" + currentTime + '\'' +
                ", appStatus=" + appStatus +
                '}';
    }

    public static Locale getSysLocale() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= 24) {
            locale = Resources.getSystem().getConfiguration().getLocales().get(0);
        } else {
            locale = Resources.getSystem().getConfiguration().locale;
        }
        if (locale == null) {
            locale = new Locale("en-US");
        }
        return locale;
    }

}
