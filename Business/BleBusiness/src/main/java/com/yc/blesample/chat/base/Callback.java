package com.yc.blesample.chat.base;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class Callback extends BluetoothGattCallback {

    public static final int CONNECTED_FAIL=10000;

    private static final String UUIDString="fa87c0d0-afac-11de-8a39-0800200c9a66";


    private Handler handler;

    private BluetoothDevice device;

    public Callback(Handler handler,BluetoothDevice device) {
        this.handler = handler;
        this.device=device;
    }


    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
       if (status==BluetoothGatt.GATT_SUCCESS){
           handler.sendEmptyMessage(newState);
       }else {
           Log.i("GitCode", "gatt fail");
           handler.sendEmptyMessage(CONNECTED_FAIL);
       }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
       List<BluetoothGattService>  services=gatt.getServices();
       for (BluetoothGattService service:services){
           List<BluetoothGattCharacteristic> characteristics=service.getCharacteristics();
           for (BluetoothGattCharacteristic characteristic: characteristics){
                if (characteristic.getUuid().toString().equals(UUIDString)){
                    characteristic.setValue("找到你啦");
                   // service.writeToParcel(characteristic);
               }
           }
       }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorRead(gatt, descriptor, status);
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);
    }
}
