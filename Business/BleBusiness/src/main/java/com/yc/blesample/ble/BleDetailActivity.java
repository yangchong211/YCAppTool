package com.yc.blesample.ble;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.yc.blesample.R;
import com.yc.blesample.chat.chat.ChatActivity;
import com.yc.blesample.chat.client.ClientService;
import com.yc.blesample.chat.client.UuidAdapter;
import com.yc.easyble.data.BleDevice;
import com.yc.easyble.utils.BleHelperUtils;
import com.yc.toastutils.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.bluetooth.BluetoothDevice.ACTION_BOND_STATE_CHANGED;
import static android.bluetooth.BluetoothDevice.BOND_BONDED;


public class BleDetailActivity extends Activity {

    private static final String TAG = "DeviceActivity";
    BluetoothDevice mDevice;
    private TextView tvName, tvAddress, tvUUID, tvType, tvState;
    private Button btn, btn_bonded;
    private ListView lvUUID;
    private UuidAdapter uuidAdapter;
    private final List<ParcelUuid> list = new ArrayList<>();
    private String selectedUuidStr = ClientService.clientUuid;
    //当前设配配对状态
    private int bondState;
    //button的状态，如果已配对为false,未配对为true，先配置对后连接
    private boolean bond = false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_device);

        BleDevice bleDevice = getIntent().getParcelableExtra("device");
        if (bleDevice != null) {
            mDevice = bleDevice.getDevice();
        }
        if (mDevice == null) {
            Toast.makeText(this, "当前选择设备有误,重选！", Toast.LENGTH_LONG).show();
            finish();
        }
        bindView();
        initView();
        registerReceiver();
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void initView() {
        uuidAdapter = new UuidAdapter(this, list);
        tvName.setText(mDevice.getName() + "");
        tvAddress.setText(mDevice.getAddress() + "");
        tvUUID.setText(selectedUuidStr);
        initDeviceBondState();
        if (mDevice.getType() == BluetoothDevice.DEVICE_TYPE_LE) {
            tvType.setText("低功耗蓝牙");
        } else if (mDevice.getType() == BluetoothDevice.DEVICE_TYPE_CLASSIC) {
            tvType.setText("经典蓝牙");
        } else if (mDevice.getType() == BluetoothDevice.DEVICE_TYPE_DUAL) {
            tvType.setText("双模蓝牙");
        } else {
            tvType.setText("未知");
        }
        lvUUID.setAdapter(uuidAdapter);
        lvUUID.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedUuidStr = list.get(position).getUuid().toString();
                tvUUID.setText(selectedUuidStr);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (bond) {
                    mDevice.createBond();
                    return;
                }
                service = ClientService.getInstance(handler);
                service.connect(mDevice, selectedUuidStr);
            }
        });
        btn_bonded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bondState == BluetoothDevice.BOND_BONDED) {
                    BleHelperUtils.removeBond(mDevice);
                } else {
                    BleHelperUtils.boundDevice(mDevice);
                }
            }
        });
        for (ParcelUuid uuid : list) {
            if (uuid.getUuid().toString().equals(ClientService.clientUuid)) {
                selectedUuidStr = ClientService.clientUuid;
                tvUUID.setText(selectedUuidStr);
                return;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void initDeviceBondState() {
        bondState = mDevice.getBondState();
        if (bondState == BluetoothDevice.BOND_BONDED) {
            tvState.setText("已配对");
            btn_bonded.setText("配对状态："+tvState.getText() + " 点击取消配对");
        } else if (bondState == BluetoothDevice.BOND_BONDING) {
            tvState.setText("配对中");
            btn_bonded.setText("配对状态："+tvState.getText() + " 配对进行中");
        } else {
            tvState.setText("未配对");
            btn_bonded.setText("配对状态："+tvState.getText() + " 点击继续配对");
        }
        initBondButton();
        if (mDevice.getUuids() != null && mDevice.getUuids().length > 0) {
            list.addAll(Arrays.asList(mDevice.getUuids()));
            uuidAdapter.notifyDataSetChanged();
        }
    }

    private void initBondButton() {
        if (bondState == BOND_BONDED) {
            btn.setText("连接");
            bond = false;
        } else {
            btn.setText("配对");
            bond = true;
        }
    }

    ClientService service;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ClientService.CONNECTED_SUCCESS:
                    Log.i(TAG, "connected success");
                    ToastUtils.showRoundRectToast("连接成功");
                    Intent intent = new Intent(BleDetailActivity.this, ChatActivity.class);
                    intent.putExtra("device", mDevice);
                    intent.putExtra("uuid", selectedUuidStr);
                    startActivity(intent);
                    break;
                case ClientService.CONNECTED_FAIL:
                    Log.i(TAG, "connected fail");
                    ToastUtils.showRoundRectToast("连接失败");
                    break;
                default:
                    break;
            }
        }
    };

    private void bindView() {
        tvName = findViewById(R.id.tvDeviceName);
        tvAddress = findViewById(R.id.tvDeviceAddress);
        tvUUID = findViewById(R.id.tvDeviceUUID);
        tvType = findViewById(R.id.tvDeviceType);
        tvState = findViewById(R.id.tvDeviceState);
        btn = findViewById(R.id.btn);
        btn_bonded = findViewById(R.id.btn_bonded);
        lvUUID = findViewById(R.id.lvUUID);
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_BOND_STATE_CHANGED);
        registerReceiver(bluetoothReceiver, filter);
    }


    BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, intent.getAction());
            if (intent.getAction().equals(ACTION_BOND_STATE_CHANGED)) {
                initDeviceBondState();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bluetoothReceiver);
    }
}
