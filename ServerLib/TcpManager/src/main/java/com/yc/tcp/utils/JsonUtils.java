package com.yc.tcp.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

/**
 * JSON转换工具
 *
 */
public class JsonUtils {

    /**
     * JSON转对象
     *
     * @param json  JSON字符串
     * @param clazz 对象类型
     * @return 对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

    public static <T> T fromJson(String json, Type type) {

        Gson gson = new Gson();
        T obj = gson.fromJson(json, type);

        return obj;
    }

    /**
     * JSON转对象
     * <p>
     * Type type = new TypeToken<List<类>>() {}.getType();
     *
     * @param json    JSON字符串
     * @param typeOfT 对象类型
     * @return 对象
     */
    public static <T> List<T> fromJsonArray(String json, Type typeOfT) {

        Gson gson = new Gson();

        return gson.fromJson(json, typeOfT);
    }

    /**
     * 对象对JSON
     *
     * @param obj 对象
     * @return json字符串
     */
    public static <T> String toJson(T obj) {
        Gson gson = new Gson();

        return gson.toJson(obj);
    }

}