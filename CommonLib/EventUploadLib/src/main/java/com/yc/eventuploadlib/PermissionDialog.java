package com.yc.eventuploadlib;


import androidx.annotation.NonNull;

/**
 * @author yangchong
 * blog  : yangchong211@163.com
 * time  : 2022/5/23
 * desc  : 隐私说明弹窗帮助类，主要是用于被app依赖的下层库的权限申请说明
 * revise:
 */
public abstract class PermissionDialog {

    protected abstract void showPermissionDialog(int type ,PermissionListener listener);

    private static PermissionDialog permissionDialog;

    public static void setPermissionDialog(@NonNull PermissionDialog dialog) {
        permissionDialog = dialog;
    }

    public static void permissionDialog(int type , PermissionListener listener) {
        if (permissionDialog != null) {
            permissionDialog.showPermissionDialog(type,listener);
        }
    }

    /**
     * 隐私弹窗文案说明回调
     */
    public interface PermissionListener {
        /**
         * 同意
         */
        void dialogClickSure();
        /**
         * 拒绝
         */
        void dialogClickCancel();
    }

}

