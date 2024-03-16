package com.yc.appstatuslib.helper.phone;

import android.content.Context;
import android.content.IntentFilter;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import static android.content.Intent.ACTION_NEW_OUTGOING_CALL;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time  : 2018/11/9
 *     desc  : 电话状态监听管理者
 *     revise:
 * </pre>
 */
public final class PhoneManager {

    private static volatile PhoneManager INSTANCE;
    private OnPhoneListener onPhoneListener;
    private MyPhoneReceiver phoneReceiver;

    public static PhoneManager getInstance() {
        if (INSTANCE == null) {
            synchronized (PhoneManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PhoneManager();
                }
            }
        }
        return INSTANCE;
    }

    public void setOnPhoneListener(OnPhoneListener onPhoneListener) {
        this.onPhoneListener = onPhoneListener;
    }

    public void registerPhoneStateListener(Context context) {
        //如果我们想要监听电话的拨打状况，需要这么几步 :
        //* 第一：获取电话服务管理器TelephonyManager manager = this.getSystemService(TELEPHONY_SERVICE);
        //* 第二：通过TelephonyManager注册我们要监听的电话状态改变事件。manager.listen(new MyPhoneStateListener(),
        //* PhoneStateListener.LISTEN_CALL_STATE);这里的PhoneStateListener.LISTEN_CALL_STATE就是我们想要
        //* 监听的状态改变事件，初次之外，还有很多其他事件哦。
        //* 第三步：通过extends PhoneStateListener来定制自己的规则。将其对象传递给第二步作为参数。
        if (onPhoneListener == null){
            throw new NullPointerException("please set phone state listener at first");
        }
        MyPhoneListener customPhoneStateListener = new MyPhoneListener(onPhoneListener);
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            telephonyManager.listen(customPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    public void registerReceiver(Context context){
        if (phoneReceiver == null){
            phoneReceiver = new MyPhoneReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_NEW_OUTGOING_CALL);
            context.registerReceiver(phoneReceiver, filter);
        }
    }

    public void unRegisterReceiver(Context context){
        if (phoneReceiver != null){
            context.unregisterReceiver(phoneReceiver);
            phoneReceiver = null;
        }
    }

}
