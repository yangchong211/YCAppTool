package com.yc.easyble.callback;


import com.yc.easyble.data.BleDevice;

public abstract class BleScanAndConnectCallback extends BleGattCallback implements IBleScanCallback {

    public abstract void onScanFinished(BleDevice scanResult);

    public void onLeScan(BleDevice bleDevice) {
    }

}
