package com.yc.toolutils.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

public class OrientSensorListener implements SensorEventListener {

    //https://blog.csdn.net/wu371894545/article/details/53635991
    //https://blog.csdn.net/warren288/article/details/43274647
    private static final String TAG = "OrientSensorListener";
    //通过这个接口创建两个回调用法来接收传感器的事件通知，比如当传感器的值发生变化时。
    private final OrientCallBack orientCallBack;
    //加速度传感器数据
    float[] accelerometerValues = new float[3];
    //地磁传感器数据
    float[] magneticValues = new float[3];
    //旋转矩阵，用来保存磁场和加速度的数据
    //基于加速度计和磁力计当前读数的旋转矩阵。
    float[] rotationMatrix = new float[9];
    //将更新后的旋转矩阵表示为三个方向角。
    final float[] orientationAngles = new float[3];
    //方向
    private double direction = 0;
    private double mLastAngle;
    private int axis_x;
    private int axis_y;
    private Context context;

    public void init(Context context) {
        this.context = context;
        // 根据当前上下文的屏幕方向调整与自然方向的相对关系。
        // 当设备的自然方向是竖直方向（比如，理论上说所有手机的自然方向都是都是是竖直方向），而应用是横屏时，
        // 需要将相对设备自然方向的方位角转换为相对水平方向的方位角。
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display dis = wm.getDefaultDisplay();
        int rotation = dis.getRotation();
        switch (rotation) {
            case Surface.ROTATION_90:
                axis_x = SensorManager.AXIS_Y;
                axis_y = SensorManager.AXIS_MINUS_X;
                break;
            case Surface.ROTATION_180:
                axis_x = SensorManager.AXIS_X;
                axis_y = SensorManager.AXIS_MINUS_Y;
                break;
            case Surface.ROTATION_270:
                axis_x = SensorManager.AXIS_MINUS_Y;
                axis_y = SensorManager.AXIS_X;
                break;
            case Surface.ROTATION_0:
            default:
                axis_x = SensorManager.AXIS_X;
                axis_y = SensorManager.AXIS_Y;
                break;
        }
    }

    public OrientSensorListener(OrientCallBack orientCallBack) {
        this.orientCallBack = orientCallBack;
    }

    public interface OrientCallBack {
        /**
         * 方向回调
         */
        void Orient(double orient);
    }

    /**
     * 系统会通过这个类创建一个传感器事件对象，提供了一个传感器的事件信息。
     * 包含一下内容，原生的传感器数据、触发传感器的事件类型、精确的数据以及事件发生的时间。
     * @param event                         event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event == null){
            orientCallBack.Orient(direction);
            return;
        }
        //判断当前是加速度感应器还是地磁感应器
        //如果当前是加速度传感器，就将values数组赋给accelerometerValues数组，
        //如果当前是地磁传感器，就将values数组赋给magneticValues数组。
        //在赋值的时候一定要调用values的深拷贝，不然accelerometerValues和magneticValues将指向同一块引用。
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //赋值调用clone方法
            //accelerometerValues = event.values.clone();
            System.arraycopy(event.values, 0, accelerometerValues,
                    0, accelerometerValues.length);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            //赋值调用clone方法
            //magneticValues = event.values.clone();
            System.arraycopy(event.values, 0, magneticValues,
                    0, magneticValues.length);
        }
        calculateOrientation();
    }

    private void calculateOrientation() {
        /*
         * public static boolean getRotationMatrix (float[] R, float[] I, float[] gravity, float[] geomagnetic)
         * r: 要填充的旋转数组
         * I: 将磁场数据转换进实际的重力坐标中 一般默认情况下可以设置为null
         * gravity: 加速度传感器数据
         * geomagnetic: 地磁传感器数据
         */
        boolean isRotationMatrix = SensorManager.getRotationMatrix(rotationMatrix,
                null, accelerometerValues, magneticValues);
        //Log.i("旋转角度 rotationMatrix : " , isRotationMatrix+"");
        /*
         * public static float[] getOrientation (float[] R, float[] values)
         * R：旋转数组
         * values ：模拟方向传感器的数据
         */
        float[] orientation = SensorManager.getOrientation(rotationMatrix, orientationAngles);
        //values[0]：方向角，但用（磁场+加速度）得到的数据范围是（-180～180）,也就是说，0表示正北，90表示正东，180/-180表示正南，-90表示正西。而直接通过方向感应器数据范围是（0～359）360/0表示正北，90表示正东，180表示正南，270表示正西。
        //values[1]：pitch 倾斜角即由静止状态开始，前后翻转，手机顶部往上抬起（0~-90），手机尾部往上抬起（0~90）
        //values[2]：roll 旋转角 即由静止状态开始，左右翻转，手机左侧抬起（0~90）,手机右侧抬起（0~-90）
        // 根据当前上下文的屏幕方向调整与自然方向的相对关系。
        // 当设备的自然方向是竖直方向（比如，理论上说所有手机的自然方向都是都是是竖直方向，而有些平板的自然方向是水平方向），而应用是横屏时，
        // 需要将相对设备自然方向的
        // 方位角转换为相对水平方向的方位角。校对作用
        // SensorManager.remapCoordinateSystem(rotationMatrix, axis_x, axis_y, orientation);
        //旋转角度
        //orientation[0]的取值范围是-180到180度。
        float x = orientationAngles[0];
        double degree = (double) Math.toDegrees(x);
        if (degree == 0.0F) {
            return;
        }
        if (degree < 0) {
            degree += 360;
        }
        //过滤一下子陡增，不然会跳动。这个30只是看日志观察效果推测出来
        if (Math.abs(this.mLastAngle - degree) > 30.0F) {
            this.mLastAngle = degree;
            return;
        }
        //然后取平均值
        degree = (degree + this.mLastAngle) / 2.0F;
        this.mLastAngle = degree;
        boolean viewStat = false;
        try {
            //获取手机屏幕方向
            if (context.getResources().getConfiguration().orientation == 2) {
                viewStat = true;
            }
        } catch (Exception var10) {
            viewStat = false;
        }
        int o = (int) this.direction;
        if (viewStat) {
            //如果屏幕是横屏，则需要特殊处理一下
            if (o > 45 && o <= 135) {
                degree = (degree + 270.0F) % 360.0F;
            } else if (o > 135 && o <= 225) {
                degree = (degree + 180.0F) % 360.0F;
            } else if (o > 225 && o < 315) {
                degree = (degree + 90.0F) % 360.0F;
            }
        }
        //Log.i("旋转角度: " , degree+"");
        if (Math.abs(degree - direction) > 5){
            //避免频繁调用
            orientCallBack.Orient(degree);
            direction = degree;
            //Log.i("旋转角度坐标点: " , degree+"");
        }
    }

    /**
     * 当已注册传感器的精度发生变化时调用。
     * @param sensor                            sensor
     * @param accuracy                          精度
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

