package com.yc.location.mode.gps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;

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
    private volatile long timeGpsM = 0;
    private final long mGpsValidateInterval;
    private long mGetGpsSignalTime = 0;
    //为控制写日志间隔
    private long mGpsLogTime = 0L;
    private LocationUpdateInternalListener mLocationInternalListener;
    private static final String TAG = "GpsManager : ";

    public GpsManager(Context context) {
        mContext = context;
        mGpsValidateInterval = Constants.gpsLocTimeIntevalMillis;
    }

    public void init(long interval) {
        if (mContext == null) {
            return;
        }
        //1.获取位置管理器
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (!LocationUtils.hasGpsProvider(mLocationManager)) {
            LogHelper.logFile(TAG+"initGpsListeners: does not found gps provider");
            return;
        }
        //添加用户权限申请判断
        if (ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //第二步：获取位置提供器
        //GPS 定位的精准度比较高，但是非常耗电。
        String strC = "force_xtra_injection";
        String strGpsProvider = LocationManager.GPS_PROVIDER;
        try {
            boolean b = mLocationManager.sendExtraCommand(strGpsProvider, strC, null);
            LogHelper.logFile(TAG+"using agps: " + b);
        } catch (Exception e) {
            //
        }
        try {
            mLocationManager.addGpsStatusListener(mGpsStatusListener);
            //第四步：监视地理位置变化
            /*
             * 参1:选择定位的方式
             * 参2:定位的间隔时间
             * 参3:当位置改变多少时进行重新定位
             * 参4:位置的回调监听
             * 参5:looper
             */
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    interval, 0, mLocationListener, Looper.myLooper());
        } catch (Throwable e) {
            //fix crash:http://omega.xiaojukeji.com/app/quality/crash/detail?app_id=1&msgid=QmMM3u-uT5ygJigMoU7opQ
            LogHelper.logFile(TAG+"initGpsListeners exception, " + e.getMessage());
            int status;
            if (e instanceof SecurityException) {
                //GPS定位权限被禁止
                status = DefaultLocation.STATUS_GPS_DENIED;
            } else {
                //GPS模块定位是不可用的
                status = DefaultLocation.STATUS_GPS_UNAVAILABLE;
            }
            if (null != mLocationInternalListener) {
                mLocationInternalListener.onStatusUpdate(DefaultLocation.STATUS_GPS, status);
            }
        }
    }

    private final GpsStatus.Listener mGpsStatusListener = new GpsStatus.Listener() {
        @Override
        public void onGpsStatusChanged(int event) {
            mGetGpsSignalTime = LocationUtils.getTimeBoot();
            dispatchGpsStatusChange(event);
        }
    };

    private final LocationListener mLocationListener = new GpsLocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mGetGpsSignalTime = LocationUtils.getTimeBoot();
            if (!LocationUtils.locCorrect(location)) {
                return;
            }
            boolean isMockLocation = LocationUtils.isMockLocation(location);
            LocationUtils.setIsGpsMocked(isMockLocation);
            if (isMockLocation && !DefaultLocationManager.enableMockLocation) {
                LogHelper.logFile(TAG+"on gps callback, mock loc and disable mock!");
                return;
            }
            //有间隔地写日志。
            long nowTime = LocationUtils.getNowTime();
            if((nowTime - mGpsLogTime) > Constants.MIN_INTERVAL_BAMAI_GPS_NLP_LOCATION) {
                LogHelper.logFile(TAG+"-onLocationChanged-: type gps, location: " + location.getLongitude()
                        + "," + location.getLatitude() + ", " +location.getSpeed() + ", " + location.getBearing());
                mGpsLogTime = nowTime;
            }
            gpsLocation = location;
            timeGpsM = LocationUtils.getNowTime();
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
                        LogHelper.logFile(TAG+"gps provider out of service");
                        gpsLocation = null;
                        break;
                    case LocationProvider.AVAILABLE:
                        if (null != mLocationInternalListener) {
                            mLocationInternalListener.onStatusUpdate(DefaultLocation.STATUS_GPS, DefaultLocation.STATUS_GPS_AVAILABLE);
                        }
                        LogHelper.logFile(TAG+"gps provider available");
                        break;
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        LogHelper.logFile(TAG+"gps provider temporarily unavailable");
                        break;
                    default:
                        break;
                }
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            LogHelper.logFile(TAG+"gps provider enabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            LogHelper.logFile(TAG+"gps provider disabled");
            gpsLocation = null;
        }
    };

    /**
     * 处理手机GPS状态切换的回调
     * @param iEvent                        event
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
                LogHelper.logFile(TAG+"gps event started");
                break;
            case GpsStatus.GPS_EVENT_STOPPED:
                LogHelper.logFile(TAG+"gps event stopped");
                gpsLocation = null;
                break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                LogHelper.logFile(TAG+"gps event first fix");
                break;
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                boolean gpsEventOverFreq = isGpsEventOverFreq();
                LogHelper.d(TAG,gpsEventOverFreq ? "gps event too often" : "gps event normal");
                if (gpsEventOverFreq) {
                    return;
                }
                try {
                    mLastGpsEvent = LocationUtils.getTimeBoot();
                    float mGpsSignalLevel = 0.0f;
                    GpsStatus mGpsStat = mLocationManager.getGpsStatus(null);
                    int iMaxSate = mGpsStat.getMaxSatellites();
                    Iterator<GpsSatellite> iter = null;
                    iter = mGpsStat.getSatellites().iterator();
                    int mSatelliteNumber = 0;
                    int iFix = 0;
                    while (iter.hasNext() && mSatelliteNumber <= iMaxSate) {
                        GpsSatellite gs = iter.next();
                        mGpsSignalLevel += gs.getSnr();
                        mSatelliteNumber++;
                        if (gs.usedInFix()) {
                            iFix++;
                        }
                    }
                    LogHelper.logFile(TAG+"gps satellite number:" + "(" + iFix + ")/" + mSatelliteNumber
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
     * @return boolean
     */
    private boolean isGpsEventOverFreq() {
        if (mLastGpsEvent == 0L) {
            return false;
        }
        return (LocationUtils.getTimeBoot() - mLastGpsEvent) < Constants.MIN_GPS_EVENT_GAP;
    }

    /**
     * 移除gps监听器
     */
    public void removeGpsListeners() {
        if (mContext == null || mLocationManager == null) {
            return;
        }
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

    /**
     * 检测gps数据是否最新 - 30s
     * @return                      是否是最新
     */
    public boolean isGpsLocationValid() {
        LogHelper.d(TAG,"isGpsLocationValid");
        boolean locCorrect = LocationUtils.locCorrect(gpsLocation);
        LogHelper.d(TAG,"locCorrect , " + locCorrect);
        if (!locCorrect) {
            return false;
        }
        boolean validGPS;
        long cur = System.currentTimeMillis();
        validGPS = (cur - timeGpsM < mGpsValidateInterval);
        LogHelper.i(TAG,"validGPS " + validGPS);
        // 动态配置：默认30s内有效
        if (!validGPS) {
            gpsLocation = null;
        }
        if (validGPS) {
            // 允许mock时，直接返回
            if (DefaultLocationManager.enableMockLocation) {
                LogHelper.logFile(TAG+"Mock GPS Enable , return");
                return true;
            }
            // 系统mock开关开启时，阻止
            if (LocationUtils.isMockSettingsON(mContext)) {
                LogHelper.logFile(TAG+"Mock GPS switch is ON, SDK ignore GPS");
                return false;
            }
            // mock检测生效时，阻止
            if (LocationUtils.isMockLocation(gpsLocation)) {
                LogHelper.logFile(TAG+"Mock GPS location tested, SDK ignore GPS");
                return false;
            }
            // 不确定，暂时判断为真GPS
            return true;
        }
        return false;
    }

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
