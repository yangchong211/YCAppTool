package com.ycbjie.library.base.config;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.ycbjie.library.BuildConfig;
import com.ycbjie.library.base.callback.BaseLifecycleCallback;
import com.ycbjie.library.base.callback.LogCallback;
import com.ycbjie.library.constant.Constant;

import cn.ycbjie.ycthreadpoollib.PoolThread;
import io.realm.Realm;
import io.realm.RealmConfiguration;

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

    private boolean isLogin;
    private boolean isShowListImg;
    private boolean isShowGirlImg;
    private boolean isProbabilityShowImg;
    private int thumbnailQuality;
    private String bannerUrl;
    private boolean isNight;
    private PoolThread executor;


    public void initConfig(Application application){
        Utils.init(application);
        initThreadPool();
        ToastUtils.init(application);
        BaseLifecycleCallback.getInstance().init(application);
        initARouter();
        //1.是否是登录状态
        isLogin = SPUtils.getInstance(Constant.SP_NAME).getBoolean(Constant.KEY_IS_LOGIN, false);
        //2.列表是否显示图片
        isShowListImg = SPUtils.getInstance(Constant.SP_NAME).getBoolean(Constant.KEY_IS_SHOW_LIST_IMG, true);
        //3.启动页是否是妹子图
        isShowGirlImg = SPUtils.getInstance(Constant.SP_NAME).getBoolean(Constant.KEY_IS_SHOW_GIRL_IMG, false);
        //4.启动页是否是妹子图
        isProbabilityShowImg = SPUtils.getInstance(Constant.SP_NAME).getBoolean(Constant.KEY_IS_SHOW_IMG_RANDOM, false);
        //5.缩略图质量 0：原图 1：默认 2：省流
        thumbnailQuality = SPUtils.getInstance(Constant.SP_NAME).getInt(Constant.KEY_THUMBNAIL_QUALITY, 1);
        //6.Banner URL 用于加载页显示
        bannerUrl = SPUtils.getInstance(Constant.SP_NAME).getString(Constant.KEY_BANNER_URL, "");
        //7.初始化夜间模式
        isNight = SPUtils.getInstance(Constant.SP_NAME).getBoolean(Constant.KEY_NIGHT_STATE);
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


    private void initARouter(){
        if (BuildConfig.IS_DEBUG) {
            //打印日志
            ARouter.openLog();
            //开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug();
        }
        //推荐在Application中初始化
        ARouter.init(Utils.getApp());
    }


    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        SPUtils.getInstance(Constant.SP_NAME).put(Constant.KEY_IS_LOGIN,login);
        this.isLogin = login;
    }

    public boolean isShowListImg() {
        return isShowListImg;
    }

    public void setShowListImg(boolean showListImg) {
        SPUtils.getInstance(Constant.SP_NAME).put(Constant.KEY_IS_SHOW_LIST_IMG,showListImg);
        this.isShowGirlImg = showListImg;
    }

    public boolean isShowGirlImg() {
        return isShowGirlImg;
    }

    public void setShowGirlImg(boolean showGirlImg) {
        SPUtils.getInstance(Constant.SP_NAME).put(Constant.KEY_IS_SHOW_GIRL_IMG,showGirlImg);
        this.isShowGirlImg = showGirlImg;
    }

    public boolean isProbabilityShowImg() {
        return isProbabilityShowImg;
    }

    public void setProbabilityShowImg(boolean probabilityShowImg) {
        SPUtils.getInstance(Constant.SP_NAME).put(Constant.KEY_IS_SHOW_IMG_RANDOM,probabilityShowImg);
        this.isProbabilityShowImg = probabilityShowImg;
    }

    public int getThumbnailQuality() {
        return thumbnailQuality;
    }

    public void setThumbnailQuality(int thumbnailQuality) {
        SPUtils.getInstance(Constant.SP_NAME).put(Constant.KEY_THUMBNAIL_QUALITY,thumbnailQuality);
        this.thumbnailQuality = thumbnailQuality;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        SPUtils.getInstance(Constant.SP_NAME).put(Constant.KEY_BANNER_URL,bannerUrl);
        this.bannerUrl = bannerUrl;
    }

    public boolean isNight() {
        return isNight;
    }

    public void setNight(boolean night) {
        SPUtils.getInstance(Constant.SP_NAME).put(Constant.KEY_NIGHT_STATE,night);
        this.isNight = night;
    }
}
