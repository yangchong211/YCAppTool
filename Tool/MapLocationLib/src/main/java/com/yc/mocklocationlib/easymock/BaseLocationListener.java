package com.yc.mocklocationlib.easymock;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;

public class BaseLocationListener implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {

    }

    // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        String message = "";
        switch (status) {
            case LocationProvider.AVAILABLE:
                message = "当前GPS为可用状态!";
                break;
            case LocationProvider.OUT_OF_SERVICE:
                message = "当前GPS不在服务内!";
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                message = "当前GPS为暂停服务状态!";
                break;
        }
        LocationToolUtils.log("onStatusChanged status : " +message);
    }

    // Provider被enable时触发此函数，比如GPS被打开
    @Override
    public void onProviderEnabled(String provider) {

    }

    // Provider被disable时触发此函数，比如GPS被关闭
    @Override
    public void onProviderDisabled(String provider) {

    }
}
