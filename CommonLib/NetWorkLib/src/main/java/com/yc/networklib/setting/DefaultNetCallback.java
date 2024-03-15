package com.yc.networklib.setting;

import android.util.Log;

public class DefaultNetCallback implements OnNetCallback {
    @Override
    public void onChange(boolean connect, String netType) {
        Log.d("NetWork-onChange: ", netType + " , " + connect);
    }

    @Override
    public void onDefaultChange(boolean available, String netType) {
        Log.d("NetWork-onDefault: ", netType + " , " + available);
    }
}
