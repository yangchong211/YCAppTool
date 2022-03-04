package com.yc.alive.constant;

import androidx.annotation.IntDef;
import androidx.annotation.RestrictTo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;


/**
 * 错误常量
 */
@RestrictTo(LIBRARY)
public class AliveErrorConst {

    // 页面打开失败
    public static final int ERROR_CODE_OPEN_FAIL = 2000;

    // 无权限
    public static final int ERROR_CODE_NO_PERMISSION = 3000;

    // 查找节点失败
    public static final int ERROR_CODE_FIND_NODE = 4000;

    @IntDef({ ERROR_CODE_OPEN_FAIL, ERROR_CODE_NO_PERMISSION, ERROR_CODE_FIND_NODE })
    @Retention(RetentionPolicy.SOURCE)
    public @interface CODE {
    }

    public static String getMessage(@CODE int code) {
        String message;
        switch (code) {
            case ERROR_CODE_OPEN_FAIL:
                message = "open fail";
                break;
            case ERROR_CODE_NO_PERMISSION:
                message = "no permission";
                break;
            case ERROR_CODE_FIND_NODE:
                message = "no node";
                break;
            default:
                message = "error";
                break;
        }
        return message;
    }
}
