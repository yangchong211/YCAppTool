package com.yc.widgetbusiness.event;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

public class EventRelativeLayout extends RelativeLayout {

    public EventRelativeLayout(Context context) {
        super(context);
    }

    public EventRelativeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EventRelativeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("touch_event_test", "ViewGroup2 dispatchTouchEvent return super");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i("touch_event_test", "ViewGroup2 onInterceptTouchEvent return super");
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("touch_event_test", "ViewGroup2 onTouchEvent return super");
        return super.onTouchEvent(event);
    }

}
