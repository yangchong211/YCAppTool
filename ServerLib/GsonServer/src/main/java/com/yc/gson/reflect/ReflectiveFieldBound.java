package com.yc.gson.reflect;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;


/**
 * <pre>
 *     author : yangchong
 *     email  : yangchong211@163.com
 *     time   : 2021/8/11
 *     desc   : 反射字段捆绑类
 *     revise : 参考：{@link com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField}
 * </pre>
 */
public abstract class ReflectiveFieldBound {

    /** 字段名称 */
    private final String mFieldName;
    /** 序列化标记 */
    private final boolean mSerialized;
    /** 反序列化标记 */
    private final boolean mDeserialized;

    public ReflectiveFieldBound(String name, boolean serialized, boolean deserialized) {
        mFieldName = name;
        mSerialized = serialized;
        mDeserialized = deserialized;
    }

    public String getFieldName() {
        return mFieldName;
    }

    public boolean isDeserialized() {
        return mDeserialized;
    }

    public boolean isSerialized() {
        return mSerialized;
    }

    public abstract void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException;

    public abstract void read(JsonReader reader, Object value) throws IOException, IllegalAccessException;

    public abstract boolean writeField(Object value) throws IOException, IllegalAccessException;
}