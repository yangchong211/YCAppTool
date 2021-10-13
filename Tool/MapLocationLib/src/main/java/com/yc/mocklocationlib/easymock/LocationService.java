package com.yc.mocklocationlib.easymock;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;

public final class LocationService implements ILocationService {

    private final ILocationService mDelegate = new LocationServiceImpl();

    private LocationService() {

    }

    public static LocationService getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public final void initLocationManager(final Context arg0) {
        if (null != this.mDelegate) {
            this.mDelegate.initLocationManager(arg0);
        }
    }

    @Override
    public final void requestOnceLocation(final LocationListener arg0) {
        if (null != this.mDelegate) {
            this.mDelegate.requestOnceLocation(arg0);
        }
    }

    @Override
    public final void registerLocationListener(final BaseLocationListener arg0) {
        if (null != this.mDelegate) {
            this.mDelegate.registerLocationListener(arg0);
        }
    }

    @Override
    public final void unRegisterLocationListener(final BaseLocationListener arg0) {
        if (null != this.mDelegate) {
            this.mDelegate.unRegisterLocationListener(arg0);
        }
    }

    @Override
    public final void startLocation(final Context arg0) {
        if (null != this.mDelegate) {
            this.mDelegate.startLocation(arg0);
        }
    }

    @Override
    public final Location getLastKnownLocation() {
        return null != this.mDelegate ? this.mDelegate.getLastKnownLocation() : null;
    }

    @Override
    public final double getLatitude() {
        return null != this.mDelegate ? this.mDelegate.getLatitude() : 0;
    }

    @Override
    public final double getLongitude() {
        return null != this.mDelegate ? this.mDelegate.getLongitude() : 0;
    }

    @Override
    public final void stopLocation() {
        if (null != this.mDelegate) {
            this.mDelegate.stopLocation();
        }
    }

    @Override
    public final void enableMock(final boolean arg0) {
        if (null != this.mDelegate) {
            this.mDelegate.enableMock(arg0);
        }
    }

    @Override
    public final void setMockLatLng(final Location arg0) {
        if (null != this.mDelegate) {
            this.mDelegate.setMockLatLng(arg0);
        }
    }

    @Override
    public final void setMockLatLng(final double arg0, final double arg1) {
        if (null != this.mDelegate) {
            this.mDelegate.setMockLatLng(arg0, arg1);
        }
    }

    @Override
    public final boolean isMockEnabled() {
        return null != this.mDelegate && this.mDelegate.isMockEnabled();
    }

    private static final class Singleton {
        static final LocationService INSTANCE = new LocationService();
    }
}
