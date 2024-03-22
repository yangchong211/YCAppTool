package com.yc.socket.easy;

import com.yc.toolutils.clone.BaseParentBean;

public abstract class BaseBean extends BaseParentBean {

    /**
     * 转json字符串
     * @return json字符串
     */
    public abstract String toJsonString();

}
