package com.yc.zxingserver.scan;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;

import com.yc.zxingserver.camera.CameraManager;
import com.yc.zxingserver.camera.FrontLightMode;

/**
 * Detects ambient light and switches on the front light when very dark, and off again when sufficiently light.
 *
 * @author Sean Owen
 * @author Nikolaus Huber
 */
final class AmbientLightManager implements SensorEventListener {

    protected static final float TOO_DARK_LUX = 45.0f;
    protected static final float BRIGHT_ENOUGH_LUX = 100.0f;

    /**
     * 光线太暗时，默认：照度45 lux
     */
    private float tooDarkLux = TOO_DARK_LUX;
    /**
     * 光线足够亮时，默认：照度450 lux
     */
    private float brightEnoughLux = BRIGHT_ENOUGH_LUX;

    private final Context context;
    private CameraManager cameraManager;
    private Sensor lightSensor;

    AmbientLightManager(Context context) {
        this.context = context;
    }

    void start(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (FrontLightMode.readPref(sharedPrefs) == FrontLightMode.AUTO) {
            SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            if (lightSensor != null) {
                sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    }

    void stop() {
        if (lightSensor != null) {
            SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            sensorManager.unregisterListener(this);
            cameraManager = null;
            lightSensor = null;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float ambientLightLux = sensorEvent.values[0];
        if (cameraManager != null) {
            if (ambientLightLux <= tooDarkLux) {
                cameraManager.sensorChanged(true,ambientLightLux);
            } else if (ambientLightLux >= brightEnoughLux) {
                cameraManager.sensorChanged(false,ambientLightLux);
            }
        }
    }

    public void setTooDarkLux(float tooDarkLux){
        this.tooDarkLux = tooDarkLux;
    }

    public void setBrightEnoughLux(float brightEnoughLux){
        this.brightEnoughLux = brightEnoughLux;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }

}