package com.yc.ycupdatelib;


import androidx.annotation.NonNull;

/**
 * @author yangchong
 * blog  : yangchong211@163.com
 * time  : 2022/5/23
 * desc  : 弹窗
 * revise:
 */
public abstract class PermissionDialog {

    protected abstract void showStorageDialog(PermissionListener listener);

    private static PermissionDialog permissionDialog;

    public static void setPermissionDialog(@NonNull PermissionDialog dialog) {
        permissionDialog = dialog;
    }

    public static void storageDialog(PermissionListener listener) {
        if (permissionDialog != null) {
            permissionDialog.showStorageDialog(listener);
        }
    }

}

