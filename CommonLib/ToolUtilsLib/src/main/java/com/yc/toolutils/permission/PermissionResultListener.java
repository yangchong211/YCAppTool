package com.yc.toolutils.permission;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCAppTool
 *     email  : yangchong211@163.com
 *     time  : 2016/09/23
 *     desc  : 权限申请结果的回调接口
 *     revise:
 * </pre>
 */
public interface PermissionResultListener {

    /**
     * 申请成功
     *
     * @param requestCode code
     */
    void onSuccess(final int requestCode);

    /**
     * 拒绝的权限集合(不在弹框提醒)
     *
     * @param deniedList 拒绝权限集合
     */
    void onDenied(@NonNull List<String> deniedList);

    /**
     * 取消的权限集合
     *
     * @param cancelList 取消权限集合
     */
    void onCancel(@NonNull List<String> cancelList);

}
