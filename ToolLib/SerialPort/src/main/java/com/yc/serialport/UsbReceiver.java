package com.yc.serialport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yc.toolutils.AppLogUtils;

public class UsbReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_MEDIA_MOUNTED.equals(intent.getAction())){
            //android.intent.action.MEDIA_MOUNTED
            AppLogUtils.d( "U盘插入");
        } else if(Intent.ACTION_MEDIA_REMOVED.equals(intent.getAction())){
            //android.intent.action.MEDIA_REMOVED
            AppLogUtils.d( "无介质");
        } else if (Intent.ACTION_MEDIA_UNMOUNTED.equals(intent.getAction())) {
            //android.intent.action.MEDIA_UNMOUNTED
            AppLogUtils.d( "U盘移除");
        }
    }


}
