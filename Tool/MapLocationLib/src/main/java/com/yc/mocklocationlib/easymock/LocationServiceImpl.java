package com.yc.mocklocationlib.easymock;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.Nullable;


public class LocationServiceImpl implements ILocationService{

    private final MonitorLocationListener mDelegateListener = new MonitorLocationListener();
    private LocationManagerHelper mLocationManager;
    private static boolean sMockEnable = false;

    @Override
    public void initLocationManager(Context context) {
        mLocationManager = LocationManagerHelper.getInstance(context);
    }

    @Override
    public void requestOnceLocation(LocationListener LocationListener) {

    }

    @Override
    public void registerLocationListener(BaseLocationListener locationListener) {
        mDelegateListener.add(locationListener);
    }

    @Override
    public void unRegisterLocationListener(BaseLocationListener locationListener) {
        mDelegateListener.remove(locationListener);
    }

    @Override
    public void startLocation(Context context) {

    }

    @Override
    public void stopLocation() {

    }

    @Nullable
    @Override
    public Location getLastKnownLocation() {
        if (mDelegateListener.isMockEnabled()) {
            return mDelegateListener.getMockLocation().getLocation();
        }
        if (mLocationManager == null) {
            return null;
        }
        return mLocationManager.getLastKnownLocation();
    }

    @Override
    public double getLatitude() {
        if (getLastKnownLocation() != null) {
            return getLastKnownLocation().getLatitude();
        }
        return 0;
    }

    @Override
    public double getLongitude() {
        if (getLastKnownLocation() != null) {
            return getLastKnownLocation().getLongitude();
        }
        return 0;
    }

    @Override
    public void enableMock(boolean enable) {
        sMockEnable = enable;
        mDelegateListener.setMockEnabled(enable);
    }

    @Override
    public void setMockLatLng(Location mockLatLng) {
        mDelegateListener.setMockLatLng(mockLatLng);
    }

    @Override
    public void setMockLatLng(double latitude, double longitude) {
        Location location = new Location("dev mock");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        mDelegateListener.setMockLatLng(location);
    }

    @Override
    public boolean isMockEnabled() {
        return sMockEnable;
    }
}
