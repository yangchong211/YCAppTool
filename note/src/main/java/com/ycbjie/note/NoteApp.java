package com.ycbjie.note;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.Utils;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/05/23
 *     desc  : 注意，只有从组件library切换到application才会用到
 *     revise:
 * </pre>
 */
public class NoteApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        initARouter();
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initARouter(){
        //打印日志
        ARouter.openLog();
        //开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.openDebug();
        //推荐在Application中初始化
        ARouter.init(this);
    }


}
