package com.yc.appstatuslib.helper.phone;

import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time  : 2018/11/9
 *     desc  : 电话状态监听
 *     revise:
 * </pre>
 */
public class MyPhoneListener extends PhoneStateListener {

    private static final String TAG = "PhoneStateListener";
    private final OnPhoneListener onPhoneListener;

    public MyPhoneListener(OnPhoneListener onPhoneListener) {
        this.onPhoneListener = onPhoneListener;
    }

    @Override
    public void onServiceStateChanged(ServiceState serviceState) {
        super.onServiceStateChanged(serviceState);
        Log.d(TAG, "onServiceStateChanged : " + serviceState);
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        //int CALL_STATE_IDLE 空闲状态，没有任何活动。
        //int CALL_STATE_OFFHOOK 摘机状态，至少有个电话活动。该活动或是拨打（dialing）或是通话，或是 on hold。并且没有电话是ringing or waiting
        //int CALL_STATE_RINGING 来电状态，电话铃声响起的那段时间或正在通话又来新电，新来电话不得不等待的那段时间。
        if (onPhoneListener == null){
            return;
        }
        //手机通话状态在广播中的对应值
        //EXTRA_STATE_IDLE 它在手机通话状态改变的广播中，用于表示CALL_STATE_IDLE状态
        //EXTRA_STATE_OFFHOOK 它在手机通话状态改变的广播中，用于表示CALL_STATE_OFFHOOK状态
        //EXTRA_STATE_RINGING 它在手机通话状态改变的广播中，用于表示CALL_STATE_RINGING状态
        //ACTION_PHONE_STATE_CHANGED 在广播中用ACTION_PHONE_STATE_CHANGED这个Action来标示通话状态改变的广播（intent）。
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                Log.d(TAG, "onCallStateChanged: 挂断");
                onPhoneListener.callIdle();
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.d(TAG, "onCallStateChanged: 来电接听");
                onPhoneListener.callOffHook();
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                Log.d(TAG, "onCallStateChanged: 来电响铃 " + incomingNumber);
                onPhoneListener.callRunning();
                break;
            default:
                break;
        }
    }

}
