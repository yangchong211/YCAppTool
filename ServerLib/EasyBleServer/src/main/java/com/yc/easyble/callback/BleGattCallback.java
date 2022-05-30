
package com.yc.easyble.callback;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.yc.easyble.data.BleDevice;
import com.yc.easyble.exception.BleException;


@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public abstract class BleGattCallback extends BluetoothGattCallback {

    public abstract void onStartConnect();

    public abstract void onConnectFail(BleDevice bleDevice, BleException exception);

    public abstract void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status);

    public abstract void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status);

}