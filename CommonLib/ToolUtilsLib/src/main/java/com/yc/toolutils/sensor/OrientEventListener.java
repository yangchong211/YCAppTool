package com.yc.toolutils.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

public class OrientEventListener implements SensorEventListener {

    //通过这个接口创建两个回调用法来接收传感器的事件通知，比如当传感器的值发生变化时。
    private final OrientCallBack orientCallBack;
    private double direction = 0;
    private float mLastAngle;
    private Context context;

    public void init(Context context) {
        this.context = context;
        // 根据当前上下文的屏幕方向调整与自然方向的相对关系。
        // 当设备的自然方向是竖直方向（比如，理论上说所有手机的自然方向都是都是是竖直方向），而应用是横屏时，
        // 需要将相对设备自然方向的方位角转换为相对水平方向的方位角。
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display dis = wm.getDefaultDisplay();
    }


    public OrientEventListener(OrientCallBack orientCallBack) {
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
        if (event == null || event.values==null || event.values.length==0){
            orientCallBack.Orient(direction);
            return;
        }
        if (Sensor.TYPE_ORIENTATION != event.sensor.getType()) {
            orientCallBack.Orient(direction);
            return;
        }
        // 根据当前上下文的屏幕方向调整与自然方向的相对关系。
        // 当设备的自然方向是竖直方向（比如，理论上说所有手机的自然方向都是都是是竖直方向，而有些平板的自然方向是水平方向），而应用是横屏时，
        // 需要将相对设备自然方向的方位角转换为相对水平方向的方位角。校对作用
        // SensorManager.remapCoordinateSystem(rotationMatrix, axis_x, axis_y, event.values);
        //values[0]：该值表示方位，也就是手机绕着Z轴旋转的角度
        //values[1]：该值表示倾斜度，或手机翘起的程度。
        //values[2]：表示手机沿Y轴的滚动角度，取值范围在-90~90之间。
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        if (x == 0.0F) {
            return;
        }
        //过滤一下子陡增，不然会跳动。这个30只是看日志观察效果推测出来
        if (Math.abs(this.mLastAngle - x) > 30.0F) {
            this.mLastAngle = x;
            return;
        }
        //然后取平均值
        x = (x + this.mLastAngle) / 2.0F;
        this.mLastAngle = x;
        boolean viewStat = false;
        try {
            //获取手机屏幕方向
            if (this.context.getResources().getConfiguration().orientation == 2) {
                viewStat = true;
            }
        } catch (Exception var10) {
            viewStat = false;
        }
        int o = (int) this.direction;
        if (viewStat) {
            //如果屏幕是横屏，则需要特殊处理一下
            if (o > 45 && o <= 135) {
                x = (x + 270.0F) % 360.0F;
            } else if (o > 135 && o <= 225) {
                x = (x + 180.0F) % 360.0F;
            } else if (o > 225 && o < 315) {
                x = (x + 90.0F) % 360.0F;
            }
        }
        double degree = x;
        if (Math.abs(degree - direction) > 1){
            orientCallBack.Orient(degree);
            direction = degree;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}

