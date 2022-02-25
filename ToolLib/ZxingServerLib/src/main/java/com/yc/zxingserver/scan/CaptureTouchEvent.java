package com.yc.zxingserver.scan;

import android.view.MotionEvent;

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public interface CaptureTouchEvent {

    /**
     * {@link android.app.Activity#onTouchEvent(MotionEvent)}
     */
    boolean onTouchEvent(MotionEvent event);
}
