package com.yc.socket.socketio;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.OkHttpClient;

import com.yc.easyexecutor.DelegateTaskExecutor;
import com.yc.notcapturelib.ssl.TrustAllCertsManager;

public class SocketIoManager {

    private Socket mSocket;
    private Map<String, List<OnmessageListener>> listenersMap = new HashMap<>();
    private SocketIoConfig config;

    private SocketIoManager() {
    }

    public static SocketIoManager getInstance() {
        return SocketIoManagerHolder.socketIoManager;
    }

    private static class SocketIoManagerHolder {
        static SocketIoManager socketIoManager = new SocketIoManager();
    }

    public void init(SocketIoConfig socketIoConfig) {
        this.config = socketIoConfig;
        try {
            String url = socketIoConfig.getUrl();
            String query = socketIoConfig.getQuery();
            IO.Options options = new IO.Options();
            URL url1 = new URL(url);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .hostnameVerifier(new TrustAllCertsManager.TrustAllHostnameVerifier(url1.getHost()))
                    .sslSocketFactory(TrustAllCertsManager.createSSLSocketFactory(), TrustAllCertsManager.createX509TrustManager())
                    .build();

// default settings for all sockets
//            IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
//            IO.setDefaultOkHttpCallFactory(okHttpClient);
//
//// set as an option
//            options.callFactory = okHttpClient;
//            options.webSocketFactory = okHttpClient;
            options.transports=new String[]{"websocket"};

            if (!TextUtils.isEmpty(query)) {
                options.query = query;
            }
            mSocket = IO.socket(url, options);
        } catch (URISyntaxException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 连接
     */
    public void connect() {
        if (mSocket != null) {
            mSocket.connect();
        } else {
            throw new RuntimeException("please init first");
        }
    }


    /**
     * 断开连接
     */
    public void disconnect() {
        listenersMap.clear();
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket.off();
        }
    }

    /**
     * 添加接收消息的监听
     * 要在init之后 ，connect之前
     *
     * @param eventName
     * @param onMessageListener
     */
    public void addMessageReciver(final String eventName, final OnmessageListener onMessageListener) {
        if (listenersMap.containsKey(eventName)) {
            List<OnmessageListener> onmessageListeners = listenersMap.get(eventName);
            onmessageListeners.add(onMessageListener);
        } else {
            List<OnmessageListener> newListeners = new ArrayList<>();
            newListeners.add(onMessageListener);
            listenersMap.put(eventName, newListeners);
            if (mSocket != null) {
                mSocket.on(eventName, new Emitter.Listener() {
                    @Override
                    public void call(final Object... args) {
                        String realData = " ";
                        if (args != null && args.length > 0) {
                            realData = args[0].toString();
                        }
                        if (config.isShowLog()) {
                            Log.i("socketIo_receive", "eventName: " + eventName + "   data:" + realData);
                        }

                        List<OnmessageListener> onmessageListeners = listenersMap.get(eventName);
                        if (onmessageListeners == null) {
                            return;
                        }
                        // 拷贝后再遍历通知防止出现在消息中移除监听器的情况造成ConcurrentModificationException
                        List<OnmessageListener> copyListeners = new ArrayList<>(onmessageListeners);
                        for (final OnmessageListener onmessageListener : copyListeners) {
                            if (onmessageListener.needCallBackOnUiThread()) {
                                DelegateTaskExecutor.getInstance().postToMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        onmessageListener.onMessage(args);
                                    }
                                });
                            } else {
                                onMessageListener.onMessage(args);
                            }
                        }
                        copyListeners.clear();
                    }
                });
            }
        }
    }

    /**
     * 移除监听
     *
     * @param eventName
     */
    public void removeMessageListener(String eventName) {
        listenersMap.remove(eventName);
        if (mSocket != null) {
            mSocket.off(eventName);
        }
    }


    /**
     * 移除监听
     *
     * @param onMessageListener
     */
    public void removeMessageListener(OnmessageListener onMessageListener) {
        if (onMessageListener == null) {
            return;
        }
        List<String> waitRemoveKeys = new ArrayList<>();
        for (String key : listenersMap.keySet()) {
            List<OnmessageListener> onmessageListeners = listenersMap.get(key);
            if (onmessageListeners != null && onmessageListeners.contains(onMessageListener)) {
                onmessageListeners.remove(onMessageListener);
                waitRemoveKeys.add(key);
            }
        }

        for (String eventNameKey : waitRemoveKeys) {
            List<OnmessageListener> onmessageListeners = listenersMap.get(eventNameKey);
            if (onmessageListeners == null || onmessageListeners.size() == 0) {
                if (mSocket != null) {
                    mSocket.off(eventNameKey);
                }
                listenersMap.remove(eventNameKey);
            }
        }

    }


    /**
     * 发送消息
     *
     * @param messageEntity
     */
    public void sendMessage(final MessageEntity messageEntity) {
        if (messageEntity == null) {
            return;
        }
        String realData = " ";
        Object[] data1 = messageEntity.getData();
        if (data1 != null && data1.length > 0) {
            realData = data1[0].toString();
        }
        if (config != null && config.isShowLog()) {
            Log.i("socketIo_send", "methodName: " + messageEntity.getMethodName() + "     data: " + realData);
        }
        if (mSocket != null) {
            if (messageEntity.getAck() == null) {
                Object[] data = messageEntity.getData();
                if (data == null) {
                    mSocket.emit(messageEntity.getMethodName());
                } else {
                    mSocket.emit(messageEntity.getMethodName(), messageEntity.getData());
                }
            } else {
                mSocket.emit(messageEntity.getMethodName(), messageEntity.getData(), new Ack() {
                    @Override
                    public void call(final Object... args) {
                        String realData = " ";
                        if (args != null && args.length > 0) {
                            realData = args[0].toString();
                        }
                        if (config != null && config.isShowLog()) {
                            Log.i("socketIo_receive_ack", "data:" + realData);
                        }
                        MessageAck ack = messageEntity.getAck();
                        if (ack.needCallBackOnUiThread()) {
                            DelegateTaskExecutor.getInstance().executeOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    messageEntity.getAck().call(args);
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    public boolean isConnected() {
        return mSocket != null && mSocket.connected();
    }

    /**
     * 根据value获取key
     *
     * @param map
     * @param value
     * @return
     */
    private List<String> getKeyByLoop(Map<String, List<OnmessageListener>> map, List<OnmessageListener> value) {
        List<String> keys = new ArrayList<>();
        for (Map.Entry<String, List<OnmessageListener>> entry : map.entrySet()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Objects.equals(entry.getValue(), value)) {
                    keys.add(entry.getKey());
                }
            }
        }
        return keys;
    }

}

