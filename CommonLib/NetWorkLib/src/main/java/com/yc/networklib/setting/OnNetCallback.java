package com.yc.networklib.setting;

public interface OnNetCallback {
    /**
     * 网络状态监听
     *
     * @param connect 是否连接上
     * @param netType 网络类型
     */
    void onChange(boolean connect, String netType);

    /**
     * 默认网络监听回调
     *
     * @param available 是否可用
     * @param netType   网络类型
     */
    void onDefaultChange(boolean available, String netType);
}
