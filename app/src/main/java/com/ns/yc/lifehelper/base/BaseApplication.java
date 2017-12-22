package com.ns.yc.lifehelper.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.service.InitializeService;
import com.ns.yc.lifehelper.utils.AppUtil;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;

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
    //private RefWatcher refWatcher;
    private boolean isInit = false;         //记录环信是否已经初始化，避免初始化两次

    public static synchronized BaseApplication getInstance() {
        if (null == instance) {
            instance = new BaseApplication();
        }
        return instance;
    }

    public BaseApplication(){}

    /*public static BaseApplication getInstance() {
        if (null == instance) {
            synchronized (BaseApplication.class){
                if(instance == null){
                    instance = new BaseApplication();
                }
            }
        }
        return instance;
    }*/


    /**
     * 这个最先执行
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    /**
     * 程序启动的时候执行
     */
    @Override
    public void onCreate() {
        Log.d("Application", "onCreate");
        super.onCreate();
        instance = this;
        initUtils();
        BaseAppManager.getInstance().init(this);        //栈管理
        //initLeakCanary();                               //Square公司内存泄漏检测工具
        initRealm();                                    //初始化Realm数据库
        initChatIM();                                   //初始化环信，不要在子线程中初始化

        //在子线程中初始化
        InitializeService.start(this);
    }


    /**
     * 程序终止的时候执行
     */
    @Override
    public void onTerminate() {
        Log.d("Application", "onTerminate");
        super.onTerminate();
        if(realm!=null){
            realm.close();
            realm = null;
        }
    }


    /**
     * 低内存的时候执行
     */
    @Override
    public void onLowMemory() {
        Log.d("Application", "onLowMemory");
        super.onLowMemory();
    }


    /**
     * HOME键退出应用程序
     * 程序在内存清理的时候执行
     */
    @Override
    public void onTrimMemory(int level) {
        Log.d("Application", "onTrimMemory");
        super.onTrimMemory(level);
    }


    /**
     * onConfigurationChanged
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("Application", "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }


    /**
     * 初始化utils工具类
     */
    private void initUtils() {
        Utils.init(this);
    }


    /**
     * 初始化内存泄漏检测工具
     */
    /*private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        refWatcher = LeakCanary.install(this);
    }*/


    /**
     * 获取RefWatcher对象
     * @param context       上下文
     * @return              RefWatcher对象
     */
    /*public static RefWatcher getRefWatcher(Context context) {
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        return application.refWatcher;
    }*/


    /**
     * 初始化数据库
     */
    private Realm realm;
    private void initRealm() {
        File file ;
        try {
            file = new File(Constant.ExternalStorageDirectory, Constant.DATABASE_FILE_PATH_FOLDER);
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        } catch (Exception e) {
            Log.e("异常",e.getMessage());
        }
        Realm.init(instance);
        RealmConfiguration realmConfig = new RealmConfiguration
                .Builder()
                .name(Constant.REALM_NAME)
                .schemaVersion(Constant.REALM_VERSION)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(realmConfig);
    }


    /**
     * 获取Realm数据库对象
     * @return              realm对象
     */
    public Realm getRealmHelper() {
        return realm;
    }



    /**
     * 初始化环信
     */
    private void initChatIM() {
        // 获取当前进程 id 并取得进程名
        int pid = android.os.Process.myPid();
        String processAppName = AppUtil.getAppName(this , pid);
        /**
         * 如果app启用了远程的service，此application:onCreate会被调用2次
         * 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
         * 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回
         */
        if (processAppName == null || !processAppName.equalsIgnoreCase(this.getPackageName())) {
            // 则此application的onCreate 是被service 调用的，直接返回
            return;
        }
        if (isInit) {
            return;
        }
        // 调用初始化方法初始化sdk
        EMClient.getInstance().init(this, initOptions());
        // 设置开启debug模式
        EMClient.getInstance().setDebugMode(true);
        // 设置初始化已经完成
        isInit = true;
    }


    /**
     * SDK初始化的一些配置
     * 关于 EMOptions 可以参考官方的 API 文档
     * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1chat_1_1_e_m_options.html
     */
    private EMOptions initOptions() {
        EMOptions options = new EMOptions();
        // 设置AppKey，如果配置文件已经配置，这里可以不用设置
         options.setAppKey("1100171221178707#yclifehelper");
        // 设置自动登录
        options.setAutoLogin(true);
        // 设置是否需要发送已读回执
        options.setRequireAck(true);
        // 设置是否需要发送回执，
        options.setRequireDeliveryAck(true);
        // 设置是否需要服务器收到消息确认
        //options.setRequireServerAck(true);
        // 设置是否根据服务器时间排序，默认是true
        options.setSortMessageByServerTime(false);
        // 收到好友申请是否自动同意，如果是自动同意就不会收到好友请求的回调，因为sdk会自动处理，默认为true
        options.setAcceptInvitationAlways(false);
        // 设置是否自动接收加群邀请，如果设置了当收到群邀请会自动同意加入
        options.setAutoAcceptGroupInvitation(false);
        // 设置（主动或被动）退出群组时，是否删除群聊聊天记录
        options.setDeleteMessagesAsExitGroup(false);
        // 设置是否允许聊天室的Owner 离开并删除聊天室的会话
        options.allowChatroomOwnerLeave(true);
        // 设置google GCM推送id，国内可以不用设置
        // options.setGCMNumber(MLConstants.ML_GCM_NUMBER);
        // 设置集成小米推送的appid和appkey
        // options.setMipushConfig(MLConstants.ML_MI_APP_ID, MLConstants.ML_MI_APP_KEY);
        return options;
    }


}


