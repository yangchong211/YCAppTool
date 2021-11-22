package com.yc.appstatuslib.broadcast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yc.appstatuslib.AppStatusManager;

public final class BluetoothBroadcastReceiver extends BroadcastReceiver {

    private final AppStatusManager mManager;

    public BluetoothBroadcastReceiver(AppStatusManager mManager) {
        this.mManager = mManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //获取蓝牙设备实例【如果无设备链接会返回null，如果在无实例的状态下调用了实例的方法，会报空指针异常】
        //主要与蓝牙设备有关系
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        switch (action){
            case BluetoothDevice.ACTION_ACL_CONNECTED:
                this.notifyBluetoothSwitchState(true);
                break;
            case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                this.notifyBluetoothSwitchState(false);
                break;
            //上面的两个链接监听，其实也可以BluetoothAdapter实现，修改状态码即可
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                switch (blueState){
                    case BluetoothAdapter.STATE_OFF:
                        this.notifyBluetoothSwitchState(false);
                        break;
                    case BluetoothAdapter.STATE_ON:
                        this.notifyBluetoothSwitchState(true);
                        break;
                }
                break;
            default:
                break;
        }

    }

    private void notifyBluetoothSwitchState(boolean state) {
        if (this.mManager != null) {
            this.mManager.dispatcherBluetoothState(state);
        }
    }
}
