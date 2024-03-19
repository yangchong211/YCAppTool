package com.yc.networklib.setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.annotation.StringDef;

import com.yc.toolutils.context.AppToolUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public final class NetRequestHelper implements INetRequest {

    private static final String TAG = "NetWork-Helper";
    private List<OnNetCallback> mNetStatusListener;
    private boolean isRegisterNetworkCallback = false;
    private boolean isRegisterDefaultNetworkCallback = false;
    public static final String UNKNOWN = "UNKNOWN";
    public static final String MOBILE = "MOBILE";
    public static final String ETHERNET = "ETHERNET";
    public static final String WIFI = "WIFI";

    @StringDef({UNKNOWN, MOBILE, ETHERNET, WIFI})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NetworkType {
    }

    /**
     * 监听网路变化监听器
     */
    private final ChangeNetworkCallback networkCallback = new ChangeNetworkCallback(this);
    /**
     * 监听默认网络回调
     * 举个例子：
     * 1.当Wi-Fi和移动流量共存的时候，默认流量是Wi-Fi；会执行 onAvailable 网络类型：WIFI , WIFI
     * 2.当Wi-Fi关闭的时候，默认流量就会切换到移动流量；会执行 onAvailable 网络类型：MOBILE , MOBILE
     * 3.当Wi-Fi再次打开的时候，默认流量就会切换到Wi-Fi；会执行 onAvailable 网络类型：WIFI , WIFI
     */
    private final DefaultNetworkCallback defaultNetworkCallback = new DefaultNetworkCallback(this);
    /**
     * 监听设置默认网络回调
     */
    private final RequestNetworkCallback requestNetworkCallback = new RequestNetworkCallback(this);
    /**
     * 线程池，主要是需要开启子线程去设置绑定默认网络
     */
    private final ScheduledExecutorService executorService =
            Executors.newSingleThreadScheduledExecutor();

    public static NetRequestHelper getInstance() {
        return NetRequestHelper.SingletonHelper.INSTANCE;
    }

    private static class SingletonHelper {
        @SuppressLint("StaticFieldLeak")
        private static final NetRequestHelper INSTANCE = new NetRequestHelper();
    }

    private NetRequestHelper() {
        mNetStatusListener = new ArrayList<>();
    }

    void changeNetStatus(String netType, boolean available) {
        for (OnNetCallback listener : mNetStatusListener) {
            listener.onChange(available, netType);
        }
    }

    void changeDefaultNetworkStatus(String netType, boolean available) {
        for (OnNetCallback listener : mNetStatusListener) {
            listener.onDefaultChange(available, netType);
        }
    }

    /**
     * 注册网络变化监听
     */
    @Override
    public void registerNetworkCallback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //获取ConnectivityManager
            final ConnectivityManager connMgr = (ConnectivityManager)
                    AppToolUtils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
            //创建NetworkRequest对象，定制化监听
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            // NetworkCapabilities.NET_CAPABILITY_INTERNET 表示此网络应该能够连接到Internet
            builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            //NetworkRequest用来定制化监听，只有符合约束条件的网络变化才会回调NetworkCallback中的方法。
            builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);
            //addTransportType()用来添加网络类型约束，上面的代码表示只监听移动网络。
            //多个TransportType之间是“或”的关系，即满足其中之一即可。
            builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
            builder.addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET);
            NetworkRequest request = builder.build();
            if (connMgr != null) {
                //创建网路变化监听器
                connMgr.registerNetworkCallback(request, networkCallback);
                isRegisterNetworkCallback = true;
                Log.d(TAG, "registerNetworkCallback");
            }
        }
    }

    /**
     * 注册默认网络回调
     */
    @Override
    public void registerDefaultNetworkCallback() {
        ConnectivityManager connMgr = (ConnectivityManager)
                AppToolUtils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            //创建网路变化监听器
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //当新网络成为默认网络时，应用打开的任何新连接都会使用此网络。
                //一段时间后，上一个默认网络上的所有剩余连接都将被强制终止。
                //如果知道默认网络发生变化的时间对应用很重要，它会按如下方式注册默认网络回调
                connMgr.registerDefaultNetworkCallback(defaultNetworkCallback);
                Log.d(TAG, "registerDefaultNetworkCallback");
                //对于通过 registerDefaultNetworkCallback() 注册的回调
                //新网络成为默认网络后，应用会收到对新网络的 onAvailable(Network) 的调用。
                //onLost() 表示网络失去成为默认网络的资格。它可能会断开连接。
            }
        }
    }

    /**
     * 解绑注册监听
     */
    @Override
    public void unregisterNetworkCallback() {
        ConnectivityManager connMgr = (ConnectivityManager)
                AppToolUtils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null && isRegisterNetworkCallback) {
            //什么时候终止监听？只有程序进程退出(注意是进程退出)或解除注册时终止监听。
            connMgr.unregisterNetworkCallback(networkCallback);
            isRegisterNetworkCallback = false;
            //您的主 activity 的 onPause() 非常适合执行这项操作，尤其是在 onResume() 中注册回调时。
            Log.d(TAG, "unregisterNetworkCallback networkCallback");
        }
        if (connMgr != null && isRegisterDefaultNetworkCallback) {
            connMgr.unregisterNetworkCallback(defaultNetworkCallback);
            isRegisterDefaultNetworkCallback = false;
            //您的主 activity 的 onPause() 非常适合执行这项操作，尤其是在 onResume() 中注册回调时。
            Log.d(TAG, "unregisterNetworkCallback defaultNetworkCallback");
        }
    }

    @Override
    public void registerNetStatusListener(OnNetCallback listener) {
        if (this.mNetStatusListener != null && !mNetStatusListener.contains(listener)) {
            this.mNetStatusListener.add(listener);
        }
    }

    @Override
    public boolean unregisterNetStatusListener(OnNetCallback listener) {
        return mNetStatusListener != null && this.mNetStatusListener.remove(listener);
    }

    /**
     * 指定某个请求采用指定的网络进行发送
     *
     * @param type 类型
     */
    @Override
    public void requestNetwork(@NetRequestHelper.NetworkType String type) {
        final ConnectivityManager connectivityManager = (ConnectivityManager)
                AppToolUtils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        // NetworkCapabilities.NET_CAPABILITY_INTERNET 表示此网络应该能够连接到Internet
        builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        if (MOBILE.equals(type)) {
            // 设置指定的网络传输类型(蜂窝传输) 等于手机网络
            builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);
            Log.i(TAG, "addTransportType , 移动网络");
        } else if (WIFI.equals(type)) {
            // 设置指定的网络传输类型是Wi-Fi
            builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
            Log.i(TAG, "addTransportType , Wi-Fi");
        } else if (ETHERNET.equals(type)) {
            // 设置指定的网络传输类型是以太网
            builder.addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET);
            Log.i(TAG, "addTransportType , 以太网");
        }
        // 设置感兴趣的网络功能
        //builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        // 设置感兴趣的网络：计费网络
        //builder.addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED);
        NetworkRequest networkRequest = builder.build();
        //使用 NetworkCallback 以及其他了解设备连接状态的方式不需要任何特定权限。
        //但是，使用某些网络可能需要特定权限。例如，可能存在应用无法使用的受限网络。
        AtomicInteger count = new AtomicInteger();
        executorService.scheduleWithFixedDelay(() -> {
            try {
                if (!CapabilitiesUtils.isConnected()) {
                    Log.i(TAG, "scheduleWithFixedDelay , 网络未连接");
                    return;
                }
                if (requestNetworkCallback.isBindNetwork()) {
                    Log.i(TAG, "scheduleWithFixedDelay , 没有绑定成功 " + count.getAndIncrement());
                    return;
                }
                //如果您想在检测到合适的网络时主动切换到该网络，请使用 requestNetwork() 方法；
                connectivityManager.requestNetwork(networkRequest, requestNetworkCallback);
                //如果只是接收已扫描网络的通知而不需要主动切换，请改用 registerNetworkCallback() 方法。
                //connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 1, 5, TimeUnit.SECONDS);
    }

    @Override
    public void destroy() {
        Log.d(TAG, "destroy");
        if (mNetStatusListener != null) {
            mNetStatusListener.clear();
            mNetStatusListener = null;
        }
        unregisterNetworkCallback();
    }

}
