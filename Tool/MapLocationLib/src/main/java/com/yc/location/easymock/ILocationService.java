package com.yc.location.easymock;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;


public interface ILocationService {

    void initLocationManager(Context context);

    /**
     * 获取一次定位回调，即回调只会得到一次
     *
     * @param LocationListener 定位回调接口
     */
    void requestOnceLocation(LocationListener LocationListener);

    void registerLocationListener(BaseLocationListener locationListener);

    void unRegisterLocationListener(BaseLocationListener locationListener);

    void startLocation(Context context);

    void stopLocation();

    /**
     * 返回最后一次定位结果，如果没有发起过定位，那么默认返回null
     */
    Location getLastKnownLocation();

    /**
     * 返回最后一次定位结果，如果没有发起过定位，那么默认返回0.f
     */
    double getLatitude();

    /**
     * 返回最后一次定位结果，如果没有发起过定位，那么默认返回0.f
     */
    double getLongitude();

    /**
     * 允许启用mock
     * 默认不允许
     */
    void enableMock(boolean enable);

    void setMockLatLng(Location mockLatLng);

    void setMockLatLng(double latitude, double longitude);

    /**
     * 是否允许位置mock
     */
    boolean isMockEnabled();

}
