package com.yc.location.dev;

import android.content.Context;
import android.location.Location;
import android.support.annotation.IntDef;

import com.yc.location.bean.DefaultLocation;
import com.yc.location.config.LocateMode;
import com.yc.location.config.LocationUpdateOption;
import com.yc.location.listener.AbsBaseLocationListener;
import com.yc.location.listener.LocationListener;
import com.yc.location.log.LogHelper;
import com.yc.location.manager.DefaultLocationManager;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LocationPerformerImpl implements LocationServiceProvider {

    private static final String TAG = "LocationPerformerImpl";
    private static boolean sMockEnable = false;
    private DefaultLocationManager mLocationManager;
    private LocationUpdateOption mLocationUpdateOption;
    private MonitorLocationListener mDelegateListener = new MonitorLocationListener();
    private String mModelKey;
    private LocateMode mLocateMode;
    private LocationUpdateOption.IntervalMode mFrequency;
    private int sCordinateType = DefaultLocation.COORDINATE_TYPE_WGS84;

    @Override
    public DefaultLocation getLastKnownLocation() {
        if (mDelegateListener.isMockEnabled()) {
            return mDelegateListener.getMockDidiLocation();
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
    public LocationServiceProvider registerLocationListener(AbsBaseLocationListener locationListener) {
        mDelegateListener.add(locationListener);
        return this;
    }

    @Override
    public void requestOnceLocation(LocationListener didiLocationListener) {
        if (mLocationManager != null) {
            mLocationManager.requestLocationUpdateOnce(didiLocationListener, mModelKey);
        }
    }

    @Override
    public LocationServiceProvider setInterval(LocationUpdateOption.IntervalMode frequency) {
        if (frequency == null) {
            return this;
        }
        this.mFrequency = frequency;
        return this;
    }

    @Override
    public LocationServiceProvider setLocateMode(LocateMode mode) {
        if (mode == null) {
            return this;
        }
        this.mLocateMode = mode;
        return this;
    }

    @Override
    public LocationServiceProvider setCoordinateType(@CordinateType int type) {
        sCordinateType = type;
        if (mLocationManager != null) {
            if (mLocationManager.isRunning()) {
                LogHelper.d(TAG,"⚠️setCoordinateType() called with: type = [" + type + "] fail LocationManager isRunning");
            } else {
                mLocationManager.setCoordinateType(sCordinateType);
            }
        }
        return this;
    }

    @Override
    public LocationServiceProvider setLogPath(File file) {
        if (mLocationManager != null) {
            mLocationManager.setLogPath(file);
        }
        return this;
    }

    @Override
    public LocationServiceProvider startLocation(Context context) {
        LogHelper.d(TAG, "⚠️startLocation() called with: context = [" + context + "]");
        if (mLocationManager != null && mLocationManager.isRunning()) {
            return this;
        }
        if (mLocationManager == null) {
            initLocationManager(context);
        }
        int code = mLocationManager.requestLocationUpdates(mDelegateListener, mLocationUpdateOption);
        LogHelper.d(TAG,"code: " + code);
        return this;
    }

    @Override
    public LocationServiceProvider stopLocation() {
        LogHelper.d(TAG, "⚠️stopLocation() called");
        if (mLocationManager != null) {
            mLocationManager.removeLocationUpdates(mDelegateListener);
        }
        return this;
    }

    @Override
    public LocationServiceProvider enableMock(boolean enable) {
        sMockEnable = enable;
        mDelegateListener.setMockEnabled(enable);
        // 允许 mock 定位 true：允许模拟定位 false：禁用模拟定位
        mLocationManager.enableMockLocation(enable);
        // 启用 mock 位置过滤  true：不允许模拟定位 false：允许模拟定位
        // TencentExtraKeys.enableMockLocationFilter(!enable);
        return this;
    }

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

    @Override
    public LocationServiceProvider unRegisterLocationListener(AbsBaseLocationListener locationListener) {
        mDelegateListener.remove(locationListener);
        return this;
    }

    public void initLocationManager(Context context) {
        this.mLocationManager = DefaultLocationManager.getInstance(context);
        this.mLocationUpdateOption = mLocationManager.getDefaultLocationUpdateOption();
        this.mModelKey = context.getPackageName();
        if (mFrequency != null) {
            mLocationUpdateOption.setInterval(mFrequency);
        } else {
            mLocationUpdateOption.setInterval(LocationUpdateOption.IntervalMode.NORMAL);
        }
        mLocationUpdateOption.setModuleKey(mModelKey);
        mLocationManager.setAppid(mModelKey);
        //设置返回的定位坐标类型。默认为GCJ02坐标(国内定位时使用的坐标)。
        //1.当前只有做国际版业务时，才需要调用此函数。别的业务线或其他情况下禁止调用此函数。
        //2.若需要调用，请在初始化阶段、请求定位之前调用。
        mLocationManager.setCoordinateType(sCordinateType);
    }

    @IntDef({DefaultLocation.COORDINATE_TYPE_GCJ02, DefaultLocation.COORDINATE_TYPE_WGS84})
    @Retention(RetentionPolicy.SOURCE)
    @interface CordinateType {

    }
}

