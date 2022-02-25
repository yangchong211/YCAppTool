package com.yc.appstatuslib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 监听Home键按下的Wathcer
 */
public class HomeKeyWatcher {

    private Context mContext;
    private IntentFilter mFilter;
    private OnHomePressedListener mListener;
    private HomeReceiver mReceiver;

    public interface OnHomePressedListener {
        //短按Home键
        void onHomePressed();
        //长按Home键
        //void onHomeLongPressed();
    }

    public HomeKeyWatcher(Context context) {
        mContext = context;
        mFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
    }

    /**
     * 设置监听
     * @param listener
     */
    public void setOnHomePressedListener(OnHomePressedListener listener) {
        mListener = listener;
        mReceiver = new HomeReceiver();
    }

    /**
     * 开始监听，注册广播
     */
    public void startWatch() {
        if (mReceiver != null) {
            mContext.registerReceiver(mReceiver, mFilter);
        }
    }

    /**
     * 停止监听，注销广播
     */
    public void stopWatch() {
        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
        }
    }

    class HomeReceiver extends BroadcastReceiver {

        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action!=null && action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    if (mListener != null) {
                        if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                            // 短按home键
                            mListener.onHomePressed();
                        } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                            // 长按home键
                            //mListener.onHomeLongPressed();
                        }
                    }
                }
            }
        }
    }
}