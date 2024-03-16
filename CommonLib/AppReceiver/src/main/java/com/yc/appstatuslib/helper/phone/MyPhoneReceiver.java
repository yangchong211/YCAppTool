package com.yc.appstatuslib.helper.phone;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time  : 2018/11/9
 *     desc  : 电话来电和去电监听广播
 *     revise:
 * </pre>
 */
public class MyPhoneReceiver extends BroadcastReceiver {

    private static final String TAG = "PhoneReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            // 如果是拨打电话
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.d(TAG, "call out:" + phoneNumber);
        } else {
            //查了下android文档，貌似没有专门用于接收来电的action，所以，非去电即来电
            // 如果是来电
            TelephonyManager tManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            // 来电号码
            String mIncomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.i(TAG, "call in 1:" + state);
            Log.i(TAG, "call in 2:" + mIncomingNumber);
            int callState = tManager.getCallState();
            switch (callState) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d(TAG, "onCallStateChanged: 挂断");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d(TAG, "onCallStateChanged: 来电接听");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d(TAG, "onCallStateChanged: 来电响铃 ");
                    break;
                default:
                    break;
            }
        }
    }

}
