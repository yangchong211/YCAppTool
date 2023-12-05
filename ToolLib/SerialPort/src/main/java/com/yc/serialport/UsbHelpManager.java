package com.yc.serialport;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.os.storage.StorageManager;

import com.yc.appcontextlib.AppToolUtils;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class UsbHelpManager {

    private static UsbDeviceConnection deviceConnection;
    private final UsbRequest usbRequest = new UsbRequest();
    private static volatile UsbHelpManager instance;
    public static UsbHelpManager getInstance() {
        if (instance == null) {
            synchronized (UsbHelpManager.class) {
                if (instance == null) {
                    instance = new UsbHelpManager();
                }
            }
        }
        return instance;
    }

    public UsbHelpManager() {

    }



    /**
     * 第一步：发现设备
     *
     * @return 返回设备
     */
    public UsbDevice findDevice() {
        // 通过UsbManager这个系统提供的类，我们可以枚举出当前连接的所有usb设备，我们主要需要的是UsbDevice对象
        UsbManager manager = (UsbManager) AppToolUtils.getApp().getSystemService(Context.USB_SERVICE);
        // 获取连接usb设备列表
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        return deviceList.get("");
    }

    /**
     * 第二步：打开设备
     * 2.1 申请权限
     *
     * @param usbDevice 设备
     */
    public void openDevice(UsbDevice usbDevice) {
        Context context = AppToolUtils.getApp();
        UsbManager usbManager = (UsbManager) AppToolUtils.getApp().getSystemService(Context.USB_SERVICE);
        // 一般来说，在没有定制的android设备上首次访问usb设备的时候
        // 默认我们是没有访问权限的，因此我们首先要判断对当前要打开的usbDevice是否有访问权限
        if (!usbManager.hasPermission(usbDevice)) {
            UsbReceiver usbPermissionReceiver = new UsbReceiver(usbDevice);
            //申请权限
            Intent intent = new Intent(UsbReceiver.ACTION_DEVICE_PERMISSION);
            PendingIntent mPermissionIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            IntentFilter permissionFilter = new IntentFilter(UsbReceiver.ACTION_DEVICE_PERMISSION);
            AppToolUtils.getApp().registerReceiver(usbPermissionReceiver, permissionFilter);
            usbManager.requestPermission(usbDevice, mPermissionIntent);
        } else {
            //真正的打开usb设备，我们需要和usb外设建立一个UsbDeviceConnection
            //理论上平板和usb外设之间的连接已经建立了，也可以首发数据了
            //但是，大部分情况下还需要对usb串口进行一些配置，比如波特率，停止位，数据控制等，不然两边配置不同，收到的数据会乱码。
            //具体怎么配置，就看你使用的串口芯片是什么了，目前流行的有pl2303,ch340等
            deviceConnection = usbManager.openDevice(usbDevice);
        }
    }

    /**
     * 2.2 获得连接口UsbInterface
     * 找到具有数据传输功能的接口UsbInterface，从它里边儿找到数据输入和输出端口UsbEndpoint
     * 一般情况下，一个usbDevice有多个UsbInterface，我们需要的一般是第一个
     *
     * @param device 设备
     * @return 接口
     */
    protected UsbInterface getUsbInterface(UsbDevice device, int index) {
        if (device != null) {
            int count = device.getInterfaceCount();
            if (count >= index) {
                return device.getInterface(index);
            }
        }
        return null;
    }

    /**
     * 2.3 获得连接端口UsbEndpoint
     * 一个usbInterface有多个UsbEndpoint，有控制端口和数据端口等，
     * 因此我们需要根据类型和数据流向来找到我们需要的数据输入和输出两个端口
     *
     * @param usbInterface 连接口
     * @return 端口
     */
    protected UsbEndpoint getUsbEndpoint(UsbInterface usbInterface) {
        UsbEndpoint usbEndpoint = null;
        for (int index = 0; index < usbInterface.getEndpointCount(); index++) {
            UsbEndpoint point = usbInterface.getEndpoint(index);
            if (point.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                if (point.getDirection() == UsbConstants.USB_DIR_IN) {
                    //in 输入
                    usbEndpoint = point;
                } else if (point.getDirection() == UsbConstants.USB_DIR_OUT) {
                    //out 输出
                    usbEndpoint = point;
                }
            }
        }
        /*if (usbInterface != null && usbInterface.getEndpointCount() >= 2) {
            if (inOrOut) {
                return usbInterface.getEndpoint(0);
            } else {
                return usbInterface.getEndpoint(1);
            }
        }*/
        return usbEndpoint;
    }

    /**
     * 数据传输
     * 3.1 发送数据
     *
     * @param device device
     * @param data   data数据
     * @return 状态
     */
    public int sendMessage(UsbDevice device, byte[] data) {
        UsbInterface usbInterface = getUsbInterface(device, 1);
        UsbEndpoint usbEndpoint = getUsbEndpoint(usbInterface);
        //获取了数据的输出端口usbEndpointIn，我们向外设发送数据就是通过这个端口来实现的
        //ulkTransfer这个函数用于在给定的端口进行数据传输
        //第一个参数就是此次传输的端口，这里我们用的输出端口，
        //第二个参数是要发送的数据，类型为字节数组，
        //第三个参数代表要发送的数据长度，
        //第四个参数，是超时
        //返回值代表发送成功的字节数，如果返回-1，那就是发送失败了。
        int ret = deviceConnection.bulkTransfer(usbEndpoint, data, data.length, 60);
        return ret;
    }

    /**
     * 数据传输
     * 3.2 接受usb外设发送来的数据
     * 找到了数据输入端口usbEndpointIn，因为数据的输入是不定时的，因此我们可以另开一个线程，来专门接受数据
     *
     * @param device device
     * @return 数据
     */
    public byte[] receiveMessage(UsbDevice device) {
        byte[] beforeData = null;
        UsbInterface usbInterface = getUsbInterface(device, 1);
        UsbEndpoint inEndpoint = getUsbEndpoint(usbInterface);
        int inMax = inEndpoint.getMaxPacketSize();
        ByteBuffer byteBuffer = ByteBuffer.allocate(inMax);
        usbRequest.initialize(deviceConnection, inEndpoint);
        usbRequest.queue(byteBuffer, inMax);
        if (deviceConnection.requestWait() == usbRequest) {
            beforeData = byteBuffer.array();
        }
        return beforeData;
    }

    /**
     * USB连接状态判断
     *
     * @return true表示连接
     */
    public boolean isUsbConnected() {
        UsbManager manager = (UsbManager) AppToolUtils.getApp().getSystemService(Context.USB_SERVICE);
        // 获取连接usb设备列表
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        for (String key : deviceList.keySet()) {
            UsbDevice usbDevice = deviceList.get(key);
            if (usbDevice != null) {
                for (int i = 0; i < usbDevice.getInterfaceCount(); i++) {
                    UsbInterface usbInterface = usbDevice.getInterface(i);
                    // 获取usb设备类型，判断当前连接的usb设备是否为存储设备（u盘或读卡器）
                    int interfaceClass = usbInterface.getInterfaceClass();
                    if (interfaceClass == UsbConstants.USB_CLASS_MASS_STORAGE) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * USB连接状态判断
     * 该方法的利用反射机制获取优盘挂载状态，但是该方法需要 FW 写入。优点是可以直接拿到 U 盘路径。
     *
     * @return true表示连接
     */
    private boolean checkUsbExist() {
        try {
            StorageManager sm = (StorageManager) AppToolUtils.getApp().getSystemService(Context.STORAGE_SERVICE);
            Method volumeState = (StorageManager.class).getMethod("getVolumeState", String.class);
            //U盘路径
            String path = "";
            try {
                Class<?> cls = Class.forName("android.os.SystemProperties");
                Method m = cls.getDeclaredMethod("get", String.class, String.class);
                m.setAccessible(true);
                path = (String) (m.invoke(null, "vold.udisk_mount_path", ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
            //U盘状态
            String udiskState = (String) volumeState.invoke(sm, path);
            //是否有读写权限
            if (android.os.Environment.MEDIA_MOUNTED.equals(udiskState)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setDeviceConnection(UsbDeviceConnection deviceConnection) {
        UsbHelpManager.deviceConnection = deviceConnection;
    }
}
