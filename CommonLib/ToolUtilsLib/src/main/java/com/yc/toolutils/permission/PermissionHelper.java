package com.yc.toolutils.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCAppTool
 *     email  : yangchong211@163.com
 *     time  : 2016/09/23
 *     desc  : 权限 相关工具类
 *     revise:
 * </pre>
 */
public final class PermissionHelper {

    private PermissionResultListener permissionResultListener;

    private PermissionHelper() {
    }

    /**
     * 获取单例对象
     *
     * @return 对象
     */
    public static PermissionHelper getInstance() {
        return PermissionHolder.INSTANCE_PERMISSION;
    }

    private static class PermissionHolder {
        private static final PermissionHelper INSTANCE_PERMISSION = new PermissionHelper();
    }

    /**
     * 判断当前应用是否有指定权限，运行时权限的检测
     */
    public boolean hasPermissions(Context context, String[] permissions) {
        if (permissions == null || permissions.length == 0) {
            return true;
        }
        boolean ifSdk = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
        for (String permission : permissions) {
            if (ifSdk && !hasPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasPermission(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 动态申请指定权限，配合hasPermission使用,注意在使用的activity中调用onRequestPermissionsResult权限申请结果的回调
     *
     * @param activity    上下文
     * @param permissions 权限组
     * @param requestCode 请求code
     */
    public void requestPermission(final Activity activity, final String[] permissions, final int requestCode, final PermissionResultListener permissionResultListener) {
        this.permissionResultListener = permissionResultListener;
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * 申请权限的结果回调，需要在Activity的onRequestPermissionsResult中调用
     *
     * @param grantResults 回调
     */
    public void onPermissionResult(Activity activity, final int requestCode, String[] permissions, int[] grantResults) {
        boolean hasPermission = true;
        List<String> deniedList = new ArrayList<>();
        List<String> cancelList = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            boolean isAllow = grantResults[i] == PackageManager.PERMISSION_GRANTED;
            hasPermission &= isAllow;
            if (!isAllow) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || !activity.shouldShowRequestPermissionRationale(permissions[i])) {
                    deniedList.add(permissions[i]);
                } else {
                    cancelList.add(permissions[i]);
                }
            }
        }
        if (permissionResultListener != null) {
            if (hasPermission) {
                permissionResultListener.onSuccess(requestCode);
            } else {
                if (deniedList.size() > 0) {
                    permissionResultListener.onDenied(deniedList);
                }
                if (cancelList.size() > 0) {
                    permissionResultListener.onCancel(cancelList);
                }
            }
        }
    }

}
