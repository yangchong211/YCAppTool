package com.yc.location.mode.gps;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Looper;

import com.yc.location.constant.Constants;
import com.yc.location.manager.DefaultLocationManager;
import com.yc.location.log.LogHelper;
import com.yc.location.monitor.LocationSensorMonitor;
import com.yc.location.utils.LocationUtils;
import com.yc.location.bean.DefaultLocation;
import com.yc.location.listener.LocationUpdateInternalListener;

import java.util.Iterator;

/**
 * <pre>
 *     @author  yangchong
 *     email  : yangchong211@163.com
 *     time   : 2017/07/22
 *     desc   : gps定位
 *     revise :
 * </pre>
 */
public class GpsManager {

    private final Context mContext;
    private LocationManager mLocationManager;
    private Location gpsLocation = null;
    private long mLastGpsEvent = 0;
    private GpsStatus mGpsStat = null;
    private float mGpsSignalLevel = 0.0f;
    private int mSatelliteNumber = 0;
    private volatile long timeGpsM = 0;
    private long mGpsValidateInterval;
    private LocationUpdateInternalListener mLocationInternalListener;

    public GpsManager(Context context) {
        mContext = context;
        mGpsValidateInterval = Constants.gpsLocTimeIntevalMillis;
    }

    public void init(long interval) {
        if (mContext == null) {
            return;
        }
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (!LocationUtils.hasGpsProvider(mLocationManager)) {
            LogHelper.logFile("initGpsListeners: does not found gps provider");
            return;
        }

        String strC = "force_xtra_injection";
        String strGpsProvider = LocationManager.GPS_PROVIDER;
        try {
            boolean b = mLocationManager.sendExtraCommand(strGpsProvider, strC, null);
            LogHelper.logFile("using agps: " + b);
        } catch (Exception e) {
            //
        }

        try {
            mLocationManager.addGpsStatusListener(mGpsStatusListener);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    interval, 0, mLocationListener, Looper.myLooper());
        } catch (Throwable e) {//fix crash:http://omega.xiaojukeji.com/app/quality/crash/detail?app_id=1&msgid=QmMM3u-uT5ygJigMoU7opQ
            LogHelper.logFile("initGpsListeners exception, " + e.getMessage());
            int status;
            if (e instanceof SecurityException) {
                status = DefaultLocation.STATUS_GPS_DENIED;
            } else {
                status = DefaultLocation.STATUS_GPS_UNAVAILABLE;
            }
            if (null != mLocationInternalListener) {
                mLocationInternalListener.onStatusUpdate(DefaultLocation.STATUS_GPS, status);
            }

        }
    }

    private long mGetGpsSignalTime = 0;
    private GpsStatus.Listener mGpsStatusListener = new GpsStatus.Listener() {
        @Override
        public void onGpsStatusChanged(int event) {
            mGetGpsSignalTime = LocationUtils.getTimeBoot();
            dispatchGpsStatusChange(event);

        }
    };

    //为控制写日志间隔
    private long mGpsBamaiLogTime = 0l;
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mGetGpsSignalTime = LocationUtils.getTimeBoot();

            if (!LocationUtils.locCorrect(location)) {
                return;
            }

            boolean isMockLocation = LocationUtils.isMockLocation(location);
            LocationUtils.setIsGpsMocked(isMockLocation);

            if (isMockLocation && !DefaultLocationManager.enableMockLocation) {
                LogHelper.logFile("on gps callback, mock loc and disable mock!");
                return;
            }

