package com.ycbjie.video;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.util.Utils;
import com.ycbjie.library.base.LibApplication;
import com.ycbjie.library.base.config.AppConfig;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/05/23
 *     desc  : 注意，只有从组件library切换到application才会用到
 *     revise:
 * </pre>
 */
public class VideoApp extends LibApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        AppConfig.INSTANCE.initConfig(this);
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
