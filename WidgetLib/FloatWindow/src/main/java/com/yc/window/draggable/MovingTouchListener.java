package com.yc.window.draggable;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/08/18
 *     desc  : 拖拽移动处理实现类
 *     revise:
 * </pre>
 */
public class MovingTouchListener extends AbsTouchListener {

    /** 手指按下的坐标 */
    private float mViewDownX;
    private float mViewDownY;

    /** 触摸移动标记 */
    private boolean mMoveTouch;

    /**
     * 触摸事件。返回false表示不会拦截事件，返回true表示自己处理事件相当于事件拦截了(避免点击事件)
     * @param v                 view
     * @param event             event
     * @return
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录按下的位置（相对 View 的坐标）
                mViewDownX = event.getX();
                mViewDownY = event.getY();
                mMoveTouch = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // 记录移动的位置（相对屏幕的坐标）
                float rawMoveX = event.getRawX() - getWindowInvisibleWidth();
                float rawMoveY = event.getRawY() - getWindowInvisibleHeight();

                // 更新移动的位置
                updateLocation(rawMoveX - mViewDownX, rawMoveY - mViewDownY);

                if (!mMoveTouch && isTouchMove(mViewDownX, event.getX(), mViewDownY, event.getY())) {
                    // 如果用户移动了手指，那么就拦截本次触摸事件，从而不让点击事件生效
                    mMoveTouch = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                return mMoveTouch;
            default:
                break;
        }
        return false;
    }
}