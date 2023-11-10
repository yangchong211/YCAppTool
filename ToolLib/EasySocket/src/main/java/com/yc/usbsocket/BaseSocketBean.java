package com.yc.usbsocket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class BaseSocketBean implements Serializable {

    /**
     * 转json字符串
     * @return json字符串
     */
    public abstract String toJsonString();

    /**
     * 对象深复制，需要实现序列化
     * @param from 源
     * @param <T> 类
     * @return 复制对象
     */
    public static <T> T deepCopy(T from) {
        Object obj = null;
        try {
            // 将对象写成 Byte Array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(from);
            out.flush();
            out.close();
            // 从流中读出 byte array，调用readObject函数反序列化出对象
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
            obj = in.readObject();
        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (T)obj;
    }
}
