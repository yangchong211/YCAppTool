package com.yc.netreceiver;

public interface NetStatusListener {

    int NET_ETHERNET = 1;
    int NET_WIFI = 2;
    int NET_MOBILE = 3;
    int NET_NONE = 0;

    void onChange(boolean connect, int netType);

}
