package com.yc.location.dev;

import android.location.Location;

import com.yc.location.bean.DefaultLocation;
import com.yc.location.bean.ErrorInfo;
import com.yc.location.listener.LocationListener;
import com.yc.location.log.LogHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MonitorLocationListener implements LocationListener {

    private static final String TAG = "MonitorLocationListener";
    private boolean mockEnabled = false;
    private Location mockLatLng = null;
    private DefaultLocation mockDidiLocation;
    private final List<LocationListener> mCallback = new ArrayList<>();

    public DefaultLocation getMockDidiLocation() {
        return mockDidiLocation;
    }

    public boolean isMockEnabled() {
        return mockEnabled && mockLatLng != null;
    }

    public void setMockEnabled(boolean mockEnabled) {
        this.mockEnabled = mockEnabled;
    }

    public void setMockLatLng(Location mockLocation) {
        mockLatLng = mockLocation;
        updateMockLocation();
        if (isMockEnabled()) {
            onLocationChanged(null);
        }
    }

    public DefaultLocation updateMockLocation() {
        if (mockLatLng != null) {
            mockLatLng.setAccuracy(10);
            long localTime = System.currentTimeMillis();
            mockLatLng.setTime(localTime);
            mockDidiLocation = DefaultLocation.convert2DidiLocation(mockLatLng,
                    "测试", DefaultLocation.COORDINATE_TYPE_WGS84, localTime);
        }
        return mockDidiLocation;
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

    @Override
    public void onLocationChanged(DefaultLocation didiLocation) {
        if (isMockEnabled()) {
            didiLocation = updateMockLocation();
        }
        dump();
        for (int i = 0; i < mCallback.size(); i++) {
            if (mCallback.get(i) == null) {
                continue;
            }
            mCallback.get(i).onLocationChanged(didiLocation);
        }
        if (didiLocation != null) {
            LogHelper.d(TAG,"location" + didiLocation.toString());
        }
    }

    @Override
    public void onLocationError(int errorCode, ErrorInfo errInfo) {
        if (isMockEnabled()) {
            onLocationChanged(null);
            return;
        }
        for (int i = 0; i < mCallback.size(); i++) {
            if (mCallback.get(i) == null) {
                continue;
            }
            mCallback.get(i).onLocationError(errorCode, errInfo);
        }
    }

    @Override
    public void onStatusUpdate(String s, int status, String s1) {
        for (int i = 0; i < mCallback.size(); i++) {
            if (mCallback.get(i) == null) {
                continue;
            }
            mCallback.get(i).onStatusUpdate(s, status, s1);
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

    private void dump() {
        synchronized (mCallback) {
            LogHelper.d(TAG,"dump DIDILocationListener is " + Arrays.toString(mCallback.toArray()));
        }
    }

}
