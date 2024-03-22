package com.yc.socket.websocket;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;

/**
 * WebSocket 管理类
 * <p>
 * Created by yangchong on 2019/3/21.
 */
public class WebSocketClientManager implements IWebSocketClient {
    private IWebSocketClient mWebSocketClient;
    private WebSocketSetting mWebSocketSetting;
    private Context mContext;

    private WebSocketClientManager() {
    }

    public static WebSocketClientManager getInstance() {
        return WebSocketHandlerWraper.webSocketClientManager;
    }

    public void init(Context context) {
        this.mContext = context;
    }


    @Override
    public void connect(WebSocketSetting webSocketSetting) {

        // 初始化重连机制
        ReconnectManager.getInstance().init(mContext);

        if (mWebSocketClient != null) {
            mWebSocketClient.connect(webSocketSetting);
        }
        mWebSocketClient = new OkhttpWebSocketClient();
        mWebSocketSetting = webSocketSetting;
        mWebSocketClient.connect(mWebSocketSetting);
        // 网络状态变化重新连接机制
        if (webSocketSetting.reconnectWithNetworkChanged()) {
            registerNetworkChangedReceiver(mContext);
        }
    }

    public WebSocketSetting getWebSocketSetting() {
        return mWebSocketSetting;
    }

    @Override
    public void reconnect() {

        // 不需要重连就不重连， 如用户手动断开的
        if (mWebSocketClient != null
                && mWebSocketClient.getConnectState() != CONNECTED
                && mWebSocketClient.getConnectState() != CONNECTING) {
            WebSocketLog.i("WebSocketClientManager", "重连 currentstate: " + mWebSocketClient.getConnectState());
            mWebSocketClient.reconnect();
        } else {
            if (mWebSocketClient != null) {
                WebSocketLog.i("WebSocketClientManager", "不需要重连 currentstate: " + mWebSocketClient.getConnectState());
            } else {
                WebSocketLog.i("WebSocketClientManager", "socket客户端未初始化");
            }
        }
    }

    @Override
    public boolean send(String text) {
        if (mWebSocketClient != null) {
            boolean send = mWebSocketClient.send(text);
            WebSocketLog.i("WebSocketClientManager", "发送消息:  " + text + "  失败");
            return send;
        } else {
            WebSocketLog.i("WebSocketClientManager", "socket未连接");
        }
        return false;
    }

    @Override
    public boolean send(byte[] bytes) {
        if (mWebSocketClient != null) {
            boolean send = mWebSocketClient.send(bytes);
            WebSocketLog.i("WebSocketClientManager", "发送消息:  bytes.length: " + bytes.length + "  失败");
            return send;
        }
        return false;
    }

    @Override
    public boolean disConnect() {
        if (mWebSocketClient != null) {
            return mWebSocketClient.disConnect();
        }
        return true;
    }

    @Override
    public int getConnectState() {
        if (mWebSocketClient != null) {
            return mWebSocketClient.getConnectState();
        }
        return UNCONNECTED;
    }

    @Override
    public boolean needAutoRetryConnect() {
        if (mWebSocketClient != null) {
            boolean needRetry = mWebSocketClient.needAutoRetryConnect();
            WebSocketLog.i("WebSocketClientManager", "是否需要自动重连: " + needRetry);
            return needRetry;
        } else {
            WebSocketLog.i("mWebSocketClient = null ", "不需要重连");
        }
        return false;
    }


    /**
     * 注册网络变化监听广播，网络由不可用变为可用时会重新连接 WebSocket
     *
     * @param context 此处应该使用 ApplicationContext，避免内存泄漏以及其它异常。
     */
    private void registerNetworkChangedReceiver(Context context) {
        if (checkPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)) {
            try {
                IntentFilter filter = new IntentFilter();
                filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                context.registerReceiver(new NetworkChangedReceiver(), filter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断是否有权限
     */
    private boolean checkPermission(Context context,
                                    String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return PackageManager.PERMISSION_GRANTED == context.getPackageManager()
                    .checkPermission(permission, context.getPackageName());
        }
        return true;
    }


    private static class WebSocketHandlerWraper {
        public static WebSocketClientManager webSocketClientManager = new WebSocketClientManager();
    }

}
