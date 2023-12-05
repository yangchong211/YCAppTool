package com.yc.usbsocket;

import com.yc.dataclonelib.BaseParentBean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class BaseBean extends BaseParentBean {

    /**
     * 转json字符串
     * @return json字符串
     */
    public abstract String toJsonString();

}
