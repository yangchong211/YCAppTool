package com.yc.window.draggable;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.yc.window.FloatWindow;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/10/21
 *     desc  : 抽象触摸事件监听
 *     revise:
 * </pre>
 */
@Deprecated
public class FloatingOnTouchListener implements View.OnTouchListener {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowParams;
    private int x;
    private int y;

    /**
     * 显示后回调这个类
     */
    public void start(FloatWindow floatWindow) {
        mWindowManager = floatWindow.getWindowManager();
        mWindowParams = floatWindow.getWindowParams();
        //给根布局设置触摸事件
        floatWindow.getDecorView().setOnTouchListener(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getRawX();
                y = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int nowX = (int) event.getRawX();
                int nowY = (int) event.getRawY();
                int movedX = nowX - x;
                int movedY = nowY - y;
                x = nowX;
                y = nowY;
                mWindowParams.x = mWindowParams.x + movedX;
                mWindowParams.y = mWindowParams.y + movedY;
                // 更新悬浮窗控件布局
                mWindowManager.updateViewLayout(view, mWindowParams);
                break;
            default:
                break;
        }
        return false;
    }
}
