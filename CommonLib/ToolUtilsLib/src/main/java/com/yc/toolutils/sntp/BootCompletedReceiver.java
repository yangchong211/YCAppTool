package com.yc.toolutils.sntp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yc.toolutils.system.AppLogUtils;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/5/11
 *     desc   : 广播
 *     revise :
 * </pre>
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    private static final String TAG = BootCompletedReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        AppLogUtils.i(TAG, "clearing ntp time disk cache as we've detected a boot");
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            NtpGetTime.clearCachedInfo();
        }
    }
}
