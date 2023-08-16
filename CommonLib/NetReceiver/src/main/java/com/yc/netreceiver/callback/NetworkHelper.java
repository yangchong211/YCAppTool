package com.yc.netreceiver.callback;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.yc.appcontextlib.AppToolUtils;
import com.yc.netreceiver.IStatusListener;
import com.yc.netreceiver.OnNetStatusListener;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public final class NetworkHelper implements IStatusListener {

    private final Context mContext;
    private final List<OnNetStatusListener> mNetStatusListener;
    private final NetworkCallbackImpl networkCallback = new NetworkCallbackImpl(this);


    public static NetworkHelper getInstance() {
        return NetworkHelper.SingletonHelper.INSTANCE;
    }

    private static class SingletonHelper {
        @SuppressLint("StaticFieldLeak")
        private static final  NetworkHelper INSTANCE = new NetworkHelper();
    }

    private NetworkHelper(){
        mContext = AppToolUtils.getApp();
        mNetStatusListener = new ArrayList<>();
        registerNetworkCallback();
    }

    void changeNetStatus(int netType, boolean available) {
        for (OnNetStatusListener listener : mNetStatusListener) {
            listener.onChange(available, netType);
        }
    }

    private void registerNetworkCallback() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            NetworkRequest request = builder.build();
            ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connMgr != null) {
                connMgr.registerNetworkCallback(request, networkCallback);
            }
        }
    }

    private void unregisterNetworkCallback() {
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            connMgr.unregisterNetworkCallback(networkCallback);
        }
    }

    @Override
    public void registerNetStatusListener(OnNetStatusListener listener) {
        if (this.mNetStatusListener != null && !mNetStatusListener.contains(listener)) {
            this.mNetStatusListener.add(listener);
        }
    }

    @Override
    public boolean unregisterNetStatusListener(OnNetStatusListener listener) {
        return mNetStatusListener != null && this.mNetStatusListener.remove(listener);
    }

    @Override
    public void destroy() {
        if (mNetStatusListener != null) {
            mNetStatusListener.clear();
        }
        unregisterNetworkCallback();
    }

}
