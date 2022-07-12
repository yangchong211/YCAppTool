package com.yc.ntptime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yc.toolutils.AppLogUtils;

public class BootCompletedReceiver extends BroadcastReceiver {

    private static final String TAG = BootCompletedReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        AppLogUtils.i(TAG, "---- clearing TrueTime disk cache as we've detected a boot");
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            NtpGetTime.clearCachedInfo();
        }
    }
}
