package com.yc.location.mode.gps;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;

import com.yc.location.easymock.LocationToolUtils;

/**
 * <pre>
 *     @author  yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/07/22
 *     desc   : gps定位监听listener
 *     revise :
 * </pre>
 */
public class GpsLocationListener implements LocationListener {

    /**
     * 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
     * @param location                      定位点
     */
    @Override
    public void onLocationChanged(Location location) {

    }

    /**
     * Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
     * @param provider                      provider
     * @param status                        status状态
     * @param extras                        extras
     */
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

    /**
     * Provider被enable时触发此函数，比如GPS被打开
     * @param provider                      provider
     */
    @Override
    public void onProviderEnabled(String provider) {

    }

    /**
     * Provider被disable时触发此函数，比如GPS被关闭
     * @param provider                      provider
     */
    @Override
    public void onProviderDisabled(String provider) {

    }
}
