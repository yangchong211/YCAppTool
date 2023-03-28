package com.yc.easyble.scan;


import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import com.yc.easyble.BleManager;
import com.yc.easyble.callback.BleScanAndConnectCallback;
import com.yc.easyble.callback.BleScanCallback;
import com.yc.easyble.callback.IBleScanCallback;
import com.yc.easyble.data.BleDevice;
import com.yc.easyble.data.BleScanState;
import com.yc.easyble.utils.BleLog;

import java.util.List;
import java.util.UUID;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BleScanner {

    public static BleScanner getInstance() {
        return BleScannerHolder.S_BLE_SCANNER;
    }

    private static class BleScannerHolder {
        private static final BleScanner S_BLE_SCANNER = new BleScanner();
    }

    private BleScanState mBleScanState = BleScanState.STATE_IDLE;

    private final BleScanPresenter mBleScanPresenter = new BleScanPresenter() {

        @Override
        public void onScanStarted(boolean success) {
            IBleScanCallback callback = mBleScanPresenter.getBleScanPresenterImp();
            if (callback != null) {
                callback.onScanStarted(success);
            }
        }

        @Override
        public void onLeScan(BleDevice bleDevice) {
            if (mBleScanPresenter.ismNeedConnect()) {
                BleScanAndConnectCallback callback = (BleScanAndConnectCallback)
                        mBleScanPresenter.getBleScanPresenterImp();
                if (callback != null) {
                    callback.onLeScan(bleDevice);
                }
            } else {
                BleScanCallback callback = (BleScanCallback) mBleScanPresenter.getBleScanPresenterImp();
                if (callback != null) {
                    callback.onLeScan(bleDevice);
                }
            }
        }

        @Override
        public void onScanning(BleDevice result) {
            IBleScanCallback callback = mBleScanPresenter.getBleScanPresenterImp();
            if (callback != null) {
                callback.onScanning(result);
            }
        }

        @Override
        public void onScanFinished(List<BleDevice> bleDeviceList) {
            if (mBleScanPresenter.ismNeedConnect()) {
                final BleScanAndConnectCallback callback = (BleScanAndConnectCallback)
                        mBleScanPresenter.getBleScanPresenterImp();
                if (bleDeviceList == null || bleDeviceList.size() < 1) {
                    if (callback != null) {
                        callback.onScanFinished(null);
                    }
                } else {
                    if (callback != null) {
                        callback.onScanFinished(bleDeviceList.get(0));
                    }
                    final List<BleDevice> list = bleDeviceList;
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            BleManager.getInstance().connect(list.get(0), callback);
                        }
                    }, 100);
                }
            } else {
                BleScanCallback callback = (BleScanCallback) mBleScanPresenter.getBleScanPresenterImp();
                if (callback != null) {
                    callback.onScanFinished(bleDeviceList);
                }
            }
        }
    };

    public void scan(UUID[] serviceUuids, String[] names, String mac, boolean fuzzy,
                     long timeOut, final BleScanCallback callback) {

        startLeScan(serviceUuids, names, mac, fuzzy, false, timeOut, callback);
    }

    public void scanAndConnect(UUID[] serviceUuids, String[] names, String mac, boolean fuzzy,
                               long timeOut, BleScanAndConnectCallback callback) {

        startLeScan(serviceUuids, names, mac, fuzzy, true, timeOut, callback);
    }

    private synchronized void startLeScan(UUID[] serviceUuids, String[] names, String mac, boolean fuzzy,
                                          boolean needConnect, long timeOut, IBleScanCallback imp) {

        if (mBleScanState != BleScanState.STATE_IDLE) {
            BleLog.w("scan action already exists, complete the previous scan action first");
            if (imp != null) {
                imp.onScanStarted(false);
            }
            return;
        }

        mBleScanPresenter.prepare(names, mac, fuzzy, needConnect, timeOut, imp);
        BluetoothAdapter bluetoothAdapter = BleManager.getInstance().getBluetoothAdapter();
        //是否扫描成功启动
        boolean success = bluetoothAdapter.startLeScan(serviceUuids, mBleScanPresenter);

        //使用默认参数启动蓝牙LE扫描，不使用过滤器。扫描结果将通过{@code回调}传递。对于未过滤的扫描，扫描在屏幕关闭时停止，以节省电力。
        //当屏幕再次打开时，将恢复扫描。
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
//            bluetoothLeScanner.startScan(new ScanCallback() {
//                /**
//                 * 发现有蓝牙设备时回调
//                 * @param callbackType      type类型
//                 * @param result            结果
//                 */
//                @Override
//                public void onScanResult(int callbackType, ScanResult result) {
//                    super.onScanResult(callbackType, result);
//                    BluetoothDevice device = result.getDevice();
//                }
//
//                /**
//                 * 在交付批处理结果时回调
//                 * @param results   先前扫描的扫描结果列表
//                 */
//                @Override
//                public void onBatchScanResults(List<ScanResult> results) {
//                    super.onBatchScanResults(results);
//                }
//
//                /**
//                 * 扫描失败回调
//                 * @param errorCode     异常码
//                 */
//                @Override
//                public void onScanFailed(int errorCode) {
//                    super.onScanFailed(errorCode);
//                }
//            });
        }
        mBleScanState = success ? BleScanState.STATE_SCANNING : BleScanState.STATE_IDLE;
        mBleScanPresenter.notifyScanStarted(success);
    }

    public synchronized void stopLeScan() {
        BluetoothAdapter bluetoothAdapter = BleManager.getInstance().getBluetoothAdapter();
        bluetoothAdapter.stopLeScan(mBleScanPresenter);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
//            bluetoothLeScanner.stopScan(new ScanCallback() {
//                @Override
//                public void onScanResult(int callbackType, ScanResult result) {
//                    super.onScanResult(callbackType, result);
//                }
//
//                @Override
//                public void onBatchScanResults(List<ScanResult> results) {
//                    super.onBatchScanResults(results);
//                }
//
//                @Override
//                public void onScanFailed(int errorCode) {
//                    super.onScanFailed(errorCode);
//                }
//            });
        }
        mBleScanState = BleScanState.STATE_IDLE;
        mBleScanPresenter.notifyScanStopped();
    }

    public BleScanState getScanState() {
        return mBleScanState;
    }


}
