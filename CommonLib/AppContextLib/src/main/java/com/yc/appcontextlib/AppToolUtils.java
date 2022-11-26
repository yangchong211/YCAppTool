package com.yc.appcontextlib;

import android.app.Application;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/05/23
 *     desc  : 初始化工具类，获取上下文，会自动初始化
 *     revise:
 * </pre>
 */
public final class AppToolUtils {

    private static Application sApplication;

    public static void init(Application app) {
        if (app == null) {
            throw new NullPointerException("Argument 'app' of type Application (#0 out of 1, zero-based) is marked by @android.support.annotation.NonNull but got null for it");
        } else {
            sApplication = app;
        }
    }

    public static Application getApp() {
        if (sApplication != null) {
            return sApplication;
        } else {
            throw new NullPointerException("u should init first");
        }
    }

}
