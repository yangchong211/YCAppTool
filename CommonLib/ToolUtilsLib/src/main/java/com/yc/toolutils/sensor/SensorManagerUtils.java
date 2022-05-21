package com.yc.toolutils.sensor;

import android.app.Application;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;

import androidx.annotation.IntDef;
import androidx.annotation.RequiresApi;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * description:手机系统方向监听器工具类
 * @author  杨充
 * @since   2021/10/20
 */
public final class SensorManagerUtils {

    private static final String TAG = "SensorManagerUtils";
    //通过这个类去创建一个传感器服务的实例，这个类提供的各种方法可以访问传感器列表、注册或解除注册传感器事件监听、获取方位信息等。
    private SensorManager sensorManager;
    //用于创建一个特定的传感器实例，这个类提供的方法可以让你决定一个传感器的功能
    private Sensor sensor;
    private Sensor defaultSensor;
    private final Context context;
    private final int type;

    public SensorManagerUtils(Context context){
        if (context instanceof Application){
            this.context = context;
        } else {
            this.context = context.getApplicationContext();
        }
        this.type = TYPE_NEW;
    }

    public SensorManagerUtils(Context context , int type){
        if (context instanceof Application){
            this.context = context;
        } else {
            this.context = context.getApplicationContext();
        }
        this.type = type;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void registerSensorCallback(OrientEventListener sensorEventListener) {
        checkSensor();
        if (sensor != null) {
            sensorManager.registerListener(sensorEventListener,
                    sensor, SensorManager.SENSOR_DELAY_NORMAL,SensorManager.SENSOR_DELAY_UI);
            sensorEventListener.init(context);
        }
    }

    public void unregisterSensorCallback(OrientEventListener sensorEventListener) {
        checkSensor();
        if (sensor != null) {
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void registerOrientSensorCallback(OrientSensorListener sensorEventListener) {
        checkSensor();
        if (sensor != null) {
            //SENSOR_DELAY_UI 适合用户界面的速率
            //注册监听时第三个参数指的是延迟时间：有四种类型，也可以自定一定时间，微秒(1 毫秒=1000 微秒)
            //SensorManager.SENSOR_DELAY_FASTEST：最快，延迟最小，同时也最消耗资源，一般只有特别依赖传感器的应用使用该频率，否则不推荐。
            //SensorManager.SENSOR_DELAY_GAME：适合游戏的频率，一般有实时性要求的应用适合使用这种频率。
            //SensorManager.SENSOR_DELAY_NORMAL：正常频率，一般对实时性要求不高的应用适合使用这种频率。
            //SensorManager.SENSOR_DELAY_UI：适合普通应用的频率，这种模式比较省电，而且系统开销小，但延迟大，因此只适合普通小程序使用。
            sensorManager.registerListener(sensorEventListener, sensor,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
            sensorManager.registerListener(sensorEventListener, defaultSensor,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
            sensorEventListener.init(context);
        }
    }

    public void unregisterOrientSensorCallback(OrientSensorListener sensorEventListener) {
        checkSensor();
        if (sensor != null) {
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    private void checkSensor() {
        try {
            //获取传感器管理对象
            sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            //获取传感器的类型
            if (type == TYPE_OLD){
                //Sensor.TYPE_ORIENTATION：方向传感器。不建议使用呢
                //在高版本的安卓中，Sensor.TYPE_ORIENTATION被画上了横线，那么我们鼠标放上去看下IDE提示是这样写的：
                //The fieldSensor.TYPE_ORIENTATION is deprecated,意思是说这个方法已经被弃用了。
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            } else {
                //使用下面两个
                //方向传感器是基于软件的，，并且它的数据是通过加速度传感器和磁场传感器共同获得的
                //加速度感应器
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                //地磁感应器
                defaultSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            }
        } catch (Exception e) {
            //表示传感器不可用
            e.printStackTrace();
        }
    }


    //使用加速度感应器 + 地磁感应器 实现方向传感器
    public static final int TYPE_NEW = 1;
    //使用过时的方向传感器
    public static final int TYPE_OLD = 2;

    @IntDef({TYPE_NEW, TYPE_OLD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SensorType {

    }


}
