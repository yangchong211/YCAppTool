package com.yc.location.dev;

import android.content.Context;
import android.location.Location;

import com.yc.location.bean.DefaultLocation;
import com.yc.location.config.LocateMode;
import com.yc.location.config.LocationUpdateOption;
import com.yc.location.listener.AbsBaseLocationListener;
import com.yc.location.listener.LocationListener;
import java.io.File;

public interface LocationServiceProvider {

    void initLocationManager(Context var1);

    void requestOnceLocation(LocationListener var1);

    LocationServiceProvider registerLocationListener(AbsBaseLocationListener var1);

    LocationServiceProvider unRegisterLocationListener(AbsBaseLocationListener var1);

    LocationServiceProvider startLocation(Context var1);

    DefaultLocation getLastKnownLocation();

    double getLatitude();

    double getLongitude();

    LocationServiceProvider setLocateMode(LocateMode var1);

    LocationServiceProvider setInterval(LocationUpdateOption.IntervalMode var1);

    LocationServiceProvider setCoordinateType(int var1);

    LocationServiceProvider stopLocation();

    LocationServiceProvider enableMock(boolean var1);

    void setMockLatLng(Location var1);

    void setMockLatLng(double var1, double var3);

    boolean isMockEnabled();

    LocationServiceProvider setLogPath(File var1);

}
