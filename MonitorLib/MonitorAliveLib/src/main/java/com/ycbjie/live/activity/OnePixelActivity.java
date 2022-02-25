package com.ycbjie.live.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/19
 *     desc  : 一像素Activity
 *     revise: 用final修饰，避免被继承
 * </pre>
 */
public final class OnePixelActivity extends Activity {

    /**
     * 注意要点：
     * 1.启动模式
     * 启动模式要设置成singleInstance,不然会有问题,
     * 当你的app在后台的时候,你解锁屏幕,创建销毁OnePixelActivity的同时会把整个栈的activity弹出来,
     * 然会用户就会莫名其妙的看到你的app自己打开
     *
     * 2.theme主题
     * 一定要把主题设置为透明,不然在你操作飞速的时候,屏幕总是会闪过一抹五彩斑斓的黑
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设定一像素的activity
        Window window = getWindow();
        //放在左上角
        window.setGravity(Gravity.START | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        //起始坐标
        params.x = 0;
        params.y = 0;
        //宽高设计为1个像素
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkScreenOn();
    }

    /**
     * 检测屏幕是否
     */
    private void checkScreenOn() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //如果设备处于交互状态，则返回true。
        boolean isScreenOn;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
            //20以上，建议用isInteractive方法
            isScreenOn = pm != null && pm.isInteractive();
        } else {
            isScreenOn = pm != null && pm.isScreenOn();
        }
        if (isScreenOn) {
            finish();
        }
    }
}
