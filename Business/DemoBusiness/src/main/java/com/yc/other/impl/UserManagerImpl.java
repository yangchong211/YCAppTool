package com.yc.other.impl;

import com.yc.api.route.RouteImpl;
import com.yc.businessinterface.IUserManager;

@RouteImpl(IUserManager.class)
public class UserManagerImpl implements IUserManager {

    /**
     * 获取用户token
     *
     * @return 返回token
     */
    @Override
    public String getToken() {
        String token = "";
        return token;
    }

    @Override
    public String getUid() {
        return "";
    }

}
