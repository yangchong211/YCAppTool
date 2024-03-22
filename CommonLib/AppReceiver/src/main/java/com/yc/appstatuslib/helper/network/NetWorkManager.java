package com.yc.appstatuslib.helper.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import com.yc.toolutils.context.AppToolUtils;

import java.util.ArrayList;
import java.util.List;

public final class NetWorkManager implements IStatusListener {

    private static final String TAG = "NetWork-Manager";
    private NetWorkReceiver mNetWorkReceiver;
    private final Context mContext;
    private final List<OnNetStatusListener> mNetStatusListener;

    private NetWorkManager() {
        mContext = AppToolUtils.getApp();
        mNetStatusListener = new ArrayList<>();
        registerReceiver();
    }

    public static NetWorkManager getInstance() {
        return SingletonHelper.INSTANCE;
    }

    void changeNetStatus(int netType, boolean available) {
        for (OnNetStatusListener listener : mNetStatusListener) {
            listener.onChange(available, netType);
        }
    }

    private static class SingletonHelper {
        @SuppressLint("StaticFieldLeak")
        private static final  NetWorkManager INSTANCE = new NetWorkManager();
    }

    /**
     * 注册网络监听广播
     * 当网络环境切换的时候，就会触发广播的回调
     */
    private void registerReceiver() {
        if (mNetWorkReceiver == null) {
            mNetWorkReceiver = new NetWorkReceiver(this);
            IntentFilter filter = new IntentFilter();
            //android.net.conn.CONNECTIVITY_CHANGE
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            mContext.registerReceiver(mNetWorkReceiver, filter);
            Log.d(TAG, "registerReceiver");
        }
    }

    /**
     * 解绑网络监听广播
     */
    private void unregisterReceiver() {
        if (mNetWorkReceiver != null) {
            mContext.unregisterReceiver(mNetWorkReceiver);
            Log.d(TAG, "unregisterReceiver");
            mNetWorkReceiver = null;
        }
    }

    @Override
    public void registerNetStatusListener(OnNetStatusListener listener) {
        if (this.mNetStatusListener != null && !mNetStatusListener.contains(listener)) {
            this.mNetStatusListener.add(listener);
        }
    }

    @Override
    public void destroy() {
        if (mNetStatusListener != null) {
            mNetStatusListener.clear();
        }
        unregisterReceiver();
    }

    @Override
    public boolean unregisterNetStatusListener(OnNetStatusListener listener) {
        return mNetStatusListener != null && this.mNetStatusListener.remove(listener);
    }

}
