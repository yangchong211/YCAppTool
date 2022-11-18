package com.yc.window.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.yc.eventuploadlib.ExceptionReporter;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/10/21
 *     desc  : window布局
 *     revise:
 * </pre>
 */
public final class WindowLayout extends FrameLayout {

    /** 触摸事件监听 */
    private OnTouchListener mOnTouchListener;

    public WindowLayout(Context context) {
        super(context);
    }

    public WindowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WindowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 为什么要那么写？有人反馈给子 View 设置 OnClickListener 后，父 View 的 OnTouchListener 收不到事件
        // 经过排查发现：父 View 在 dispatchTouchEvent 方法中直接将触摸事件派发给了子 View 的 onTouchEvent 方法
        // 从而导致父 View.OnTouchListener 收不到该事件
        // 解决方案是重写 View 的触摸规则，让父 View 的触摸监听优于子 View 的点击事件
        if (mOnTouchListener != null && mOnTouchListener.onTouch(this, ev)) {
            return true;
        }
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e){
            e.printStackTrace();
            ExceptionReporter.report("Float WindowLayout DispatchTouchEvent", e);
        }
        return true;
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        //super.setOnTouchListener(l);
        mOnTouchListener = l;
    }
}