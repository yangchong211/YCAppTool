package com.yc.gson.listener;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonToken;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2021/8/11
 *     desc   : 解析异常监听器
 *     revise :
 * </pre>
 */
public interface JsonListener {

    /**
     * 类型解析异常
     *
     * @param typeToken             类型 Token
     * @param fieldName             字段名称
     * @param jsonToken             后台给定的类型
     */
    void onTypeException(TypeToken<?> typeToken, String fieldName, JsonToken jsonToken);
}