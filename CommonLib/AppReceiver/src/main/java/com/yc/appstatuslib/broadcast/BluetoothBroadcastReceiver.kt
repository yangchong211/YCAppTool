package com.yc.appstatuslib.broadcast

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.yc.appstatuslib.AppStatusManager

/**
 * @author: yangchong
 * email  : yangchong211@163.com
 * time   : 2017/5/18
 * desc   : 蓝牙监听广播
 * revise :
 */
class BluetoothBroadcastReceiver(private val mManager: AppStatusManager?) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        //获取蓝牙设备实例【如果无设备链接会返回null，如果在无实例的状态下调用了实例的方法，会报空指针异常】
        //主要与蓝牙设备有关系
        //val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
        when (intent.action) {
            BluetoothDevice.ACTION_ACL_CONNECTED -> notifyBluetoothSwitchState(true)
            BluetoothDevice.ACTION_ACL_DISCONNECTED -> notifyBluetoothSwitchState(false)
            //上面的两个链接监听，其实也可以BluetoothAdapter实现，修改状态码即可
            BluetoothAdapter.ACTION_STATE_CHANGED -> {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                Log.d("BluetoothReceiver", state.toString())
                when (state) {
                    BluetoothAdapter.STATE_OFF -> notifyBluetoothSwitchState(false)
                    BluetoothAdapter.STATE_ON -> notifyBluetoothSwitchState(true)
                    else -> {

                    }
                }
            }

            else -> {
            }
        }
    }

    private fun notifyBluetoothSwitchState(state: Boolean) {
        mManager?.dispatcherBluetoothState(state)
    }
}