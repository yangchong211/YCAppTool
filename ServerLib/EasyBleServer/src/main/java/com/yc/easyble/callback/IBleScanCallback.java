package com.yc.easyble.callback;

import com.yc.easyble.data.BleDevice;


public interface IBleScanCallback {

    /**
     * 开始扫描
     * @param success
     */
    void onScanStarted(boolean success);

    /**
     * 扫描中
     * @param bleDevice
     */
    void onScanning(BleDevice bleDevice);

}
