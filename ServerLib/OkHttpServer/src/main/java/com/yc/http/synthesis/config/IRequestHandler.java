package com.yc.http.synthesis.config;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yc.http.synthesis.EasyUtils;
import com.yc.http.synthesis.request.HttpRequest;

import java.lang.reflect.Type;

import okhttp3.Response;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2019/11/25
 *    desc   : 请求处理器
 */
public interface IRequestHandler {

    /**
     * 请求成功时回调
     *
     * @param httpRequest   请求接口对象
     * @param response      响应对象
     * @param type          解析类型
     * @return              返回结果
     *
     * @throws Exception    如果抛出则回调失败
     */
    @NonNull
    Object requestSucceed(@NonNull HttpRequest<?> httpRequest, @NonNull Response response, @NonNull Type type) throws Exception;

    /**
     * 请求失败
     *
     * @param httpRequest   请求接口对象
     * @param e             错误对象
     * @return              错误对象
     */
    @NonNull
    Exception requestFail(@NonNull HttpRequest<?> httpRequest, @NonNull Exception e);

    /**
     * 读取缓存
     *
     * @param httpRequest   请求接口对象
     * @param cacheTime     缓存的有效期（以毫秒为单位）
     * @return              返回新的请求对象
     */
    @Nullable
    default Object readCache(@NonNull HttpRequest<?> httpRequest, @NonNull Type type, long cacheTime) {
        return null;
    }

    /**
     * 写入缓存
     *
     * @param httpRequest   请求接口对象
     * @param result        请求结果对象
     * @return              缓存写入结果
     */
    default boolean writeCache(@NonNull HttpRequest<?> httpRequest, @NonNull Response response, @NonNull Object result) {
        return false;
    }

    /**
     * 清空缓存
     */
    default void clearCache() {}

    /**
     * 解析泛型
     */
    default Type getType(Object object) {
        return EasyUtils.getGenericType(object);
    }
}