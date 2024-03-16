package com.yc.appcommoninter.report;


import androidx.annotation.NonNull;

/**
 * @author yangchong
 * blog  : yangchong211@163.com
 * time  : 2022/5/23
 * desc  : 隐私说明弹窗帮助类，主要是用于被app依赖的下层库的权限申请说明
 * revise:
 */
public abstract class PermissionReporter {

    protected abstract void showPermissionDialog(int type ,PermissionListener listener);

    private static PermissionReporter permissionDialog;

    public static void setPermissionDialog(@NonNull PermissionReporter dialog) {
        permissionDialog = dialog;
    }

    /**
     * 弹出说明文案弹窗
     * @param type                  这个type主要是区分是什么隐私权限的弹窗
     * @param listener              这个是弹窗回调
     */
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

