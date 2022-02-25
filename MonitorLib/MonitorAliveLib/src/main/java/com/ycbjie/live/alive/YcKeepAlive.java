
package com.ycbjie.live.alive;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ycbjie.live.config.ForegroundNotification;
import com.ycbjie.live.config.KeepLiveService;
import com.ycbjie.live.service.HideForegroundService;
import com.ycbjie.live.service.JobHandlerService;
import com.ycbjie.live.service.LocalService;
import com.ycbjie.live.service.RemoteService;
import com.ycbjie.live.utils.ServiceUtils;
import com.ycbjie.live.whitelist.IWhiteListCallback;
import com.ycbjie.live.whitelist.IWhiteListProvider;
import com.ycbjie.live.whitelist.WhiteList;
import com.ycbjie.live.whitelist.WhiteListIntentWrapper;

import java.util.List;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/19
 *     desc  : 保活工具
 *     revise:
 * </pre>
 */
public final class YcKeepAlive {

    /**
     * 运行模式
     */
    public enum RunMode {
        /**
         * 省电模式
         * 省电一些，但保活效果会差一点
         */
        ENERGY,
        /**
         * 流氓模式
         * 相对耗电，但可造就不死之身
         */
        ROGUE
    }

    private static Application sApplication;
    public static ForegroundNotification sForegroundNotification = null;
    public static KeepLiveService sKeepLiveService = null;
    public static RunMode sRunMode = null;
    /**
     * 是否使用静态音乐形式保活
     */
    public static boolean sUseSilenceMusic = true;

    /**
     * 启动保活
     *
     * @param application            your application
     * @param foregroundNotification 前台服务 必须要，安卓8.0后必须有前台通知才能正常启动Service
     * @param keepLiveService        保活业务
     */
    public static void startWork(@NonNull Application application, @NonNull RunMode runMode,
                                 @NonNull ForegroundNotification foregroundNotification,
                                 @NonNull KeepLiveService keepLiveService) {
        if (ServiceUtils.isMainProcess(application)) {
            YcKeepAlive.sApplication = application;
            YcKeepAlive.sForegroundNotification = foregroundNotification;
            YcKeepAlive.sKeepLiveService = keepLiveService;
            YcKeepAlive.sRunMode = runMode;
            //大于21，也就是Android 5.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //启动定时器，在定时器中启动本地服务和守护进程
                Intent intent = new Intent(application, JobHandlerService.class);
                //大于26，也就是Android 8.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    application.startForegroundService(intent);
                } else {
                    //小于26，也就是在5.0以上，8.0以下
                    application.startService(intent);
                }
            } else {
                //Android 5.0以下
                //开启双进程保活策略
                startDoubleProcessService(application);
            }
        }
    }

    /**
     * 停止保活
     */
    public static void stopWork() {
        stopWork(getApplication());
    }

    /**
     * 停止保活
     *
     * @param context
     */
    public static void stopWork(@NonNull Context context) {
        if (YcKeepAlive.sForegroundNotification != null && YcKeepAlive.sForegroundNotification.isShow()) {
            context.startService(new Intent(context, HideForegroundService.class));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //停止定时器，在定时器中启动本地服务和守护进程
            JobHandlerService.stop(context);
        } else {
            //停止双进程保活策略
            stopDoubleProcessService(context);
        }
    }

    /**
     * 开启双进程保活策略
     * @param context                   上下文
     */
    public static void startDoubleProcessService(@NonNull Context context) {
        //启动本地服务
        Intent localIntent = new Intent(context, LocalService.class);
        //启动守护进程
        Intent guardIntent = new Intent(context, RemoteService.class);
        context.startService(localIntent);
        context.startService(guardIntent);
    }

    /**
     * 停止双进程保活策略
     * @param context                   上下文
     */
    public static void stopDoubleProcessService(@NonNull Context context) {
        //停止本地服务
        Intent localIntent = new Intent(context, LocalService.class);
        //停止守护进程
        Intent guardIntent = new Intent(context, RemoteService.class);
        context.stopService(localIntent);
        context.stopService(guardIntent);
    }

    public static Application getApplication() {
        if (sApplication == null) {
            throw new ExceptionInInitializerError("请先在全局Application中调用 YcKeepAlive.startWork() 进行初始化！");
        }
        return sApplication;
    }

    /**
     * 是否启用无声音乐
     * 如不设置，则默认启用
     *
     * @param enable
     */
    public static void useSilenceMusice(boolean enable) {
        YcKeepAlive.sUseSilenceMusic = enable;
    }

    //======================================白名单=============================================//

    /**
     * 获取已匹配合适的白名单跳转意图
     *
     * @return 已匹配合适的白名单跳转意图
     */
    public static List<WhiteListIntentWrapper> getMatchedWhiteListIntent() {
        return WhiteList.getMatchedWhiteListIntent();
    }

    /**
     * 跳转到设置白名单的页面
     *
     * @param fragment
     * @param target
     * @return
     */
    public static List<WhiteListIntentWrapper> gotoWhiteListActivity(
            final Fragment fragment, String target) {
        return WhiteList.gotoWhiteListActivity(fragment, target);
    }

    /**
     * 跳转到设置白名单的页面
     *
     * @param activity
     * @param target
     * @return
     */
    public static List<WhiteListIntentWrapper> gotoWhiteListActivity(
            final Activity activity, String target) {
        return WhiteList.gotoWhiteListActivity(activity, target);
    }

    /**
     * 设置白名单意图跳转回调
     *
     * @param sIWhiteListCallback
     */
    public static void setIWhiteListCallback(IWhiteListCallback sIWhiteListCallback) {
        WhiteList.setIWhiteListCallback(sIWhiteListCallback);
    }

    /**
     * 设置白名单跳转意图数据提供者
     *
     * @param sIWhiteListProvider
     */
    public static void setIWhiteListProvider(IWhiteListProvider sIWhiteListProvider) {
        WhiteList.setIWhiteListProvider(sIWhiteListProvider);
    }

}
