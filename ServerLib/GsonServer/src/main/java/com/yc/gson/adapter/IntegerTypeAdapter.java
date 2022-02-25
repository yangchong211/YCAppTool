package com.yc.gson.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.math.BigDecimal;


/**
 * <pre>
 *     author : yangchong
 *     email  : yangchong211@163.com
 *     time   : 2021/8/11
 *     desc   : Integer 类型解析适配器
 *     revise : 参考：{@link com.google.gson.internal.bind.TypeAdapters#INTEGER}
 * </pre>
 */
public class IntegerTypeAdapter extends TypeAdapter<Integer> {

    @Override
    public Integer read(JsonReader in) throws IOException {
        switch (in.peek()) {
            case NUMBER:
                try {
                    return in.nextInt();
                } catch (NumberFormatException e) {
                    // 如果带小数点则会抛出这个异常
                    return (int) in.nextDouble();
                }
            case STRING:
                String result = in.nextString();
                if (result == null || "".equals(result)) {
                    return 0;
                }
                try {
                    return Integer.parseInt(result);
                } catch (NumberFormatException e) {
                    // 如果带小数点则会抛出这个异常
                    return (int) new BigDecimal(result).floatValue();
                }
            case NULL:
                in.nextNull();
                return null;
            default:
                in.skipValue();
                throw new IllegalArgumentException();
        }
    }

    @Override
    public void write(JsonWriter out, Integer value) throws IOException {
        out.value(value);
    }
}