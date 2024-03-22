package com.yc.socket.websocket;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class SocketCallBackManager {

    private List<ISocketListener> listeners = new ArrayList<>();

    private SocketCallBackManager() {
    }

    public static SocketCallBackManager getInstance() {
        return SocketCallBackManagerHolder.INSTANCE;
    }

    private static class SocketCallBackManagerHolder {
        static SocketCallBackManager INSTANCE = new SocketCallBackManager();

    }

    public void addListener(ISocketListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ISocketListener listener) {
        listeners.remove(listener);
    }


    public void notifyConnected() {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onConnected();
        }
    }


    public void notifyDisConnected() {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onDisconnect();
        }
    }

    public void notifyConnectFailed(Throwable e) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onConnectFailed(e);
        }
    }


    public void notifyReciveString(String msg) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onMessage(msg);
        }
    }

    public void notifyReciveBytes(ByteBuffer byteBuffer) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onMessage(byteBuffer);
        }
    }

}
