package com.yc.widgetbusiness.event;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class EventTextView extends AppCompatTextView {

    public EventTextView(Context context) {
        this(context , null);
    }

    public EventTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public EventTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("touch_event_test", "View dispatchTouchEvent return super");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("touch_event_test", "View onTouchEvent return super");
        return super.onTouchEvent(event);
    }

}
