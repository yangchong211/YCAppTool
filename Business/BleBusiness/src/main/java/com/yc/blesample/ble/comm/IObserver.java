package com.yc.blesample.ble.comm;


import com.yc.easyble.data.BleDevice;

public interface IObserver {

    void disConnected(BleDevice bleDevice);
}
