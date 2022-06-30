package com.yc.easyble.callback;


import com.yc.easyble.data.BleDevice;

import java.util.List;

public abstract class BleScanCallback implements IBleScanCallback {

    public abstract void onScanFinished(List<BleDevice> scanResultList);

    public void onLeScan(BleDevice bleDevice) {

    }
}
