package com.yc.common.impl;

import com.yc.eventuploadlib.PermissionDialog;

public class PermissionDialogImpl extends PermissionDialog {
    @Override
    protected void showPermissionDialog(int type, PermissionListener listener) {
        if (type ==1 ){
            //弹出读写权限说明弹窗
            listener.dialogClickSure();
        } else if (type == 2){
            //弹出获取相机权限说明弹窗
            listener.dialogClickCancel();
        }
    }
}
