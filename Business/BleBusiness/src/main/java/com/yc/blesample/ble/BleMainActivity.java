package com.yc.blesample.ble;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.yc.blesample.R;
import com.yc.blesample.ble.adapter.BleDeviceAdapter;
import com.yc.blesample.ble.comm.ObserverManager;
import com.yc.blesample.ble.operation.OperationActivity;
import com.yc.easyble.BleManager;
import com.yc.easyble.callback.BleGattCallback;
import com.yc.easyble.callback.BleMtuChangedCallback;
import com.yc.easyble.callback.BleRssiCallback;
import com.yc.easyble.callback.BleScanCallback;
import com.yc.easyble.data.BleDevice;
import com.yc.easyble.exception.BleException;
import com.yc.easyble.config.BleScanRuleConfig;
import com.yc.toolutils.AppDeviceUtils;
import com.yc.toolutils.AppLogUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class BleMainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = BleMainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_OPEN_GPS = 1;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 2;

    private LinearLayout layout_setting;
    private TextView txt_setting;
    private TextView tvContent,tv_mac;
    private Button btn_scan, btn_find;
    private EditText et_name, et_mac, et_uuid;
    private Switch sw_auto;
    private ImageView img_loading;

    private Animation operatingAnim;
    private BleDeviceAdapter mDeviceAdapter;
    private ProgressDialog progressDialog;

    /**
     * 开启页面
     *
     * @param context 上下文
     */
    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, BleMainActivity.class);
            context.startActivity(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_main);
        initView();


        BleManager.getInstance().init(getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setConnectOverTime(20000)
                .setOperateTimeout(5000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showConnectedDevice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
    }

    private void test() {
        boolean blueEnable = BleManager.getInstance().isBlueEnable();
        AppLogUtils.d("BleManager: 是否支持蓝牙 " + blueEnable);
        boolean b = BleManager.getInstance().enableBluetooth();
        AppLogUtils.d("BleManager: 判断是否打开蓝牙 " + b);
        boolean b2 = BleManager.getInstance().disableBluetooth();
        AppLogUtils.d("BleManager: 关闭本地蓝牙 " + b);
        boolean b3 = BleManager.getInstance().enableBluetooth();
        AppLogUtils.d("BleManager: 打开蓝牙 " + b3);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_scan) {
            if (btn_scan.getText().equals(getString(R.string.start_scan))) {
                //开始扫描
                checkPermissions();
            } else if (btn_scan.getText().equals(getString(R.string.stop_scan))) {
                //停止扫描
                BleManager.getInstance().cancelScan();
            }
        } else if (id == R.id.txt_setting) {
            if (layout_setting.getVisibility() == View.VISIBLE) {
                layout_setting.setVisibility(View.GONE);
                txt_setting.setText(getString(R.string.expand_search_settings));
            } else {
                layout_setting.setVisibility(View.VISIBLE);
                txt_setting.setText(getString(R.string.retrieve_search_settings));
            }
        } else if (id == R.id.btn_find) {
            Set<BluetoothDevice> bondedDevices =
                    BleManager.getInstance().getBluetoothAdapter().getBondedDevices();
            List<BleDevice> deviceList = new ArrayList<>();
            if (bondedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                for (BluetoothDevice device : bondedDevices) {
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
                    deviceList.add(new BleDevice(device));
                }
            }
            mDeviceAdapter.clearConnectedDevice();
            mDeviceAdapter.addAllDevice(deviceList);
        }
    }

    private void initView() {
        tvContent = findViewById(R.id.tv_content);
        btn_scan = (Button) findViewById(R.id.btn_scan);
        btn_find = findViewById(R.id.btn_find);
        btn_scan.setText(getString(R.string.start_scan));
        btn_scan.setOnClickListener(this);
        btn_find.setOnClickListener(this);
        et_name = (EditText) findViewById(R.id.et_name);
        et_mac = (EditText) findViewById(R.id.et_mac);
        et_uuid = (EditText) findViewById(R.id.et_uuid);
        sw_auto = (Switch) findViewById(R.id.sw_auto);
        tv_mac = findViewById(R.id.tv_mac);
        layout_setting = (LinearLayout) findViewById(R.id.layout_setting);
        txt_setting = (TextView) findViewById(R.id.txt_setting);
        txt_setting.setOnClickListener(this);
        layout_setting.setVisibility(View.VISIBLE);
        txt_setting.setText(getString(R.string.expand_search_settings));

        img_loading = (ImageView) findViewById(R.id.img_loading);
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        operatingAnim.setInterpolator(new LinearInterpolator());
        progressDialog = new ProgressDialog(this);

        mDeviceAdapter = new BleDeviceAdapter(this);
        mDeviceAdapter.setOnDeviceClickListener(new BleDeviceAdapter.OnDeviceClickListener() {
            @Override
            public void onItemClick(BleDevice bleDevice) {
                Intent intent = new Intent(BleMainActivity.this, BleDetailActivity.class);
                intent.putExtra("device", bleDevice);
                startActivity(intent);
            }

            @Override
            public void onConnect(BleDevice bleDevice) {
                //开始连接
                if (!BleManager.getInstance().isConnected(bleDevice)) {
                    //停止搜索
                    BleManager.getInstance().cancelScan();
                    connect(bleDevice);
                }
            }

            @Override
            public void onDisConnect(final BleDevice bleDevice) {
                //取消连接
                if (BleManager.getInstance().isConnected(bleDevice)) {
                    BleManager.getInstance().disconnect(bleDevice);
                }
            }

            @Override
            public void onDetail(BleDevice bleDevice) {
                //详情
                if (BleManager.getInstance().isConnected(bleDevice)) {
                    Intent intent = new Intent(BleMainActivity.this, OperationActivity.class);
                    intent.putExtra(OperationActivity.KEY_DATA, bleDevice);
                    startActivity(intent);
                }
            }

            @Override
            public void readRssi(BleDevice bleDevice) {
                readBleDeviceRssi(bleDevice);
            }
        });
        ListView listView_device = (ListView) findViewById(R.id.list_device);
        listView_device.setAdapter(mDeviceAdapter);
        tv_mac.setText(AppDeviceUtils.getMacAddress(this));
    }

    private void showConnectedDevice() {
        List<BleDevice> deviceList = BleManager.getInstance().getAllConnectedDevice();
        mDeviceAdapter.addAllDevice(deviceList);
    }

    /**
     * 设置扫描规则
     */
    private void setScanRule() {
        String[] uuids;
        String str_uuid = et_uuid.getText().toString();
        if (TextUtils.isEmpty(str_uuid)) {
            uuids = null;
        } else {
            uuids = str_uuid.split(",");
        }
        UUID[] serviceUuids = null;
        if (uuids != null && uuids.length > 0) {
            serviceUuids = new UUID[uuids.length];
            for (int i = 0; i < uuids.length; i++) {
                String name = uuids[i];
                String[] components = name.split("-");
                if (components.length != 5) {
                    serviceUuids[i] = null;
                } else {
                    serviceUuids[i] = UUID.fromString(uuids[i]);
                }
            }
        }

        String[] names;
        String str_name = et_name.getText().toString();
        if (TextUtils.isEmpty(str_name)) {
            names = null;
        } else {
            names = str_name.split(",");
        }

        String mac = et_mac.getText().toString();

        boolean isAutoConnect = sw_auto.isChecked();

        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setServiceUuids(serviceUuids)      // 只扫描指定的服务的设备，可选
                .setDeviceName(true, names)   // 只扫描指定广播名的设备，可选
                .setDeviceMac(mac)                  // 只扫描指定mac的设备，可选
                .setAutoConnect(isAutoConnect)      // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(10000)              // 扫描超时时间，可选，默认10秒
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
    }

    /**
     * 开始扫描
     */
    private void startScan() {
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                mDeviceAdapter.clearScanDevice();
                img_loading.startAnimation(operatingAnim);
                img_loading.setVisibility(View.VISIBLE);
                btn_find.setVisibility(View.GONE);
                btn_scan.setText(getString(R.string.stop_scan));
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {

            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                mDeviceAdapter.addAllDevice(scanResultList);
                img_loading.clearAnimation();
                img_loading.setVisibility(View.INVISIBLE);
                btn_scan.setText(getString(R.string.stop_scan));
                btn_find.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 连接
     *
     * @param bleDevice
     */
    private void connect(final BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                progressDialog.show();
                tvContent.setText("连接状态：" + "开始连接");
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                img_loading.clearAnimation();
                img_loading.setVisibility(View.INVISIBLE);
                btn_scan.setText(getString(R.string.start_scan));
                progressDialog.dismiss();
                tvContent.setText("连接状态：" + "连接失败" + exception.getDescription());
                Toast.makeText(BleMainActivity.this, getString(R.string.connect_fail), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();
                mDeviceAdapter.clearScanDevice();
                mDeviceAdapter.addDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();
                tvContent.setText("连接状态：" + "连接成功");
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();
                mDeviceAdapter.clearScanDevice();
                mDeviceAdapter.addDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();

                if (isActiveDisConnected) {
                    tvContent.setText("连接状态：" + "断开了");
                } else {
                    tvContent.setText("连接状态：" + "连接断开");
                    ObserverManager.getInstance().notifyObserver(bleDevice);
                }
            }
        });
    }

    private void readBleDeviceRssi(BleDevice bleDevice) {
        BleManager.getInstance().readRssi(bleDevice, new BleRssiCallback() {
            @Override
            public void onRssiFailure(BleException exception) {
                Log.i(TAG, "onRssiFailure" + exception.toString());
                tvContent.setText("连接状态：" + "onRssiFailure" + exception.toString());
            }

            @Override
            public void onRssiSuccess(int rssi) {
                Log.i(TAG, "onRssiSuccess: " + rssi);
                tvContent.setText("连接状态：" + "onRssiSuccess" + rssi);
            }
        });
    }

    private void setMtu(BleDevice bleDevice, int mtu) {
        BleManager.getInstance().setMtu(bleDevice, mtu, new BleMtuChangedCallback() {
            @Override
            public void onSetMTUFailure(BleException exception) {
                Log.i(TAG, "onsetMTUFailure" + exception.toString());
            }

            @Override
            public void onMtuChanged(int mtu) {
                Log.i(TAG, "onMtuChanged: " + mtu);
            }
        });
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode,
                                                 @NonNull String[] permissions,
                                                 @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_LOCATION:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            onPermissionGranted(permissions[i]);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    private void checkPermissions() {
        if (!BleManager.getInstance().isBlueEnable()) {
            Toast.makeText(this, getString(R.string.please_open_blue), Toast.LENGTH_LONG).show();
            return;
        }
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission);
            } else {
                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
            ActivityCompat.requestPermissions(this, deniedPermissions, REQUEST_CODE_PERMISSION_LOCATION);
        }
    }

    private void onPermissionGranted(String permission) {
        if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permission)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGPSIsOpen()) {
                showDialog();
            } else {
                setScanRule();
                startScan();
            }
        }
    }

    private void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.notifyTitle)
                .setMessage(R.string.gpsNotifyMsg)
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                .setPositiveButton(R.string.setting,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(intent, REQUEST_CODE_OPEN_GPS);
                            }
                        })

                .setCancelable(false)
                .show();
    }

    private boolean checkGPSIsOpen() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            return false;
        }
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OPEN_GPS) {
            if (checkGPSIsOpen()) {
                setScanRule();
                startScan();
            }
        }
    }
}
