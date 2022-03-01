package com.yc.businessinterface;

import com.yc.api.route.IRoute;

public interface IUpdateManager extends IRoute {

    /**
     * 检测升级
     * @param updateManagerCallBack                         回调
     */
    void checkUpdate(UpdateManagerCallBack updateManagerCallBack);

    interface UpdateManagerCallBack {
        void updateCallBack(boolean isNeedUpdate);
    }

}
