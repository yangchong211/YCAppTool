package com.yc.mocklocationlib.gpsmock.utils;

import android.content.Context;

import com.yc.mocklocationlib.gpsmock.bean.LatLng;

public class GpsMockConfig {
    public GpsMockConfig() {
    }

    public static boolean isGPSMockOpen(Context context) {
        return SharedPrefsUtil.getBoolean(context, "gps_mock_open", false);
    }

    public static void setGPSMockOpen(Context context, boolean open) {
        SharedPrefsUtil.putBoolean(context, "gps_mock_open", open);
    }

    public static LatLng getMockLocation(Context context) {
        return (LatLng) CacheUtils.readObject(context, "mock_location");
    }

    public static void saveMockLocation(Context context, LatLng latLng) {
        CacheUtils.saveObject(context, "mock_location", latLng);
    }
}
