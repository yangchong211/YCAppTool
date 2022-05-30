package com.yc.gson.element;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.yc.gson.reflect.ReflectiveTypeUtils;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 *    @author yangchong
 *    github : https://github.com/getActivity/GsonFactory
 *    time   : 2020/12/08
 *    desc   : Array 解析适配器，参考：{@link com.google.gson.internal.bind.CollectionTypeAdapterFactory}
 */
/**
 * <pre>
 *     author : yangchong
 *     email  : yangchong211@163.com
 *     time   : 2021/8/11
 *     desc   : Array 解析适配器
 *     revise : 参考：{@link com.google.gson.internal.bind.CollectionTypeAdapterFactory}
 * </pre>
 */
public class CollectionTypeAdapterFactory implements TypeAdapterFactory {

    private final ConstructorConstructor mConstructorConstructor;

    public CollectionTypeAdapterFactory(ConstructorConstructor constructor) {
        mConstructorConstructor = constructor;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Type type = typeToken.getType();
        Class<? super T> rawType = typeToken.getRawType();
        // 判断是否包含这种类型
        if (ReflectiveTypeUtils.containsClass(rawType)) {
            return null;
        }
        if (typeToken.getType() instanceof GenericArrayType ||
                typeToken.getType() instanceof Class &&
                ((Class<?>) typeToken.getType()).isArray()) {
            return null;
        }

        if (!Collection.class.isAssignableFrom(rawType)) {
            return null;
        }

        Type elementType = $Gson$Types.getCollectionElementType(type, rawType);
        TypeAdapter<?> elementTypeAdapter = gson.getAdapter(TypeToken.get(elementType));
        ObjectConstructor<T> constructor = mConstructorConstructor.get(typeToken);

        // create() doesn't define a type parameter
        @SuppressWarnings({"unchecked", "rawtypes"})
        TypeAdapter<T> result = new CollectionTypeAdapter(gson, elementType, elementTypeAdapter, constructor);
        return result;
    }
}