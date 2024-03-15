package com.yc.toolutils.clone;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class CloneDataUtils {

    /**
     * 对象深复制，需要实现序列化
     *
     * @param from 源
     * @param <T>  类
     * @return 复制对象
     */
    public static <T> T deepCopy(T from) {
        if (from == null) {
            return null;
        }
        Object obj = null;
        try {
            // 将对象写成 Byte Array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(from);
            out.flush();
            out.close();
            // 从流中读出 byte array，调用readObject函数反序列化出对象
            byte[] bytes = bos.toByteArray();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream in = new ObjectInputStream(inputStream);
            obj = in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return (T) obj;
    }

}
