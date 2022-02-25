package com.yc.gson.reflect;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.yc.gson.factory.GsonFactory;
import com.yc.gson.listener.JsonListener;

import java.io.IOException;
import java.util.Map;

/**
 * <pre>
 *     author : yangchong
 *     email  : yangchong211@163.com
 *     time   : 2021/8/11
 *     desc   : Object 解析适配器
 *     revise : 参考：{@link com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter}
 * </pre>
 */
public class ReflectiveTypeAdapter<T> extends TypeAdapter<T> {

    private final ObjectConstructor<T> mConstructor;
    private final Map<String, ReflectiveFieldBound> mBoundFields;

    private TypeToken<?> mTypeToken;
    private String mFieldName;

    public ReflectiveTypeAdapter(ObjectConstructor<T> constructor, Map<String, ReflectiveFieldBound> fields) {
        mConstructor = constructor;
        mBoundFields = fields;
    }

    public void setReflectiveType(TypeToken<?> typeToken, String fieldName) {
        mTypeToken = typeToken;
        mFieldName = fieldName;
    }

    @Override
    public T read(JsonReader in) throws IOException {
        JsonToken jsonToken = in.peek();

        if (jsonToken == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        if (jsonToken != JsonToken.BEGIN_OBJECT) {
            in.skipValue();
            JsonListener callback = GsonFactory.getJsonCallback();
            if (callback != null) {
                callback.onTypeException(mTypeToken, mFieldName, jsonToken);
            }
            return null;
        }

        T instance = mConstructor.construct();
        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            ReflectiveFieldBound field = mBoundFields.get(name);
            if (field == null || !field.isDeserialized()) {
                in.skipValue();
                continue;
            }

            JsonToken peek = in.peek();

            try {
                field.read(in, instance);
            } catch (IllegalStateException e) {
                throw new JsonSyntaxException(e);
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            } catch (IllegalArgumentException e) {
                JsonListener callback = GsonFactory.getJsonCallback();
                if (callback != null) {
                    callback.onTypeException(TypeToken.get(instance.getClass()), field.getFieldName(), peek);
                }
            }
        }
        in.endObject();
        return instance;
    }

    @Override
    public void write(JsonWriter out, T value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        out.beginObject();
        for (ReflectiveFieldBound fieldBound : mBoundFields.values()) {
            try {
                if (fieldBound.writeField(value)) {
                    out.name(fieldBound.getFieldName());
                    fieldBound.write(out, value);
                }
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            }
        }
        out.endObject();
    }
}