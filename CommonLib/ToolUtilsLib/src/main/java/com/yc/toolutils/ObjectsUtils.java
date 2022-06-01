package com.yc.toolutils;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/21
 *     desc  : Objects工具类
 *     revise:
 * </pre>
 */
public final class ObjectsUtils {

    public static <T> T requireNonNull(T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    public static <T> T requireNonNull(T obj , String message) {
        if (obj == null) {
            throw new NullPointerException(message);
        }
        return obj;
    }

}
