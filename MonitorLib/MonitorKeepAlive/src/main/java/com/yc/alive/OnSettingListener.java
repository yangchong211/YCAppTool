package com.yc.alive;

import com.yc.alive.constant.AliveSettingType.TYPE;

/**
 * 设置监听
 */
public interface OnSettingListener {

    /**
     * 设置成功回调
     *
     * @param type 设置类型
     */
    void onSuccess(@TYPE int type);

    /**
     * 所有设置完成
     */
    void onAllCompleted();

    /**
     * 设置失败回调
     *
     * @param type    设置类型
     * @param message 错误信息
     */
    void onFailure(@TYPE int type, String message);
}
