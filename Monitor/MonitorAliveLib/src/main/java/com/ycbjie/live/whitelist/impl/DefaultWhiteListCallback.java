
package com.ycbjie.live.whitelist.impl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ycbjie.live.whitelist.IWhiteListCallback;
import com.ycbjie.live.whitelist.IntentType;
import com.ycbjie.live.whitelist.WhiteListIntentWrapper;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/19
 *     desc  : 默认白名单跳转回调
 *     revise:
 * </pre>
 */
public class DefaultWhiteListCallback implements IWhiteListCallback {

    protected String mTarget;
    protected String mAppName;

    /**
     * 初始化
     *
     * @param target  需要加入白名单的目标
     * @param appName 需要处理白名单的应用名
     */
    @Override
    public void init(@NonNull String target, @NonNull String appName) {
        mTarget = target;
        mAppName = appName;
    }

    /**
     * 显示白名单
     *
     * @param activity
     * @param intentWrapper
     */
    @Override
    public void showWhiteList(@NonNull final Activity activity, final @NonNull WhiteListIntentWrapper intentWrapper) {
        showWhiteList(intentWrapper, activity, null);
    }

    /**
     * 显示白名单
     *
     * @param fragment
     * @param intentWrapper
     */
    @Override
    public void showWhiteList(@NonNull Fragment fragment, @NonNull WhiteListIntentWrapper intentWrapper) {
        showWhiteList(intentWrapper, fragment.getActivity(), fragment);
    }

