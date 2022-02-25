package com.yc.gson.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;


/**
 * <pre>
 *     author : yangchong
 *     email  : yangchong211@163.com
 *     time   : 2021/8/11
 *     desc   : Float 类型解析适配器
 *     revise : 参考：{@link com.google.gson.internal.bind.TypeAdapters#FLOAT}
 * </pre>
 */
public class FloatTypeAdapter extends TypeAdapter<Float> {

    @Override
    public Float read(JsonReader in) throws IOException {
        switch (in.peek()) {
            case NUMBER:
                return (float) in.nextDouble();
            case STRING:
                String result = in.nextString();
                if (result == null || "".equals(result)) {
                    return 0F;
                }
                return Float.parseFloat(result);
            case NULL:
                in.nextNull();
                return null;
            default:
                in.skipValue();
                throw new IllegalArgumentException();
        }
    }

    @Override
    public void write(JsonWriter out, Float value) throws IOException {
        out.value(value);
    }
}