package com.yc.widgetbusiness.event;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class EventLinearLayout extends LinearLayout {

    public EventLinearLayout(Context context) {
        super(context);
    }

    public EventLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EventLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("touch_event_test", "ViewGroup1 dispatchTouchEvent return super");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i("touch_event_test", "ViewGroup1 onInterceptTouchEvent return super");
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("touch_event_test", "ViewGroup1 onTouchEvent return super");
        return super.onTouchEvent(event);
    }


}
