package com.yc.dataclonelib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class BaseParentBean implements Serializable {

    /**
     * 对象深复制，需要实现序列化
     * @return 复制对象
     */
    public <T> T deepCopy() {
        Object obj = null;
        try {
            // 将对象写成 Byte Array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(this);
            out.flush();
            out.close();
            // 从流中读出 byte array，调用readObject函数反序列化出对象
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(inputStream);
            obj = in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (T) obj;
    }

}
