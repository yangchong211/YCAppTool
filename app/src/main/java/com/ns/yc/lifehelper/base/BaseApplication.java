package com.ns.yc.lifehelper.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.util.Utils;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.api.LogInterceptor;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.OkHttpClient;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/18
 * 描    述：BaseApplication
 * 修订历史：
 * ================================================
 */
public class BaseApplication extends Application {


    private static BaseApplication instance;
    public synchronized static BaseApplication getInstance() {
        if (null == instance) {
            instance = new BaseApplication();
        }
        return instance;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initUtils();
        initRealm();
        initOkHttpUtils();
    }

    private void initUtils() {
        Utils.init(this);
    }

    /**
     * 初始化数据库
     */
    private Realm realm;
    private void initRealm() {
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration
                .Builder()
                .name(Constant.REALM_NAME)
                .schemaVersion(Constant.REALM_VERSION)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(realmConfig);
    }
    public Realm getRealmHelper() {
        return realm;
    }


    /**
     * 初始化鸿洋大神网络请求框架
     */
    private void initOkHttpUtils() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //.addInterceptor(new BaseInterceptor())
                //.addInterceptor(interceptor)
                .addInterceptor(new LogInterceptor("TAG_APP",true))          //打印请求网络数据日志，上线版本就关掉
                .connectTimeout(50000L, TimeUnit.MILLISECONDS)                  //连接超时时间
                .readTimeout(10000L, TimeUnit.MILLISECONDS)                     //读数据超时时间
                .writeTimeout(10000L,TimeUnit.MICROSECONDS)                     //写数据超时时间
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

}
