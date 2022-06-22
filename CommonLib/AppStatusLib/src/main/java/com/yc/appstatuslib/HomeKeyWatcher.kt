package com.yc.appstatuslib

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

/**
 * @author yangchong
 * email  : yangchong211@163.com
 * time   : 2017/5/18
 * desc   : 监听Home键按下的Wathcer
 * revise :
 */
class HomeKeyWatcher(private val mContext: Context) {

    private val mFilter: IntentFilter = IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
    private var mListener: OnHomePressedListener? = null
    private var mReceiver: HomeReceiver? = null

    interface OnHomePressedListener {
        //短按Home键
        fun onHomePressed()

        //长按Home键
        fun onHomeLongPressed()
    }

    /**
     * 设置监听
     * @param listener
     */
    fun setOnHomePressedListener(listener: OnHomePressedListener?) {
        mListener = listener
        mReceiver = HomeReceiver()
    }

    /**
     * 开始监听，注册广播
     */
    fun startWatch() {
        if (mReceiver != null) {
            mContext.registerReceiver(mReceiver, mFilter)
        }
    }

    /**
     * 停止监听，注销广播
     */
    fun stopWatch() {
        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver)
        }
    }

    internal inner class HomeReceiver : BroadcastReceiver() {
        private val SYSTEM_DIALOG_REASON_KEY = "reason"
        private val SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps"
        private val SYSTEM_DIALOG_REASON_HOME_KEY = "homekey"

        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action != null && action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {
                val reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY)
                if (reason != null) {
                    if (mListener != null) {
                        if (reason == SYSTEM_DIALOG_REASON_HOME_KEY) {
                            // 短按home键
                            mListener?.onHomePressed()
                        } else if (reason == SYSTEM_DIALOG_REASON_RECENT_APPS) {
                            // 长按home键
                            mListener?.onHomeLongPressed()
                        }
                    }
                }
            }
        }
    }

}