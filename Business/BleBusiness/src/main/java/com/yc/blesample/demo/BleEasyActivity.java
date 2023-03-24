package com.yc.blesample.demo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.yc.blesample.R;
import com.yc.blesample.ble.BleMainActivity;
import com.yc.toastutils.ToastUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;

public class BleEasyActivity extends AppCompatActivity {

    private TextView tvCheck;
    private TextView tvFind;
    private TextView tvFindContent;
    private TextView tvGetBle;
    private TextView tvBleContent;
    private static final int REQUEST_ENABLE_BT = 101;
    BluetoothAdapter bluetoothAdapter;
    private ArrayList<Bean> list = new ArrayList<>();
    private ArrayList<Bean> bleList = new ArrayList<>();

    /**
     * 开启页面
     *
     * @param context 上下文
     */
    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, BleEasyActivity.class);
            context.startActivity(target);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

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
        tvCheck = findViewById(R.id.tv_check);
        tvFind = findViewById(R.id.tv_find);
        tvFindContent = findViewById(R.id.tv_find_content);
        tvGetBle = findViewById(R.id.tv_get_ble);
        tvBleContent = findViewById(R.id.tv_ble_content);
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
                ToastUtils.showRoundRectToast("查询已配对设备");
                findDevice();
            }
        });
        tvGetBle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showRoundRectToast("查询连接的设备");
                startFind();
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
            list.clear();
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
                    Bean bean = new Bean();
                    bean.deviceName = deviceName+"";
                    bean.deviceHardwareAddress = deviceHardwareAddress;
                    list.add(bean);
                }
            }
        }
        setText();
    }

    private void setText(){
        if (list!=null && list.size()>0){
            StringBuilder stringBuilder = new StringBuilder();
            for (int i=0 ; i<list.size() ; i++){
                stringBuilder.append(list.get(i).deviceName);
                stringBuilder.append(" - ");
                stringBuilder.append(list.get(i).deviceHardwareAddress);
                stringBuilder.append("\n");
            }
            tvFindContent.setText(stringBuilder.toString());
        } else  {
            tvFindContent.setText("暂无匹配数据");
        }
    }

    /**
     * 发现设备
     * 如要开始发现设备，只需调用 startDiscovery()。
     * 该进程为异步操作，并且会返回一个布尔值，指示发现进程是否已成功启动。发现进程通常包含约 12 秒钟的查询扫描，随后会对发现的每台设备进行页面扫描，以检索其蓝牙名称。
     */
    private void startFind(){
        bleList.clear();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
        tvBleContent.postDelayed(new Runnable() {
            @Override
            public void run() {
                setText2();
            }
        },10000);
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
                Bean bean = new Bean();
                bean.deviceName = deviceName+"";
                bean.deviceHardwareAddress = deviceHardwareAddress;
                bleList.add(bean);
            }
        }
    };

    public class Bean{
        public String deviceName;
        public String deviceHardwareAddress;
    }


    private void setText2(){
        if (bleList!=null && bleList.size()>0){
            StringBuilder stringBuilder = new StringBuilder();
            for (int i=0 ; i<bleList.size() ; i++){
                stringBuilder.append(bleList.get(i).deviceName);
                stringBuilder.append(" - ");
                stringBuilder.append(bleList.get(i).deviceHardwareAddress);
                stringBuilder.append("\n");
            }
            tvBleContent.setText(stringBuilder.toString());
        } else  {
            tvBleContent.setText("暂无匹配数据");
        }
    }

}
