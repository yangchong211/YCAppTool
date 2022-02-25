

package com.ycbjie.live.whitelist;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.PowerManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ycbjie.live.alive.YcKeepAlive;
import com.ycbjie.live.whitelist.impl.DefaultWhiteListCallback;
import com.ycbjie.live.whitelist.impl.DefaultWhiteListProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/19
 *     desc  :
 *     revise:
 * </pre>
 */
public final class WhiteList {

    /**
     * 所有白名单跳转意图
     */
    private static List<WhiteListIntentWrapper> sAllWhiteListIntent;
    /**
     * 已匹配合适的白名单跳转意图
     */
    private static List<WhiteListIntentWrapper> sMatchedWhiteListIntent;

    private static IWhiteListProvider sIWhiteListProvider = new DefaultWhiteListProvider();

    /**
     * 设置白名单跳转意图数据提供者
     *
     * @param sIWhiteListProvider
     */
    public static void setIWhiteListProvider(IWhiteListProvider sIWhiteListProvider) {
        WhiteList.sIWhiteListProvider = sIWhiteListProvider;
    }

    /**
     * 获取所有白名单跳转意图
     *
     * @param application
     * @return
     */
    public static List<WhiteListIntentWrapper> getAllWhiteListIntent(Application application) {
        if (sAllWhiteListIntent == null) {
            sAllWhiteListIntent = sIWhiteListProvider.getWhiteList(application);
        }
        return sAllWhiteListIntent;
    }

    /**
     * 获取已匹配合适的白名单跳转意图
     *
     * @return 已匹配合适的白名单跳转意图
     */
    public static List<WhiteListIntentWrapper> getMatchedWhiteListIntent() {
        if (sMatchedWhiteListIntent == null) {
            sMatchedWhiteListIntent = new ArrayList<>();
            List<WhiteListIntentWrapper> intentWrapperList = getAllWhiteListIntent(YcKeepAlive.getApplication());
            for (final WhiteListIntentWrapper intentWrapper : intentWrapperList) {
                //如果本机上没有能处理这个Intent的Activity，说明不是对应的机型，直接忽略进入下一次循环。
                if (!intentWrapper.doesActivityExists()) {
                    continue;
                }

                if (intentWrapper.getType() == IntentType.DOZE) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        PowerManager pm = (PowerManager) YcKeepAlive.getApplication().getSystemService(Context.POWER_SERVICE);
                        if (!pm.isIgnoringBatteryOptimizations(YcKeepAlive.getApplication().getPackageName())) {
                            sMatchedWhiteListIntent.add(intentWrapper);
                        }
                    }
                } else {
                    sMatchedWhiteListIntent.add(intentWrapper);
                }
            }
        }
        return sMatchedWhiteListIntent;
    }


    //==========================跳转到白名单设置界面==============================//
    /**
     * 白名单意图跳转回调
     */
    private static IWhiteListCallback sIWhiteListCallback;

    /**
     * 设置白名单意图跳转回调
     *
     * @param sIWhiteListCallback
     */
    public static void setIWhiteListCallback(IWhiteListCallback sIWhiteListCallback) {
        WhiteList.sIWhiteListCallback = sIWhiteListCallback;
    }

    /**
     * 跳转到设置白名单的页面
     *
     * @param activity
     * @param target
     * @return
     */
    @NonNull
    public static List<WhiteListIntentWrapper> gotoWhiteListActivity(final Activity activity, String target) {
        checkCallback(target);

        List<WhiteListIntentWrapper> matchedWhiteListIntent = getMatchedWhiteListIntent();

        Iterator<WhiteListIntentWrapper> iterator = matchedWhiteListIntent.iterator();
        while (iterator.hasNext()) {
            WhiteListIntentWrapper intentWrapper = iterator.next();
            if (intentWrapper.getType() == IntentType.DOZE) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    PowerManager pm = (PowerManager) YcKeepAlive.getApplication().getSystemService(Context.POWER_SERVICE);
                    if (pm.isIgnoringBatteryOptimizations(YcKeepAlive.getApplication().getPackageName())) {
                        iterator.remove();
                    } else {
                        sIWhiteListCallback.showWhiteList(activity, intentWrapper);
                    }
                }
            } else {
                sIWhiteListCallback.showWhiteList(activity, intentWrapper);
            }
        }
        return matchedWhiteListIntent;
    }

    /**
     * 跳转到设置白名单的页面
     *
     * @param fragment
     * @param target
     * @return
     */
    @NonNull
    public static List<WhiteListIntentWrapper> gotoWhiteListActivity(final Fragment fragment, String target) {
        checkCallback(target);

        List<WhiteListIntentWrapper> matchedWhiteListIntent = getMatchedWhiteListIntent();
        Iterator<WhiteListIntentWrapper> iterator = matchedWhiteListIntent.iterator();
        while (iterator.hasNext()) {
            WhiteListIntentWrapper intentWrapper = iterator.next();
            if (intentWrapper.getType() == IntentType.DOZE) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    PowerManager pm = (PowerManager) YcKeepAlive.getApplication().getSystemService(Context.POWER_SERVICE);
                    if (pm.isIgnoringBatteryOptimizations(YcKeepAlive.getApplication().getPackageName())) {
                        iterator.remove();
                    } else {
                        sIWhiteListCallback.showWhiteList(fragment, intentWrapper);
                    }
                }
            } else {
                sIWhiteListCallback.showWhiteList(fragment, intentWrapper);
            }
        }
        return matchedWhiteListIntent;
    }

    private static void checkCallback(String target) {
        if (sIWhiteListCallback == null) {
            sIWhiteListCallback = new DefaultWhiteListCallback();
        }
        if (target == null) {
            target = "核心服务的持续运行";
        }
        sIWhiteListCallback.init(target, getApplicationName(YcKeepAlive.getApplication()));
    }

    /**
     * 防止华为机型未加入白名单时按返回键回到桌面再锁屏后几秒钟进程被杀
     */
    public static void gotoHome(Activity activity) {
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.addCategory(Intent.CATEGORY_HOME);
        activity.startActivity(launcherIntent);
    }


    /**
     * 获取应用的名称
     *
     * @param application
     * @return
     */
    public static String getApplicationName(Application application) {
        PackageManager packageManager;
        ApplicationInfo info;
        try {
            packageManager = application.getPackageManager();
            info = packageManager.getApplicationInfo(application.getPackageName(), 0);
            return packageManager.getApplicationLabel(info).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return application.getPackageName();
    }

}
