package com.yc.netreceiver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import com.yc.appcontextlib.AppToolUtils;

import java.util.ArrayList;
import java.util.List;

public final class NetWorkManager {

    private static final String TAG = "NetWork-Manager";
    private NetWorkReceiver mNetWorkReceiver;
    private final Context mContext;
    private final List<NetStatusListener> mNetStatusListener;

    private NetWorkManager() {
        mContext = AppToolUtils.getApp();
        mNetStatusListener = new ArrayList<>();
    }

    public static NetWorkManager getInstance() {
        return SingletonHelper.INSTANCE;
    }

    void changeNetStatus(int netType, boolean available) {
        for (NetStatusListener listener : mNetStatusListener) {
            listener.onChange(available, netType);
        }
    }

    private static class SingletonHelper {
        @SuppressLint("StaticFieldLeak")
        private final static NetWorkManager INSTANCE = new NetWorkManager();
    }

    public void registerReceiver() {
        if (mNetWorkReceiver == null){
            mNetWorkReceiver = new NetWorkReceiver(this);
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            mContext.registerReceiver(mNetWorkReceiver, filter);
            Log.d(TAG, "registerReceiver");
        }
    }

    public void registerNetStatusListener(NetStatusListener listener) {
        if (this.mNetStatusListener != null && !mNetStatusListener.contains(listener)) {
            this.mNetStatusListener.add(listener);
        }
    }

    public void destroy() {
        if (mNetStatusListener != null) {
            mNetStatusListener.clear();
        }
        unregisterReceiver();
    }

    public boolean unregisterNetStatusListener(NetStatusListener listener) {
        return mNetStatusListener != null && this.mNetStatusListener.remove(listener);
    }

    private void unregisterReceiver() {
        if (mNetWorkReceiver != null) {
            mContext.unregisterReceiver(mNetWorkReceiver);
            Log.d(TAG, "unregisterReceiver");
            mNetWorkReceiver = null;
        }
    }

}
