package com.yc.easyble.utils;

import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.util.Log;

import com.yc.easyble.BleManager;

import java.lang.reflect.Method;
import java.util.Set;

public final class BleHelperUtils {

    private final static String TAG = "BleHelperUtils";

    /**
     * 在低于Android API 19时，配对的方法是隐藏的方法，所以只有通过反射方法实现。反射BluetoothDevice类中的createBond方法。
     * @param bluetoothDevice           bluetoothDevice
     * @return      true 执行绑定 false 未执行绑定
     */
    public static boolean boundDevice(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice == null) {
            Log.e(TAG, "boundDevice-->bluetoothDevice == null");
            return false;
        }
        try {
            Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
            Boolean returnValue = (Boolean) createBondMethod.invoke(bluetoothDevice);
            if (returnValue == null){
                return false;
            }
            return returnValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 官方API，但是只支持Android API 19以上的设备。
     * @param bluetoothDevice       bluetoothDevice
     * @return      true 执行绑定 false 未执行绑定
     */
    public static boolean boundDeviceAPI(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice == null) {
            return false;
        }
        //注意：Android 4.4版本之后的API
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return bluetoothDevice.createBond();
        }
        return false;
    }

    /**
     * 解除配对的方法是隐藏的方法，所以只有通过反射方法实现。反射BluetoothDevice类中的removeBond方法。
     * 参考：/Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
     * @param bluetoothDevice           bluetoothDevice
     * @return      true 执行解除绑定 false 未执行解除绑定
     */
    public static boolean removeBond(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice == null) {
            Log.e(TAG, "boundDevice-->bluetoothDevice == null");
            return false;
        }
        try {
            Method createBondMethod = BluetoothDevice.class.getMethod("removeBond");
            Boolean returnValue = (Boolean) createBondMethod.invoke(bluetoothDevice);
            if (returnValue == null){
                return false;
            }
            return returnValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 判断设备是否配对
     * @param bluetoothDevice           bluetoothDevice
     * @return
     */
    public static boolean findDeviceBonded(BluetoothDevice bluetoothDevice){
        //获取所有已经配对的设备
        Set<BluetoothDevice> bondedDevices =
                BleManager.getInstance().getBluetoothAdapter().getBondedDevices();
        if (bondedDevices != null && bondedDevices.size() > 0){
            for (BluetoothDevice device : bondedDevices) {
                if (device.getAddress().equals(bluetoothDevice.getAddress())){
                    return true;
                }
            }
        }
        return false;
    }

}
