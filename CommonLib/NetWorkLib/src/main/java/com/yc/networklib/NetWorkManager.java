package com.yc.networklib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.yc.appcontextlib.AppToolUtils;

import java.util.ArrayList;
import java.util.List;

public final class NetWorkManager {

    private static final String TAG = "NetWork-Manager";
    private final NetWorkReceiver mNetWorkReceiver;
    private final Context mContext;
    private final List<NetStatusListener> mNetStatusListener;

    private NetWorkManager() {
        mContext = AppToolUtils.getApp();
        mNetWorkReceiver = new NetWorkReceiver(this);
        mNetStatusListener = new ArrayList<>();
        registerReceiver();
    }

    public static NetWorkManager getInstance() {
        return SingletonHelper.INSTANCE;
    }

    protected void changeNetStatus(int netType, boolean available) {
        for (NetStatusListener listener : mNetStatusListener) {
            listener.onChange(available, netType);
        }
    }

    private static class SingletonHelper {
        @SuppressLint("StaticFieldLeak")
        private final static NetWorkManager INSTANCE = new NetWorkManager();
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(mNetWorkReceiver, filter);
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
        }
    }

    public interface NetStatusListener {
        void onChange(boolean connect, int netType);
    }
}
