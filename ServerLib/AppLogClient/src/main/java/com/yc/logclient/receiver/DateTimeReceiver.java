package com.yc.logclient.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yc.logclient.LogUtils;
import com.yc.logclient.bean.AppLogBean;

import java.util.TimeZone;

import static android.content.Intent.ACTION_TIMEZONE_CHANGED;

public class DateTimeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (ACTION_TIMEZONE_CHANGED.equals(action)) {
            LogUtils.d("DateTimeReceiver - ACTION_TIMEZONE_CHANGED:" + TimeZone.getDefault().toString());
            AppLogBean.S_D_FORMAT.setTimeZone(TimeZone.getDefault());
        }
    }
}
