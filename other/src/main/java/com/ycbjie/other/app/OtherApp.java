package com.ycbjie.other.app;

import android.content.Context;

import com.ycbjie.library.base.app.LibApplication;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/05/23
 *     desc  : 注意，只有从组件library切换到application才会用到
 *     revise:
 * </pre>
 */
public class OtherApp extends LibApplication {


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }


}
