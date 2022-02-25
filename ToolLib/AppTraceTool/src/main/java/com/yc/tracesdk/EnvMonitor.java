package com.yc.tracesdk;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;

import com.yc.tracesdk.LocInfoProtoBuf.EnvInfo;


public class EnvMonitor {

    private static volatile EnvMonitor mEnvMonitor;

    private Context          mContext;
    private WifiManager      mWifiManager;
    private LocationManager  mLocationManager;
    private SensorManager    mSensorManager;
    private Sensor           mLightSensor;
    private Sensor           mAirPressureSensor;
    //private Sensor          mAccelerometerSensor;
    private BluetoothAdapter mBluetoothAdapter;
    private Handler          mHandler;
    private boolean          mEnableRegularScan;
    private boolean          mWifiConnected;
    private boolean          mWifiEnabled;
    private boolean          mGpsEnabled;
    private boolean          mBluetoothEnabled;
    private long             mGpsLastFixedUpdate_ts;
    private long             mGpsLastFixed_ts;
    private long             mGpsFixedInterval;
    private long             mLightUpdate_ts;
    private long             mPressureUpdate_ts;
    //private long            mAccelerometerUpdate_ts;
    private float            mLightValue;
    private float            mAirPressureValue;
    //private float[]         mAccelerometerValue;

    private static final long SCAN_INTERVAL = 20 * 1000;

    /**
     * 内部构造，相关对象之初始化（不申请任何资源）
     * @param context
     */
    private EnvMonitor(Context context) {

        if(context == null) return;

        mContext = context.getApplicationContext();
        mEnableRegularScan = false;
        mWifiConnected = false;
        mWifiEnabled = false;
        mGpsEnabled = false;
        mBluetoothEnabled = false;
        mGpsLastFixed_ts = 0L;
        mGpsFixedInterval = 0L;
        mGpsLastFixedUpdate_ts = 0L;
        mLightUpdate_ts = 0L;
        mPressureUpdate_ts = 0L;
        //mAccelerometerUpdate_ts = 0L;
        mLightValue = 0.f;
        mAirPressureValue = 0.f;
        //mAccelerometerValue = new float[3];
    }

    static EnvMonitor getInstance(Context context) {

        if(mEnvMonitor == null) {
            synchronized (EnvMonitor.class) {
                if(mEnvMonitor == null) {
                    mEnvMonitor = new EnvMonitor(context);
                }
            }
        }

        return mEnvMonitor;
    }

    void start() {

        // ===== start env loop
        mHandler = new Handler();
        mEnableRegularScan = true;
        mHandler.post(mRegularScanRunnable);

        // ==== start wifi connection status listener
        mWifiManager = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        try {
            mContext.registerReceiver(mWifiReceiver, intentFilter);
        } catch (SecurityException e) {}

        // ==== start gps fixed interval listener
        mLocationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
        try {
            mLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1000, 10, mLocationListener);
        } catch (Exception e) {  };

        // ==== start sensor light/pressure listener
        mSensorManager = (SensorManager)mContext.getSystemService(Context.SENSOR_SERVICE);
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mAirPressureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        //mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        try {
            mSensorManager.registerListener(sensorEventListener, mLightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } catch (Exception e) {}

        try {
            mSensorManager.registerListener(sensorEventListener, mAirPressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } catch (Exception e) {}
        //mSensorManager.registerListener(sensorEventListener, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

        // ==== init bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    }

    void stop() {

        try {

            // ==== stop sensor light/pressure listener
            mSensorManager.unregisterListener(sensorEventListener);

            // ==== stop gps fixed interval listener
            mLocationManager.removeUpdates(mLocationListener);

            // ==== stop wifi connection status listener
            mContext.unregisterReceiver(mWifiReceiver);

            // ==== stop env loop
            mEnableRegularScan = false;
            if (mHandler != null) mHandler.removeCallbacks(mRegularScanRunnable);

        } catch (Exception e) { }

    }

    private Runnable mRegularScanRunnable = new Runnable() {
        @Override
        public void run() {

            LogHelper.log(" Env regular loop running. ");

            mWifiEnabled = false;
            mBluetoothEnabled = false;
            try {
                mWifiEnabled = mWifiManager.isWifiEnabled();
                mGpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                mBluetoothEnabled = mBluetoothAdapter.isEnabled();
            } catch (Exception e) {

            }

            // ==== build env data to proto buf
            long now_ts = System.currentTimeMillis();
            EnvInfo.Builder envBuilder = new EnvInfo.Builder();

            envBuilder.time(now_ts);
            envBuilder.wifi_enabled(mWifiEnabled ? 1 : 0);
            envBuilder.wifi_connected(mWifiConnected ? 1 : 0);
            envBuilder.gps_enabled(mGpsEnabled ? 1 : 0);
            envBuilder.gps_fix_interv(mGpsLastFixedUpdate_ts - now_ts > SCAN_INTERVAL ? 0 : mGpsFixedInterval);
            envBuilder.light(mLightUpdate_ts - now_ts > SCAN_INTERVAL ? 0 : (int) mLightValue);
            envBuilder.air_pressure(mPressureUpdate_ts - now_ts > SCAN_INTERVAL ? 0 : (int) (mAirPressureValue * 100));

            //EnvInfo.Accelerometer.Builder accBuilder = new EnvInfo.Accelerometer.Builder();
            //accBuilder.x_axis(0.f);
            //accBuilder.y_axis(0.f);
            //accBuilder.z_axis(0.f);
            //envBuilder.accelerometer(accBuilder.build());

            envBuilder.bluetooth_enabled(mBluetoothEnabled ? 1 : 0);

            try {
                byte[] resbytes = envBuilder.build().toByteArray();
                DBHandler.getInstance(mContext).insertEnvData(resbytes);
            } catch (Exception e) {}

            if(mEnableRegularScan && mHandler != null) mHandler.postDelayed(mRegularScanRunnable, SCAN_INTERVAL);
        }
    };

    BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {
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

    LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if(location != null) {
                long now_ts = System.currentTimeMillis();
                if(mGpsLastFixed_ts != 0) {
                    mGpsFixedInterval = now_ts - mGpsLastFixed_ts;
                    mGpsLastFixedUpdate_ts = now_ts;
                }
                mGpsLastFixed_ts = now_ts;
            }
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

    SensorEventListener sensorEventListener = new SensorEventListener() {
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
//            if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//                mAccelerometerUpdate_ts = now_ts;
//                mAccelerometerValue[0] = event.values[0];
//                mAccelerometerValue[1] = event.values[1];
//                mAccelerometerValue[2] = event.values[2];
//            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
