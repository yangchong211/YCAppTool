package com.yc.blesample.ble.comm;


import com.yc.easyble.data.BleDevice;

public interface IObservable {

    void addObserver(IObserver obj);

    void deleteObserver(IObserver obj);

    void notifyObserver(BleDevice bleDevice);
}
