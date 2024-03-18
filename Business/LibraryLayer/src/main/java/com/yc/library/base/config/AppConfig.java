package com.yc.library.base.config;

import android.app.Application;

import com.yc.library.base.callback.LogCallback;

import com.yc.ycthreadpoollib.pool.PoolThread;


/**
 * <pre>
 *     @author      杨充
 *     blog         https://www.jianshu.com/p/53017c3fc75d
 *     time         2017/5/14
 *     desc         所有的配置
 *     revise       生命周期和application一样
 *     GitHub       https://github.com/yangchong211
 * </pre>
 */
public enum AppConfig {

    //对象
    INSTANCE;

    private PoolThread executor;


    public void initConfig(Application application){
        initThreadPool();
    }


    /**
     * 初始化线程池管理器
     */
    private void initThreadPool() {
        // 创建一个独立的实例进行使用
        if (executor==null){
            executor = PoolThread.ThreadBuilder
                    .createFixed(6)
                    .setPriority(Thread.MAX_PRIORITY)
                    .setCallback(new LogCallback())
                    .build();
        }
    }

    /**
     * 获取线程池管理器对象，统一的管理器维护所有的线程池
     * @return                      executor对象
     */
    public PoolThread getExecutor(){
        initThreadPool();
        return executor;
    }

    public void closeExecutor(){
        if(executor!=null){
            executor.close();
            executor = null;
        }
    }
}
