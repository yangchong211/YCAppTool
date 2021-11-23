package com.yc.location.manager;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import com.yc.location.constant.Constants;
import com.yc.location.log.LogHelper;
import com.yc.location.utils.LocationUtils;
import com.yc.location.utils.ReflectUtils;

/**
 * NLP的管理类，可以从此获得有效NLP 定位位置等
 */
public class NLPManager {

    private Context context;
    private LocationManager locationManager;
    private volatile long updateNlpStamp = 0L;
//    private double[] lonlatacc = new double[3];
    private volatile Location originNLPLocation = null;
    private volatile boolean isRunning = false;
    private Handler mWorkHandler;

//    private long mNLPBamaiLogTime = 0l;

    public NLPManager(Context c, Handler handler) {
        context = c;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mWorkHandler = handler;
    }

    public synchronized void start() {
        if (!isRunning) {
            isRunning = true;
            try {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0f, locationListener, mWorkHandler.getLooper());
            } catch (SecurityException e) {
                LogHelper.logBamai("mNLPManager#start: exception:" + e.getMessage());
            } catch (Exception e) {
                LogHelper.logBamai("mNLPManager#start: exception:" + e.getMessage());
            }
        }
    }

    public synchronized void stop() {

        if (isRunning) {
            //
            try {
                locationManager.removeUpdates(locationListener);
            } catch (SecurityException e) {
                LogHelper.logBamai("mNLPManager#stop: exception:" + e.getMessage());
            } catch (Exception e) {
                LogHelper.logBamai("mNLPManager#stop: exception:" + e.getMessage());

            }

            mWorkHandler = null;
            isRunning = false;
        }

    }

    public Location getNLPLocation() {
        if (LocationUtils.locCorrect(originNLPLocation)) {
            if ((LocationUtils.getTimeBoot() - updateNlpStamp > Constants.VALIDATE_INTERVAL_NLP_LOCATION)) {
                originNLPLocation = null;
            }
        } else {
            originNLPLocation = null;
        }
        return originNLPLocation;
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (!LocationUtils.locCorrect(location)) {
                return;
            }

            // mock gps可能对NLP造成影响，出现这种情况，默认设置为国内，不保存坐标。TODO:LCC:验证此问题，查资料问雨雷。
            if (!DefaultLocationManager.enableMockLocation) { // SDK 禁止 mock
                boolean isMock;
                if (LocationUtils.isMockSettingsON(context)) {
                    isMock = true;
                } else if (LocationUtils.isGpsMocked()) {
                    isMock = true;
                } else {
                    Object fromMock = ReflectUtils.invokeMethod(location, "isFromMockProvider");
                    isMock = fromMock != null && (boolean) fromMock;
                }
                if (isMock) {
                    return;
                }
            }

            //有间隔地写日志。
//            long nowTime = System.currentTimeMillis();
//            if((nowTime - mNLPBamaiLogTime) > Const.MIN_INTERVAL_BAMAI_GPS_NLP_LOCATION) {
//                LogHelper.logBamai("-onLocationChanged-: type gps, location: " + location.toString());
//                mNLPBamaiLogTime = nowTime;
//            }

            if ((location.getLatitude() == 0.0d && location.getLongitude() == 0.0d)
                    || location.getAccuracy() <= 0.0f) {
                LogHelper.logBamai("zero nlp location: " + String.valueOf(location));
            } else {
                originNLPLocation = location;
                updateNlpStamp = LocationUtils.getTimeBoot();
            }
//            double[] lonlat = EvilTransform.transform(location.getLongitude(), location.getLatitude());

//            synchronized (this) {
//                if (lonlatacc == null) lonlatacc = new double[3];
//                lonlatacc[0] = lonlat[0];
//                lonlatacc[1] = lonlat[1];
//                lonlatacc[2] = location.getAccuracy();

//                if (null != location) {
//                    LogHelper.logBamai("NLP change: " + location.getProvider()
//                            + ", " + location.getLongitude() + ", " + location.getLatitude()
//                            + ", " + location.getAccuracy());
//                }
//            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
