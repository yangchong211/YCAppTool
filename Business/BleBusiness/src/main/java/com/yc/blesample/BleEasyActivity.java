package com.yc.blesample;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.yc.toastutils.ToastUtils;

import java.util.Set;

public class BleEasyActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvCheck;
    private TextView tvFind;
    private static final int REQUEST_ENABLE_BT = 101;
    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_test);
        initView();
        initListener();
        initBluetoothAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        tvCheck = findViewById(R.id.tv_check);
        tvFind = findViewById(R.id.tv_find);
    }

    private void initListener() {
        tvCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBle();
            }
        });
        tvFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDevice();
            }
        });
    }

    private void initBluetoothAdapter() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * 检查蓝牙
     */
    private void checkBle(){
        //调用 isEnabled()，以检查当前是否已启用蓝牙。如果此方法返回 false，则表示蓝牙处于停用状态。
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            ToastUtils.showRoundRectToast("蓝牙可用");
        }
    }

    /**
     * 查询已配对设备
     */
    private void findDevice(){
        if (bluetoothAdapter!=null){
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                for (BluetoothDevice device : pairedDevices) {
                    //获取名称
                    String deviceName = device.getName();
                    // MAC address
                    String deviceHardwareAddress = device.getAddress();
                    BluetoothClass bluetoothClass = device.getBluetoothClass();
                    int bondState = device.getBondState();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        int type = device.getType();
                    }
                    ParcelUuid[] uuids = device.getUuids();
                }
            }
        }
    }

    /**
     * 发现设备
     * 如要开始发现设备，只需调用 startDiscovery()。
     * 该进程为异步操作，并且会返回一个布尔值，指示发现进程是否已成功启动。发现进程通常包含约 12 秒钟的查询扫描，随后会对发现的每台设备进行页面扫描，以检索其蓝牙名称。
     */
    private void startFind(){
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
    };

    public class Bean{
        public String deviceName;
        public String deviceHardwareAddress;
    }
}
