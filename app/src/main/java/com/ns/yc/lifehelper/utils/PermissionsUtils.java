package com.ns.yc.lifehelper.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by PC on 2017/4/18.
 * 权限检查工具类
 */

public class PermissionsUtils {

    /**检测存储权限*/
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    /**检测相机权限*/
    private static final int REQUEST_EXTERNAL_CAMERO = 0;
    private static String[] PERMISSIONS_CAMERO = {
            Manifest.permission.CAMERA
    };
    public static void verifyCameraPermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_CAMERO, REQUEST_EXTERNAL_CAMERO);
        }
    }

    /**检测读取网络状态权限*/
    private static final int REQUEST_EXTERNAL_POHNE = 2;
    private static String[] PERMISSIONS_POHNE = {
            Manifest.permission.READ_PHONE_STATE
    };
    public static void verifyPhonePermissions(Activity activity){
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_POHNE, REQUEST_EXTERNAL_POHNE);
        }
    }

    /**动态注册获取本地图片权限*/
    private static final int MY_PERMISSIONS_REQUEST_GET_PHOTO = 3;
    public static boolean registerManifestPhoto(Activity activity) {
        // 检查是否获得了权限（Android6.0运行时权限）
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            // 没有获得授权，申请授权
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // 弹窗需要解释为何需要该权限，再次请求授权
                Toast.makeText(activity, "请授权！", Toast.LENGTH_LONG).show();
                // 帮跳转到该应用的设置界面，让用户手动授权
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivity(intent);
            }else{
                // 不需要解释为何需要该权限，直接请求授权
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_GET_PHOTO);
            }
            return false;
        }else {
            // 已经获得授权，获取图片
            return true;
        }
    }

    /**动态注册拍照权限*/
    private static final int MY_PERMISSIONS_REQUEST_GET_CAMEAM = 4;
    public static boolean registerManifestCam(Activity activity) {
        // 检查是否获得了权限（Android6.0运行时权限）
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            // 没有获得授权，申请授权
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // 弹窗需要解释为何需要该权限，再次请求授权
                Toast.makeText(activity, "请授权！", Toast.LENGTH_LONG).show();
                // 帮跳转到该应用的设置界面，让用户手动授权
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivity(intent);
            }else{
                // 不需要解释为何需要该权限，直接请求授权
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_GET_CAMEAM);
            }
            return false;
        }else {
            // 已经获得授权，获取图片
            return true;
        }
    }

    /**检测打电话权限*/
    private static final int REQUEST_EXTERNAL_CALL = 5;
    private static String[] PERMISSIONS_CALL = {
            Manifest.permission.CALL_PHONE
    };
    public static void verifyCallPermissions(Activity activity){
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_CALL, REQUEST_EXTERNAL_CALL);
        }
    }


}
