package com.yc.socket.websocket;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import java.nio.ByteBuffer;

/**
 * 负责 WebSocket 重连
 */
public class ReconnectManager {

    private Context mContext;
    // 重试时间间隔
    private int retryInterval[] = {500, 500, 1000, 1000, 2 * 1000, 3 * 1000, 3 * 1000, 3 * 1000, 3 * 1000, 3 * 1000, 4 * 1000, 4 * 1000, 4 * 1000, 4 * 1000, 4 * 1000, 6 * 1000, 7 * 1000, 8 * 1000, 9 * 1000, 10 * 1000};
    private static Handler handler = new Handler();
    // 是否正在重连
    private boolean isReconnecting = false;

    /**
     * 重试次数
     */
    private int retryCount = 0;
    private ISocketListener listener;

    private ReconnectManager() {

    }

    public static ReconnectManager getInstance() {
        return ReconnectManager.ReconnectManagerHolder.INSTANCE;
    }

    public void init(Context context) {
        this.mContext = context;
        if (listener != null) {
            return;
        }
        listener = new ISocketListener() {
            @Override
            public void onConnected() {
                retryCount = 0;
                isReconnecting = false;
            }

            @Override
            public void onConnectFailed(Throwable e) {
                isReconnecting = false;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startReconnect();
                    }
                }, getRetryInterval(retryCount));
            }

            @Override
            public void onDisconnect() {
                isReconnecting = false;
            }

            @Override
            public <T> void onMessage(String message) {
                isReconnecting = false;
            }

            @Override
            public <T> void onMessage(ByteBuffer bytes) {
                isReconnecting = false;
            }
        };
        SocketCallBackManager.getInstance().addListener(listener);
    }

    private static class ReconnectManagerHolder {
        static ReconnectManager INSTANCE = new ReconnectManager();

    }

    public void startReconnect() {
        if (!netWorkIsConnect()) {
            WebSocketLog.i("ReconnectManager", "网络未连接停止重试");
            return;
        }
        if (isReconnecting) {
            WebSocketLog.i("ReconnectManager", "正在进行重连 return");
            return;
        }

        retryCount++;
        WebSocketSetting webSocketSetting = WebSocketClientManager.getInstance().getWebSocketSetting();
        if (webSocketSetting != null && retryCount <= webSocketSetting.getReconnectFrequency()) {
            isReconnecting = true;
            WebSocketClientManager.getInstance().reconnect();
            WebSocketLog.i("ReconnectManager", "重试第" + retryCount + "次");
        } else {
            WebSocketLog.i("ReconnectManager", "重试第" + retryCount + "次 超过重试次数了");
        }
    }


    /**
     * 获取连接重试间隔
     *
     * @param retryIndex 第几次重试
     */
    private int getRetryInterval(int retryIndex) {
        int maxtLimit = Math.min(Math.max(0, retryIndex), retryInterval.length - 1);
        return retryInterval[maxtLimit];
    }

    /**
     * 获取网络状态
     *
     * @return
     */
    private boolean netWorkIsConnect() {
        ConnectivityManager manager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        try {
            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
            if (activeNetwork == null) {
                return false;
            }
            return activeNetwork.isConnected();
        } catch (Exception e) {
            return false;
        }
    }
}
