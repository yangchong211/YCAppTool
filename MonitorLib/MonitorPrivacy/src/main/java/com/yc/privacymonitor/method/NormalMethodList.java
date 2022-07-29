package com.yc.privacymonitor.method;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanSettings;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.location.LocationManager;
import android.media.AudioRecord;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.WorkSource;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.SurfaceControl;

import com.yc.privacymonitor.bean.ClassMethodBean;
import com.yc.privacymonitor.bean.MethodBean;
import com.yc.privacymonitor.handler.SettingsResolverImpl;

import java.net.NetworkInterface;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;

public class NormalMethodList implements HookMethodList{

    @Override
    public LinkedList<MethodBean> getMethodList() {
        LinkedList<MethodBean> list = new LinkedList<>();
        //-------设备信息
        list.add(new MethodBean(TelephonyManager.class,"getDeviceId"));
        list.add(new MethodBean(TelephonyManager.class,"getDeviceId",int.class));
        list.add(new MethodBean(TelephonyManager.class,"getSubscriberId",int.class));
        list.add(new MethodBean(TelephonyManager.class,"getImei"));
        list.add(new MethodBean(TelephonyManager.class,"getImei",int.class));
        list.add(new MethodBean(TelephonyManager.class,"getMeid",int.class));
        list.add(new MethodBean(TelephonyManager.class,"getSimSerialNumber",int.class));
        list.add(new MethodBean(TelephonyManager.class,"getSimOperatorNameForPhone",int.class));
        list.add(new MethodBean(TelephonyManager.class,"getSimCountryIsoForPhone",int.class));
        list.add(new MethodBean(TelephonyManager.class,"getTypeAllocationCode",int.class));
        list.add(new MethodBean(TelephonyManager.class,"getManufacturerCode",int.class));
        list.add(new MethodBean(TelephonyManager.class,"getNaiBySubscriberId",int.class));
        list.add(new MethodBean(TelephonyManager.class,"getPhoneTypeFromNetworkType",int.class));
        list.add(new MethodBean(TelephonyManager.class,"getTelephonyProperty",int.class,String.class,String.class));
        list.add(new MethodBean(TelephonyManager.class,"getTelephonyProperty",String.class,String.class));
        list.add(new MethodBean(Build.class,"getSerial"));
        //-----MacAddress
        list.add(new MethodBean(WifiInfo.class,"getMacAddress"));
        list.add(new MethodBean(NetworkInterface.class,"getHardwareAddress"));
        //-----ssid
        list.add(new MethodBean(WifiInfo.class,"getSSID"));
        list.add(new MethodBean(NetworkInfo.class,"getExtraInfo"));


        MethodBean s1 = new MethodBean(Settings.Secure.class,"getString", ContentResolver.class,String.class);
        s1.setMethodHandler(new SettingsResolverImpl());
        list.add(s1);
        MethodBean s2 = new MethodBean(Settings.System.class,"getString", ContentResolver.class,String.class);
        s2.setMethodHandler(new SettingsResolverImpl());
        list.add(s2);
        //-----定位
        list.add(new MethodBean(LocationManager.class,"getLastLocation"));
        list.add(new MethodBean(LocationManager.class,"getLastKnownLocation",String.class));
        //--屏幕
        list.add(new MethodBean(MediaRecorder.class,"start"));
        list.add(new MethodBean(MediaRecorder.class,"prepare"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            list.add(new MethodBean(ImageReader.class,"acquireLatestImage"));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            list.add(new MethodBean(SurfaceControl.class,"screenshot", Rect.class,Integer.TYPE,Integer.TYPE,Boolean.TYPE,Integer.TYPE));
        }
        //--网络
        list.add(new MethodBean(WifiManager.class,"setWifiEnabled",boolean.class));
        list.add(new MethodBean(WifiManager.class,"isWifiEnabled"));
        list.add(new MethodBean(WifiManager.class,"startScan"));
        list.add(new MethodBean(NetworkInterface.class,"getNetworkInterfaces"));
        //--系统特殊处理
//        MethodWrapper contentResolverWrap = new MethodWrapper(ContentResolver.class,"query", Uri.class,String[].class,String.class,String[].class,String.class);
//        contentResolverWrap.setMethodHandler(new ContentResolverHandler());
//        list.add(contentResolverWrap);
        //--蓝牙
        list.add(new MethodBean(BluetoothAdapter.class,"enable"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            list.add(new MethodBean(BluetoothAdapter.class,"startLeScan", UUID[].class, BluetoothAdapter.LeScanCallback.class));
        }
        list.add(new MethodBean(BluetoothDevice.class,"createBond"));
        list.add(new MethodBean(BluetoothAdapter.class,"getProfileConnectionState",int.class));
        list.add(new MethodBean(BluetoothAdapter.class,"getBondedDevices"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            list.add(new MethodBean(BluetoothLeScanner.class,"startScan", List.class, ScanSettings.class, WorkSource.class, ScanCallback.class, PendingIntent.class,List.class));
        }
        //--录音
        list.add(new MethodBean(AudioRecord.class,"startRecording"));
        list.add(new MethodBean(MediaRecorder.class,"start"));
        list.add(new MethodBean(MediaRecorder.class,"prepare"));
        //--拍照
        list.add(new MethodBean(Camera.class,"startPreview"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            list.add(new MethodBean(CameraManager.class,"openCameraDeviceUserAsync",String.class, CameraDevice.StateCallback.class, Executor.class,int.class));
        }
        //--剪贴板
        list.add(new MethodBean(ClipboardManager.class,"getPrimaryClip"));
        list.add(new MethodBean(ClipboardManager.class,"getText"));
        //list.add(new MethodBean(ClipboardManager.class,"setText",CharSequence.class));
        //list.add(new MethodBean(ClipboardManager.class,"setPrimaryClip", ClipData.class));
        list.add(new MethodBean(ClipData.class,"getItemAt",int.class));

        //--获取安装列表
        list.add(new MethodBean(PackageManager.class,"getInstalledPackages"));
        list.add(new MethodBean(PackageManager.class,"queryIntentActivities"));
        list.add(new MethodBean(PackageManager.class,"queryIntentActivityOptions"));
        list.add(new MethodBean(PackageManager.class,"queryIntentActivitiesAsUser"));
        list.add(new MethodBean(PackageManager.class,"getInstalledPackagesAsUser"));
        return list;
    }

    @Override
    public LinkedList<ClassMethodBean> getAbsMethodList() {
        LinkedList<ClassMethodBean> list = new LinkedList<>();
        //PackageManager
        ClassMethodBean pkgManagerHook = new ClassMethodBean("android.app.ApplicationPackageManager");
        pkgManagerHook.addMethod("getInstalledPackagesAsUser");
        pkgManagerHook.addMethod("getInstalledApplicationsAsUser");
        list.add(pkgManagerHook);
        //SmsManager    短信
        ClassMethodBean smsManagerHook = new ClassMethodBean("android.telephony.SmsManager");
        smsManagerHook.addMethod("sendTextMessageInternal");
        smsManagerHook.addMethod("getDefault");
        list.add(smsManagerHook);
        //runtime
        ClassMethodBean runtimeHook = new ClassMethodBean("java.lang.Runtime");
        runtimeHook.addMethod("exec");
        list.add(runtimeHook);
        return list;
    }
}