    private void showWhiteList(final @NonNull WhiteListIntentWrapper intentWrapper, final Activity activity, final Fragment fragment) {
        if (activity==null){
            return;
        }
        switch(intentWrapper.getType()) {
            case IntentType.DOZE:
                new AlertDialog.Builder(activity)
                        .setCancelable(false)
                        .setTitle("需要忽略 " + mAppName + " 的电池优化")
                        .setMessage(mTarget + "需要 " + mAppName + " 加入到电池优化的忽略名单。\n\n" +
                                "请点击『确定』，在弹出的『忽略电池优化』对话框中，选择『是』。")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int w) {
                                intentWrapper.startActivitySafely(activity, fragment);
                            }
                        })
                        .show();
                break;
            case IntentType.HUAWEI:
                new AlertDialog.Builder(activity)
                        .setCancelable(false)
                        .setTitle("需要允许 " + mAppName + " 自动启动")
                        .setMessage(mTarget + "需要允许 " + mAppName + " 的自动启动。\n\n" +
                                "请点击『确定』，在弹出的『自启管理』中，将 " + mAppName + " 对应的开关打开。")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int w) {
                                intentWrapper.startActivitySafely(activity, fragment);
                            }
                        })
                        .show();
                break;
            case IntentType.ZTE_GOD:
            case IntentType.HUAWEI_GOD:
                new AlertDialog.Builder(activity)
                        .setCancelable(false)
                        .setTitle(mAppName + " 需要加入锁屏清理白名单")
                        .setMessage(mTarget + "需要 " + mAppName + " 加入到锁屏清理白名单。\n\n" +
                                "请点击『确定』，在弹出的『锁屏清理』列表中，将 " + mAppName + " 对应的开关打开。")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int w) {
                                intentWrapper.startActivitySafely(activity, fragment);
                            }
                        })
                        .show();
                break;
            case IntentType.XIAOMI_GOD:
                new AlertDialog.Builder(activity)
                        .setCancelable(false)
                        .setTitle("需要关闭 " + mAppName + " 的神隐模式")
                        .setMessage(mTarget + "需要关闭 " + mAppName + " 的神隐模式。\n\n" +
                                "请点击『确定』，在弹出的 " + mAppName + " 神隐模式设置中，选择『无限制』，然后选择『允许定位』。")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int w) {
                                intentWrapper.startActivitySafely(activity, fragment);
                            }
                        })
                        .show();
                break;
            case IntentType.SAMSUNG_L:
                new AlertDialog.Builder(activity)
                        .setCancelable(false)
                        .setTitle("需要允许 " + mAppName + " 的自启动")
                        .setMessage(mTarget + "需要 " + mAppName + " 在屏幕关闭时继续运行。\n\n" +
                                "请点击『确定』，在弹出的『智能管理器』中，点击『内存』，选择『自启动应用程序』选项卡，将 " + mAppName + " 对应的开关打开。")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int w) {
                                intentWrapper.startActivitySafely(activity, fragment);
                            }
                        })
                        .show();
                break;
            case IntentType.SAMSUNG_M:
                new AlertDialog.Builder(activity)
                        .setCancelable(false)
                        .setTitle("需要允许 " + mAppName + " 的自启动")
                        .setMessage(mTarget + "需要 " + mAppName + " 在屏幕关闭时继续运行。\n\n" +
                                "请点击『确定』，在弹出的『电池』页面中，点击『未监视的应用程序』->『添加应用程序』，勾选 " + mAppName + "，然后点击『完成』。")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int w) {
                                intentWrapper.startActivitySafely(activity, fragment);
                            }
                        })
                        .show();
                break;
            case IntentType.MEIZU:
                new AlertDialog.Builder(activity)
                        .setCancelable(false)
                        .setTitle("需要允许 " + mAppName + " 保持后台运行")
                        .setMessage(mTarget + "需要允许 " + mAppName + " 保持后台运行。\n\n" +
                                "请点击『确定』，在弹出的应用信息界面中，将『后台管理』选项更改为『保持后台运行』。")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int w) {
                                intentWrapper.startActivitySafely(activity, fragment);
                            }
                        })
                        .show();
                break;
            case IntentType.MEIZU_GOD:
                new AlertDialog.Builder(activity)
                        .setCancelable(false)
                        .setTitle(mAppName + " 需要在待机时保持运行")
                        .setMessage(mTarget + "需要 " + mAppName + " 在待机时保持运行。\n\n" +
                                "请点击『确定』，在弹出的『待机耗电管理』中，将 " + mAppName + " 对应的开关打开。")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int w) {
                                intentWrapper.startActivitySafely(activity, fragment);
                            }
                        })
                        .show();
                break;
            case IntentType.ZTE:
            case IntentType.LETV:
            case IntentType.XIAOMI:
            case IntentType.OPPO:
            case IntentType.OPPO_OLD:
                new AlertDialog.Builder(activity)
                        .setCancelable(false)
                        .setTitle("需要允许 " + mAppName + " 的自启动")
                        .setMessage(mTarget + "需要 " + mAppName + " 加入到自启动白名单。\n\n" +
                                "请点击『确定』，在弹出的『自启动管理』中，将 " + mAppName + " 对应的开关打开。")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int w) {
                                intentWrapper.startActivitySafely(activity, fragment);
                            }
                        })
                        .show();
                break;
            case IntentType.COOLPAD:
                new AlertDialog.Builder(activity)
                        .setCancelable(false)
                        .setTitle("需要允许 " + mAppName + " 的自启动")
                        .setMessage(mTarget + "需要允许 " + mAppName + " 的自启动。\n\n" +
                                "请点击『确定』，在弹出的『酷管家』中，找到『软件管理』->『自启动管理』，取消勾选 " + mAppName + "，将 " + mAppName + " 的状态改为『已允许』。")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int w) {
                                intentWrapper.startActivitySafely(activity, fragment);
                            }
                        })
                        .show();
                break;
            case IntentType.VIVO_GOD:
                new AlertDialog.Builder(activity)
                        .setCancelable(false)
                        .setTitle("需要允许 " + mAppName + " 的后台运行")
                        .setMessage(mTarget + "需要允许 " + mAppName + " 在后台高耗电时运行。\n\n" +
                                "请点击『确定』，在弹出的『后台高耗电』中，将 " + mAppName + " 对应的开关打开。")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int w) {
                                intentWrapper.startActivitySafely(activity, fragment);
                            }
                        })
                        .show();
                break;
            case IntentType.GIONEE:
                new AlertDialog.Builder(activity)
                        .setCancelable(false)
                        .setTitle(mAppName + " 需要加入应用自启和绿色后台白名单")
                        .setMessage(mTarget + "需要允许 " + mAppName + " 的自启动和后台运行。\n\n" +
                                "请点击『确定』，在弹出的『系统管家』中，分别找到『应用管理』->『应用自启』和『绿色后台』->『清理白名单』，将 " + mAppName + " 添加到白名单。")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int w) {
                                intentWrapper.startActivitySafely(activity, fragment);
                            }
                        })
                        .show();
                break;
            case IntentType.LETV_GOD:
                new AlertDialog.Builder(activity)
                        .setCancelable(false)
                        .setTitle("需要禁止 " + mAppName + " 被自动清理")
                        .setMessage(mTarget + "需要禁止 " + mAppName + " 被自动清理。\n\n" +
                                "请点击『确定』，在弹出的『应用保护』中，将 " + mAppName + " 对应的开关关闭。")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int w) {
                                intentWrapper.startActivitySafely(activity, fragment);
                            }
                        })
                        .show();
                break;
            case IntentType.LENOVO:
                new AlertDialog.Builder(activity)
                        .setCancelable(false)
                        .setTitle("需要允许 " + mAppName + " 的后台运行")
                        .setMessage(mTarget + "需要允许 " + mAppName + " 的后台自启、后台 GPS 和后台运行。\n\n" +
                                "请点击『确定』，在弹出的『后台管理』中，分别找到『后台自启』、『后台 GPS』和『后台运行』，将 " + mAppName + " 对应的开关打开。")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int w) {
                                intentWrapper.startActivitySafely(activity, fragment);
                            }
                        })
                        .show();
                break;
            case IntentType.LENOVO_GOD:
                new AlertDialog.Builder(activity)
                        .setCancelable(false)
                        .setTitle("需要关闭 " + mAppName + " 的后台耗电优化")
                        .setMessage(mTarget + "需要关闭 " + mAppName + " 的后台耗电优化。\n\n" +
                                "请点击『确定』，在弹出的『后台耗电优化』中，将 " + mAppName + " 对应的开关关闭。")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int w) {
                                intentWrapper.startActivitySafely(activity, fragment);
                            }
                        })
                        .show();
                break;
            default:
                break;
        }
    }
}
