package com.yc.window.draggable;

import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.yc.eventuploadlib.ExceptionReporter;
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
public abstract class AbsTouchListener implements View.OnTouchListener {

    private FloatWindow mFloatWindow;
    private View mDecorView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowParams;

    /**
     * 显示后回调这个类
     */
    public void start(FloatWindow floatWindow) {
        mFloatWindow = floatWindow;
        mDecorView = floatWindow.getDecorView();
        mWindowManager = floatWindow.getWindowManager();
        mWindowParams = floatWindow.getWindowParams();
        //给根布局设置触摸事件
        mDecorView.setOnTouchListener(this);
    }

    protected FloatWindow getFloatWindow() {
        return mFloatWindow;
    }

    protected WindowManager getWindowManager() {
        return mWindowManager;
    }

    protected WindowManager.LayoutParams getWindowParams() {
        return mWindowParams;
    }

    protected View getRootView() {
        return mDecorView;
    }

    /**
     * 获取当前 Window 的宽度
     */
    protected int getWindowWidth() {
        Rect rect = new Rect();
        getRootView().getWindowVisibleDisplayFrame(rect);
        return rect.right - rect.left;
    }

    /**
     * 获取当前 Window 的高度
     */
    protected int getWindowHeight() {
        Rect rect = new Rect();
        getRootView().getWindowVisibleDisplayFrame(rect);
        return rect.bottom - rect.top;
    }

    /**
     * 获取窗口不可见的宽度，一般情况下为横屏状态下刘海的高度
     */
    protected int getWindowInvisibleWidth() {
        Rect rect = new Rect();
        mDecorView.getWindowVisibleDisplayFrame(rect);
        return rect.left;
    }

    /**
     * 获取窗口不可见的高度，一般情况下为状态栏的高度
     */
    protected int getWindowInvisibleHeight() {
        Rect rect = new Rect();
        mDecorView.getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    /**
     * 更新悬浮窗的位置
     *
     * @param x x 坐标
     * @param y y 坐标
     */
    protected void updateLocation(float x, float y) {
        updateLocation((int) x, (int) y);
    }

    /**
     * 更新 WindowManager 所在的位置
     */
    protected void updateLocation(int x, int y) {
        // 屏幕默认的重心
        int screenGravity = Gravity.TOP | Gravity.START;
        // 判断本次移动的位置是否跟当前的窗口位置是否一致
        if (mWindowParams.gravity == screenGravity && mWindowParams.x == x && mWindowParams.y == y) {
            return;
        }

        mWindowParams.x = x;
        mWindowParams.y = y;
        // 一定要先设置重心位置为左上角
        mWindowParams.gravity = screenGravity;
        try {
            //更改位置
            mWindowManager.updateViewLayout(mDecorView, mWindowParams);
        } catch (IllegalArgumentException e) {
            // 当 WindowManager 已经消失时调用会发生崩溃
            // IllegalArgumentException: View not attached to window manager
            e.printStackTrace();
            ExceptionReporter.report("Float AbsTouchListener updateViewLayout", e);
        }
    }

    /**
     * 判断用户是否移动了，判断标准以下：
     * 根据手指按下和抬起时的坐标进行判断，不能根据有没有 move 事件来判断
     * 因为在有些机型上面，就算用户没有手指没有移动也会产生 move 事件
     *
     * @param downX 手指按下时的 x 坐标
     * @param upX   手指抬起时的 x 坐标
     * @param downY 手指按下时的 y 坐标
     * @param upY   手指抬起时的 y 坐标
     */
    protected boolean isTouchMove(float downX, float upX, float downY, float upY) {
        float minTouchSlop = getScaledTouchSlop();
        boolean isMove = Math.abs(downX - upX) >= minTouchSlop || Math.abs(downY - upY) >= minTouchSlop;
        // fix 如何解决滑出指定距离又滑入当作是点击事件bug？
        return isMove;
    }

    /**
     * 获取最小触摸距离
     */
    protected float getScaledTouchSlop() {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, displayMetrics);
    }
}