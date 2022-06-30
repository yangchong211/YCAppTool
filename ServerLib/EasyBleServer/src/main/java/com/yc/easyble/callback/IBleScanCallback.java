package com.yc.easyble.callback;

import com.yc.easyble.data.BleDevice;


public interface IBleScanCallback {

    /**
     * 开始扫描
     *
     * @param success 是否成功
     */
    void onScanStarted(boolean success);

    /**
     * 扫描中
     *
     * @param bleDevice 蓝牙设备
     */
    void onScanning(BleDevice bleDevice);

}
