package com.yc.baseclasslib.provider;

import android.content.ContentProvider;
import android.content.Context;
import android.content.pm.ProviderInfo;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time  : 2018/11/9
 *     desc  : 父类ContentProvider
 *     revise:
 * </pre>
 */
public abstract class BaseContentProvider extends ContentProvider {

    /**
     * ContentProvider初始化流程---->
     * ActivityThread.attach
     * ActivityManagerService.attachApplication
     * ApplicationThread.bindApplication
     * ActivityThread#mH.handleMessage
     * ActivityThread#handleBindApplication
     *
     * 最后在ActivityThread#handleBindApplication方法中做了几步核心操作---->
     * 1.makeApplication创建application中会执行attachBaseContext(context);
     * 2.installContentProviders方法中遍历providers列表，初始化每一个provider，都是用application的context。构造出ContentProvider然后执行attachInfo() 方法，attachInfo()执行完毕会执行onCreate()。
     * 3.最后再mInstrumentation.callApplicationOnCreate(app);执行Application的OnCreate方法。
     */

    /**
     * 实例化之后，将调用该函数来告诉内容提供者关于自身的信息。
     * @param context       上下文
     * @param info          info
     */
    @Override
    public void attachInfo(Context context, ProviderInfo info) {
        super.attachInfo(context, info);
    }

    /**
     * 如果成功加载提供程序，则为True，否则为false
     * @return          true
     */
    @Override
    public boolean onCreate() {
        return false;
    }
}
