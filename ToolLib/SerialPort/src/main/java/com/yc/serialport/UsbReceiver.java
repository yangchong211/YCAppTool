package com.yc.serialport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;

import com.yc.appcontextlib.AppToolUtils;
import com.yc.toolutils.AppLogUtils;

public class UsbReceiver extends BroadcastReceiver {

    public static final String ACTION_DEVICE_PERMISSION = "com.yc.serialport.USB_PERMISSION";

    private final UsbDevice usbDevice;

    public UsbReceiver(UsbDevice usbDevice) {
        this.usbDevice = usbDevice;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(Intent.ACTION_MEDIA_MOUNTED.equals(action)){
            //android.intent.action.MEDIA_MOUNTED
            AppLogUtils.d( "U盘插入");
        } else if(Intent.ACTION_MEDIA_REMOVED.equals(action)){
            //android.intent.action.MEDIA_REMOVED
            AppLogUtils.d( "无介质");
        } else if (Intent.ACTION_MEDIA_UNMOUNTED.equals(action)) {
            //android.intent.action.MEDIA_UNMOUNTED
            AppLogUtils.d( "U盘移除");
        } else if (UsbReceiver.ACTION_DEVICE_PERMISSION.equals(action)) {
            synchronized (this) {
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (device.getDeviceName().equals(usbDevice.getDeviceName())) {
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        UsbManager usbManager = (UsbManager) AppToolUtils.getApp().getSystemService(Context.USB_SERVICE);
                        //授权成功,在这里进行打开设备操作
                        UsbDeviceConnection usbDeviceConnection = usbManager.openDevice(usbDevice);
                        UsbHelpManager.getInstance().setDeviceConnection(usbDeviceConnection);
                    } else {
                        //授权失败
                    }
                }
            }
        }
    }


}
