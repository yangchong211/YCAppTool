package com.yc.location.listener;

import com.yc.location.bean.DefaultLocation;
import com.yc.location.bean.ErrorInfo;

public abstract class AbsBaseLocationListener implements LocationListener {

    public AbsBaseLocationListener() {

    }

    public void onLocationChanged(DefaultLocation didiLocation) {

    }

    public void onLocationError(int errNo, ErrorInfo errInfo) {

    }

    public void onStatusUpdate(String name, int status, String desc) {

    }
}
