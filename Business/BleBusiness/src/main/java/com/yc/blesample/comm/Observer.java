package com.yc.blesample.comm;


import com.yc.easyble.data.BleDevice;

public interface Observer {

    void disConnected(BleDevice bleDevice);
}
