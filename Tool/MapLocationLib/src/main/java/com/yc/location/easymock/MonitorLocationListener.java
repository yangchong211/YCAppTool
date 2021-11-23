package com.yc.location.easymock;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MonitorLocationListener extends BaseLocationListener {

    private boolean mockEnabled = false;
    private LocationModel mockMonitorLocation;
    private Location mLocation;
    private final List<LocationListener> mCallback = new ArrayList<>();

    @Override
    public void onLocationChanged(Location location) {
        if (isMockEnabled()) {
            LocationModel locationModel = updateMockLocation();
            location = locationModel.getLocation();
        }
        for (int i = 0; i < mCallback.size(); i++) {
            if (mCallback.get(i) == null) {
                continue;
            }
            mCallback.get(i).onLocationChanged(location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        for (int i = 0; i < mCallback.size(); i++) {
            if (mCallback.get(i) == null) {
                continue;
            }
            mCallback.get(i).onStatusChanged(provider, status, extras);
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        for (int i = 0; i < mCallback.size(); i++) {
            if (mCallback.get(i) == null) {
                continue;
            }
            mCallback.get(i).onProviderEnabled(provider);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        for (int i = 0; i < mCallback.size(); i++) {
            if (mCallback.get(i) == null) {
                continue;
            }
            mCallback.get(i).onProviderDisabled(provider);
        }
    }

    public boolean isMockEnabled() {
        return mockEnabled && mLocation != null;
    }

    public void setMockEnabled(boolean mockEnabled) {
        this.mockEnabled = mockEnabled;
    }

    public void setMockLatLng(Location location) {
        mLocation = location;
        updateMockLocation();
        if (isMockEnabled()) {
            onLocationChanged(null);
        }
    }

    public LocationModel getMockLocation() {
        return mockMonitorLocation;
    }

    public LocationModel updateMockLocation() {
        if (mLocation != null) {
            long localTime = System.currentTimeMillis();
            mockMonitorLocation = new LocationModel(mLocation,localTime);
        }
        return mockMonitorLocation;
    }

    public int add(LocationListener listener) {
        if (listener == null) {
            return -1;
        }
        synchronized (mCallback) {
            if (mCallback.contains(listener)) {
                return 0;
            }
            return mCallback.add(listener) ? 0 : -1;
        }
    }

    public int remove(LocationListener listener) {
        if (listener == null) {
            return -1;
        }
        synchronized (mCallback) {
            return mCallback.remove(listener) ? 0 : -1;
        }
    }
}
