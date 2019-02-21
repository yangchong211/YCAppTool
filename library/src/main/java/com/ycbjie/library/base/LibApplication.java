package com.ycbjie.library.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/05/23
 *     desc  : 可以做一些公共处理逻辑
 *     revise:
 * </pre>
 */
public class LibApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
