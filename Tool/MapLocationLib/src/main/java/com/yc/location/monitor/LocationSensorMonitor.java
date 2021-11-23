package com.yc.location.monitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;
import android.provider.Settings;

import com.yc.location.utils.AppToolUtils;
import com.yc.location.utils.ReflectUtils;
import com.yc.location.utils.LocationUtils;


public class LocationSensorMonitor {

    private static volatile LocationSensorMonitor sensorMonitor = null;

    private Context          mContext;
    private WifiManager      mWifiManager;
    private SensorManager    mSensorManager;
    private Sensor           mLightSensor;
    private Sensor           mAirPressureSensor;

    private boolean          mWifiConnected = false;

    private float            mLightValue          = 0.f;
    private float            mAirPressureValue    = 0.f;

    private long             mLightUpdate_ts      = 0L;
    private long             mPressureUpdate_ts   = 0L;

    private long             mGpsLastFixed_ts      = 0L;
    private long             mGpsFixedInterval     = 0L;

    private static final long SCAN_INTERVAL = 20 * 1000;

    private LocationSensorMonitor(Context c) {
        mContext = c;
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);

    }

    public static LocationSensorMonitor getInstance(Context context) {

        if (context == null) return null;

        if (sensorMonitor == null) {
            synchronized (LocationSensorMonitor.class) {
                if (sensorMonitor == null) {
                    sensorMonitor = new LocationSensorMonitor(context);
                }
            }
        }

        return sensorMonitor;
    }

    public void start() {

        if (mContext == null) return;

        // ==== start wifi connection status listener
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
            mContext.registerReceiver(mWifiReceiver, intentFilter);
        } catch (Exception e) {}

        // ==== start sensor light/pressure listener
        mSensorManager = (SensorManager)mContext.getSystemService(Context.SENSOR_SERVICE);
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mAirPressureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        try {
            mSensorManager.registerListener(sensorEventListener, mLightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } catch (Exception e) {}

        try {
            mSensorManager.registerListener(sensorEventListener, mAirPressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } catch (Exception e) {}
    }

    public void stop() {

        if (mContext == null) return;

        // ==== stop sensor light/pressure listener
        mSensorManager.unregisterListener(sensorEventListener);

        // ==== stop wifi connection status listener
        try {
            mContext.unregisterReceiver(mWifiReceiver);
        } catch (Exception e) {

        }
    }

    /**
     * wifi是否打开
     * @return
     */
    public boolean isWifiEnabled() {
        boolean wifiEnabled = false;

        if (mWifiManager == null) return wifiEnabled;

        try {
            wifiEnabled = mWifiManager.isWifiEnabled();
        } catch (Exception e) {}

        return wifiEnabled;
    }

    /**
     * wifi是否允许扫描（设置中有wifi在未打开时是否允许扫描的开关）
     * @return
     */
    public boolean isWifiAllowScan() {
        boolean bWifiEnabled = false;
        if (mWifiManager == null) {
            return bWifiEnabled;
        }
        if (AppToolUtils.getSdk() > 17) {
      /*
       * 判断是否开启了WIFI持续扫描
       */
            try {
                Object o = ReflectUtils.invokeMethod(mWifiManager, "isScanAlwaysAvailable");
                bWifiEnabled = String.valueOf(o).equals("true");
                if (bWifiEnabled) {
//                    LogHelper.logBamai("api" + Utils.getSdk() + " always scan");
                }

            } catch (Exception e) {
            }
        }
        return bWifiEnabled;
    }

    public boolean isGpsEnabled() {
        String str = Settings.Secure.getString(
                mContext.getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (str != null) {
            return str.contains("gps");
        } else {
            return false;
        }
    }

    protected boolean isWifiConnected() {
        return mWifiConnected;
    }

    public  int getAirPressure() {
        long now = System.currentTimeMillis();

        return now - mPressureUpdate_ts > SCAN_INTERVAL ? 0 : (int) (mAirPressureValue*100);
    }

    public  int getLight() {
        long now = System.currentTimeMillis();

        return now - mLightUpdate_ts > SCAN_INTERVAL ? 0 : (int) mLightValue;
    }

    public  void setGpsFixedTimestamp(long timestamp) {
        if (mGpsLastFixed_ts != 0L)
            mGpsFixedInterval = timestamp - mGpsLastFixed_ts;
        mGpsLastFixed_ts = timestamp;
    }

    public  int getGpsFixedInterval() {
        long now = System.currentTimeMillis();

        return now - mGpsLastFixed_ts > SCAN_INTERVAL ? 0 : (int)mGpsFixedInterval;
    }

    public BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent != null && intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
                SupplicantState supplicantState = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
                if(supplicantState != null && supplicantState.equals(SupplicantState.COMPLETED)) {
                    mWifiConnected = true;
                } else {
                    mWifiConnected = false;
                }
            }
        }
    };

    public SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            long now_ts = System.currentTimeMillis();

            if(event.sensor.getType() == Sensor.TYPE_LIGHT) {
                mLightUpdate_ts = now_ts;
                mLightValue = event.values[0];
            }
            if(event.sensor.getType() == Sensor.TYPE_PRESSURE) {
                mPressureUpdate_ts = now_ts;
                mAirPressureValue = event.values[0];
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
