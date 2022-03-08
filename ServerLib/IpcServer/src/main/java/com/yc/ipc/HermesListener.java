package com.yc.ipc;


public abstract class HermesListener {

    public abstract void onHermesConnected(Class<? extends HermesService> service);

    public void onHermesDisconnected(Class<? extends HermesService> service) {

    }

}
