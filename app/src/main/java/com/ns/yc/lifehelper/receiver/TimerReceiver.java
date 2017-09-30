package com.ns.yc.lifehelper.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/5/18
 * 描    述：注册广播，注意广播接收者中不要做耗时操作
 * 修订历史：
 * ================================================
 */
public class TimerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
    }

}
