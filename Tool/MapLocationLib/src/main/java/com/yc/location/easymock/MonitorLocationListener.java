package com.yc.location.easymock;

import android.location.Location;
import com.yc.location.bean.DefaultLocation;
import com.yc.location.listener.AbsBaseLocationListener;
import com.yc.location.listener.LocationListener;

import java.util.ArrayList;
import java.util.List;

public class MonitorLocationListener extends AbsBaseLocationListener {

    private boolean mockEnabled = false;
    private Location mockLatLng = null;
    private DefaultLocation mockDidiLocation;
    private final List<LocationListener> mCallback = new ArrayList<>();

    @Override
    public void onLocationChanged(DefaultLocation location) {
        super.onLocationChanged(location);
        if (isMockEnabled()) {
            location = this.updateMockLocation();
        }
        for (int i = 0; i < mCallback.size(); i++) {
            if (mCallback.get(i) == null) {
                continue;
            }
            mCallback.get(i).onLocationChanged(location);
        }
    }

    @Override
    public void onStatusUpdate(String name, int status, String desc) {
        super.onStatusUpdate(name, status, desc);
        for(int i = 0; i < this.mCallback.size(); ++i) {
            if (this.mCallback.get(i) != null) {
                if (mCallback.get(i) == null) {
                    continue;
                }
                ((LocationListener)this.mCallback.get(i)).onStatusUpdate(name, status, desc);
            }
        }
    }


    public boolean isMockEnabled() {
        return mockEnabled && mockLatLng != null;
    }

    public void setMockEnabled(boolean mockEnabled) {
        this.mockEnabled = mockEnabled;
    }

    public void setMockLatLng(Location location) {
        mockLatLng = location;
        updateMockLocation();
        if (isMockEnabled()) {
            onLocationChanged(null);
        }
    }

    public DefaultLocation getMockLocation() {
        return mockDidiLocation;
    }

    public DefaultLocation updateMockLocation() {
        if (this.mockLatLng != null) {
            this.mockLatLng.setAccuracy(10.0F);
            long localTime = System.currentTimeMillis();
            this.mockLatLng.setTime(localTime);
            this.mockDidiLocation = DefaultLocation.convert2DidiLocation(
                    this.mockLatLng, "测试", 0, localTime);
        }
        return this.mockDidiLocation;
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
