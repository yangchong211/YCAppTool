package com.yc.location.dev;

import android.content.Context;
import android.location.Location;

import com.yc.location.bean.DefaultLocation;
import com.yc.location.config.LocateMode;
import com.yc.location.config.LocationUpdateOption;
import com.yc.location.listener.AbsBaseLocationListener;
import com.yc.location.listener.LocationListener;
import java.io.File;

public interface ILocationProvider {

    void initLocationManager(Context var1);

    void requestOnceLocation(LocationListener var1);

    ILocationProvider registerLocationListener(AbsBaseLocationListener var1);

    ILocationProvider unRegisterLocationListener(AbsBaseLocationListener var1);

    ILocationProvider startLocation(Context var1);

    DefaultLocation getLastKnownLocation();

    double getLatitude();

    double getLongitude();

    ILocationProvider setLocateMode(LocateMode var1);

    ILocationProvider setInterval(LocationUpdateOption.IntervalMode var1);

    ILocationProvider setCoordinateType(int var1);

    ILocationProvider stopLocation();

    ILocationProvider enableMock(boolean var1);

    void setMockLatLng(Location var1);

    void setMockLatLng(double var1, double var3);

    boolean isMockEnabled();

    ILocationProvider setLogPath(File var1);

}
