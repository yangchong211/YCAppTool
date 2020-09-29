package com.yc.zxingserver.scan;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public interface CaptureLifecycle {

    /**
     * {@link Activity#onCreate(Bundle)}
     */
    void onCreate();
    /**
     * {@link Activity#onResume()}
     */
    void onResume();

    /**
     * {@link Activity#onPause()}
     */
    void onPause();

    /**
     * {@link Activity#onDestroy()}
     */
    void onDestroy();

}
