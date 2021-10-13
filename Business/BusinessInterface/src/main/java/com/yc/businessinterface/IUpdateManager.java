package com.yc.businessinterface;

public interface IUpdateManager {

    /**
     * 检测升级
     * @param updateManagerCallBack                         回调
     */
    void checkUpdate(UpdateManagerCallBack updateManagerCallBack);

    interface UpdateManagerCallBack {
        void updateCallBack(boolean isNeedUpdate);
    }

}