//            if (regularGpsCount != null) {
//                regularGpsCount.count();
//            }
            //有间隔地写日志。
            long nowTime = System.currentTimeMillis();
            if((nowTime - mGpsBamaiLogTime) > Constants.MIN_INTERVAL_BAMAI_GPS_NLP_LOCATION) {
                LogHelper.logFile("-onLocationChanged-: type gps, location: " + location.getLongitude()
                        + "," + location.getLatitude() + ", " +location.getSpeed() + ", " + location.getBearing());
                mGpsBamaiLogTime = nowTime;
            }

            gpsLocation = location;
            timeGpsM = System.currentTimeMillis();

            LocationSensorMonitor.getInstance(mContext).setGpsFixedTimestamp(timeGpsM);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (LocationManager.GPS_PROVIDER.equals(provider)) {
                switch (status) {
                    case LocationProvider.OUT_OF_SERVICE:
                        if (null != mLocationInternalListener) {
                            mLocationInternalListener.onStatusUpdate(DefaultLocation.STATUS_GPS, DefaultLocation.STATUS_GPS_UNAVAILABLE);
                        }
                        LogHelper.logFile("gps provider out of service");
                        gpsLocation = null;
                        break;
                    case LocationProvider.AVAILABLE:
                        if (null != mLocationInternalListener) {
                            mLocationInternalListener.onStatusUpdate(DefaultLocation.STATUS_GPS, DefaultLocation.STATUS_GPS_AVAILABLE);
                        }
                        LogHelper.logFile("gps provider available");
                        break;
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        LogHelper.logFile("gps provider temporarily unavailable");
//                        gpsLocation = null;
                        break;
                    default:
                        break;

                }
            }

        }

        @Override
        public void onProviderEnabled(String provider) {
            LogHelper.logFile("gps provider enabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            LogHelper.logFile("gps provider disabled");
            gpsLocation = null;
        }
    };

    /**
     * 处理手机GPS状态切换的回调
     *
     * @param
     * @return void
     */
    private void dispatchGpsStatusChange(int iEvent) {
        if (mLocationManager == null) {
            return;
        }
        //fix crash:极个别手机上报权限crash
        try {
            if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                return;
            }
        } catch (Throwable e) {
            return;
        }

        switch (iEvent) {
            case GpsStatus.GPS_EVENT_STARTED:
                LogHelper.logFile("gps event started");
                break;
            case GpsStatus.GPS_EVENT_STOPPED:
                LogHelper.logFile("gps event stopped");
                gpsLocation = null;
                break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                LogHelper.logFile("gps event first fix");

                break;
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                if (isGpsEventOverFreq()) {
                    return;
                }
                //liuchangcheng: fix bug: 5171
                try {
                    mLastGpsEvent = LocationUtils.getTimeBoot();

                    mGpsSignalLevel = 0.0f;
                    mGpsStat = mLocationManager.getGpsStatus(null);
                    int iMaxSate = mGpsStat.getMaxSatellites();
                    Iterator<GpsSatellite> iter = null;
                    iter = mGpsStat.getSatellites().iterator();
                    mSatelliteNumber = 0;
                    int iFix = 0;
                    while (iter.hasNext() && mSatelliteNumber <= iMaxSate) {
                        GpsSatellite gs = iter.next();
                        mGpsSignalLevel += gs.getSnr();
                        mSatelliteNumber++;
                        if (gs.usedInFix()) {
                            iFix++;
                        }
                    }
                    LogHelper.logFile("gps satellite number:" + "(" + iFix + ")/" + mSatelliteNumber
                            + " level:" + mGpsSignalLevel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 判断GPSEVENT通知是否过于频繁
     *
     * @param
     * @return boolean
     */
    private boolean isGpsEventOverFreq() {
        if (mLastGpsEvent == 0l) {
            return false;
        }

        return (LocationUtils.getTimeBoot() - mLastGpsEvent) < Constants.MIN_GPS_EVENT_GAP;
    }

    /** 移除gps监听器 */
    public void rmGpsListeners() {

        if (mContext == null || mLocationManager == null) return;

        try {
            mLocationManager.removeGpsStatusListener(mGpsStatusListener);
            mLocationManager.removeUpdates(mLocationListener);
        } catch (Throwable e) {
            LogHelper.logFile("rmGpsListeners exception, " + e.getMessage());
        }
        gpsLocation = null;
        mLocationManager = null;
        mLocationInternalListener = null;
    }

    /** 检测gps数据是否最新 - 30s */
    public boolean isGpsLocationValid() {

        if (!LocationUtils.locCorrect(gpsLocation)) {
            return false;
        }

        boolean validGPS;
        long cur = System.currentTimeMillis();
        validGPS = (cur - timeGpsM < mGpsValidateInterval); // 动态配置：默认30s内有效

        if (!validGPS) {
//            LogHelper.logBamai("CHECK GPS out of date");

            gpsLocation = null;
        }

        if (validGPS) {
            if (DefaultLocationManager.enableMockLocation) {                      // 允许mock时，直接返回
                return true;
            }
            if (LocationUtils.isMockSettingsON(mContext)) {                            // 系统mock开关开启时，阻止
                LogHelper.logFile("Mock GPS switch is ON, SDK ignore GPS");
                return false;
            }
            if (LocationUtils.isMockLocation(gpsLocation)) {                                       // mock检测生效时，阻止
                LogHelper.logFile("Mock GPS location tested, SDK ignore GPS");
                return false;
            }
            // 不确定，暂时判断为真GPS
            return true;
        }

        return false;
    }

//    /**
//     * 设置GPS失效时间间隔
//     * @param gpsValidateInterval
//     */
//    void setGpsValidateInterval(long gpsValidateInterval) {
//        this.mGpsValidateInterval = gpsValidateInterval;
//    }

    public Location getGpsLocation() {
        return gpsLocation;
    }

    public void setLocationInternalListener(LocationUpdateInternalListener locationInternalListener) {
        this.mLocationInternalListener = locationInternalListener;

    }
    
    public long getReceiveGpsSignalTime() {
        return mGetGpsSignalTime;
    }

}
