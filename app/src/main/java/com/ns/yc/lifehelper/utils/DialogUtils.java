package com.ns.yc.lifehelper.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.base.BaseApplication;
import com.ns.yc.lifehelper.ui.main.view.activity.WebViewActivity;
import com.ns.yc.lifehelper.ui.me.view.activity.MeFeedBackActivity;
import com.pedaily.yc.ycdialoglib.selector.CustomSelectDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/27
 * 描    述：对话框工具类
 * 修订历史：
 * ================================================
 */
public class DialogUtils {

    /**
     * 显示进度条对话框
     **/
    private static ProgressDialog dialog;
    public static void showProgressDialog(Activity activity) {
        if (dialog == null) {
            dialog = new ProgressDialog(activity);
            dialog.setMessage("玩命加载中……");
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
        }
        dialog.show();
    }

    /**
     * 关闭进度条对话框
     */
    public static void dismissProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }



    /**
     * 自定义PopupWindow
     */
    public static void showCustomPopupWindow(final Activity activity){
        if(!activity.isFinishing()){
            View popMenuView = activity.getLayoutInflater().inflate(R.layout.dialog_custom_view, null);
            final PopupWindow popMenu = new PopupWindow(popMenuView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
            popMenu.setClippingEnabled(false);
            popMenu.setFocusable(true);         //点击其他地方关闭
            //popMenu.setAnimationStyle(R.style.dialog_custom_view);
            popMenu.showAtLocation(popMenuView, Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            AppUtil.setBackgroundAlpha(activity,0.5f);

            TextView tv_first = (TextView) popMenuView.findViewById(R.id.tv_first);
            TextView tv_second = (TextView) popMenuView.findViewById(R.id.tv_second);
            TextView tv_three = (TextView) popMenuView.findViewById(R.id.tv_three);
            tv_first.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //吐槽跳转意见反馈页面
                    activity.startActivity(new Intent(activity, MeFeedBackActivity.class));
                    popMenu.dismiss();
                }
            });
            tv_second.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //评分跳转应用宝 com.tencent.android.qqdownloader
                    if(AppUtil.isPkgInstalled(activity,"com.tencent.android.qqdownloader")){
                        //GoToScoreUtils.goToMarket(activity, AppUtils.getAppPackageName());
                        //GoToScoreUtils.goToMarket(activity, "com.zero2ipo.harlanhu.pedaily");
                        ArrayList<String> installAppMarkets = GoToScoreUtils.getInstallAppMarkets(activity);
                        ArrayList<String> filterInstallMarkets = GoToScoreUtils.getFilterInstallMarkets(activity, installAppMarkets);
                        GoToScoreUtils.launchAppDetail(activity,"com.zero2ipo.harlanhu.pedaily",filterInstallMarkets.get(0));
                    }else {
                        Intent intent = new Intent(activity, WebViewActivity.class);
                        intent.putExtra("url", Constant.QQUrl);
                        activity.startActivity(intent);
                    }
                    popMenu.dismiss();
                }
            });
            tv_three.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popMenu.dismiss();
                }
            });
            popMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    AppUtil.setBackgroundAlpha(activity,1.0f);
                }
            });
        }
    }


    /**
     * 自定义AlertDialog
     */
    public static void showCustomAlertDialog(final Activity activity){
        if(!activity.isFinishing()){
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            final AlertDialog alertDialog = builder.create();
            View view = LayoutInflater.from(activity).inflate(R.layout.dialog_custom_view, null);
            alertDialog.setView(view);

            TextView tv_first = (TextView) view.findViewById(R.id.tv_first);
            TextView tv_second = (TextView) view.findViewById(R.id.tv_second);
            TextView tv_three = (TextView) view.findViewById(R.id.tv_three);
            tv_first.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //吐槽跳转意见反馈页面
                    activity.startActivity(new Intent(activity, MeFeedBackActivity.class));
                    alertDialog.dismiss();
                }
            });
            tv_second.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //评分跳转应用宝 com.tencent.android.qqdownloader
                    if(AppUtil.isPkgInstalled(activity,"com.tencent.android.qqdownloader")){
                        //GoToScoreUtils.goToMarket(activity, AppUtils.getAppPackageName());
                        //GoToScoreUtils.goToMarket(activity, "com.zero2ipo.harlanhu.pedaily");
                        ArrayList<String> installAppMarkets = GoToScoreUtils.getInstallAppMarkets(activity);
                        ArrayList<String> filterInstallMarkets = GoToScoreUtils.getFilterInstallMarkets(activity, installAppMarkets);
                        GoToScoreUtils.launchAppDetail(activity,"com.zero2ipo.harlanhu.pedaily",filterInstallMarkets.get(0));
                    }else {
                        Intent intent = new Intent(activity, WebViewActivity.class);
                        intent.putExtra("url", Constant.QQUrl);
                        activity.startActivity(intent);
                    }
                    alertDialog.dismiss();
                }
            });
            tv_three.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            if(alertDialog.getWindow()!=null){
                Window window = alertDialog.getWindow();
                window.setBackgroundDrawableResource(R.color.colorTransparent);
                window.setGravity(Gravity.CENTER);                          //此处可以设置dialog显示的位置
                window.setWindowAnimations(R.style.dialog_custom_view);     //添加动画
            }

            //alertDialog.setTitle("自定义AlertDialog");
            alertDialog.show();
        }
    }


    /**
     * 全局吐司
     */
    private static Toast toast;
    public static void showWindowToast(String str) {
        BaseApplication application = BaseApplication.getInstance();
        LayoutInflater inflate = (LayoutInflater) application.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.toast_window_layout, null);
        TextView tv_toast = (TextView) view.findViewById(R.id.message);
        if(toast==null){
            toast = new Toast(application);
        }
        toast.setView(view);
        tv_toast.setText(str);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    /**
     * 全局加载弹窗
     */
    public static void showWindowLoad(){
        BaseApplication context = BaseApplication.getInstance();
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppTheme);        //传统主题
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading_view, null);
        alertDialog.setView(view);
        ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
        AnimationDrawable animationDrawable = (AnimationDrawable) iv_image.getDrawable();
        animationDrawable.start();


        if(alertDialog.getWindow()!=null){
            Window window = alertDialog.getWindow();
            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorTransparent);

            WindowManager.LayoutParams params = window.getAttributes();
            //WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
        }

        //报错：Unable to add window -- token null is not for an application
        //全局弹窗必须依附Activity，必须在Activity运行下才能弹窗，否则崩溃
        if (Build.VERSION.SDK_INT >= 23) {
            if(!Settings.canDrawOverlays(context)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return;
            } else {
                //Android6.0以上
                if (!alertDialog.isShowing()) {
                    alertDialog.show();
                }
            }
        } else {
            //Android6.0以下，不用动态声明权限
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        }
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
    }


    /**
     * 全局对话框
     * 第一个方法利用系统弹出dialog
     * 报错：IllegalStateException: You need to use a Theme.AppCompat theme (or descendant) with this activity.
     * 解决办法：
     * context：全局上下文，service上下文
     */
    public static void showWindowDialog(final Activity context){
        //首先获取当前Activity
        //final Activity activity = BaseAppManager.getInstance().currentActivity();
        if(AppUtil.isActivityLiving(context)){
            //context 不行
            //final BaseApplication context = BaseApplication.getInstance();
            //AlertDialog.Builder builder = new AlertDialog.Builder(context);
            AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AppTheme);        //传统主题
            final AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(true);


            View view = LayoutInflater.from(context).inflate(R.layout.dialog_custom_view, null);
            alertDialog.setView(view);


            if(alertDialog.getWindow()!=null){
                Window window = alertDialog.getWindow();
                //TODO 这句话很重要
                window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                window.setBackgroundDrawableResource(R.color.colorTransparent);

                WindowManager.LayoutParams params = window.getAttributes();
                //WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.MATCH_PARENT;
                params.gravity = Gravity.CENTER;
                window.setAttributes(params);

                //window.setGravity(Gravity.CENTER);                          //此处可以设置dialog显示的位置
                //window.setWindowAnimations(R.style.dialog_custom_view);       //添加动画
            }

            //报错：Unable to add window -- token null is not for an application
            //全局弹窗必须依附Activity，必须在Activity运行下才能弹窗，否则崩溃
            if (Build.VERSION.SDK_INT >= 23) {
                if(!Settings.canDrawOverlays(context)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    return;
                } else {
                    //Android6.0以上
                    if (!alertDialog.isShowing()) {
                        alertDialog.show();
                    }
                }
            } else {
                //Android6.0以下，不用动态声明权限
                if (!alertDialog.isShowing()) {
                    alertDialog.show();
                }
            }

            //Unable to add window android.view.ViewRootImpl$W@12b82d6 -- permission denied for this window type
            //alertDialog.show();
            AppUtil.setBackgroundAlpha(context,0.5f);
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    AppUtil.setBackgroundAlpha(context,1.0f);
                }
            });
        }
    }


    /**
     * 全局对话框
     * 第二个方法是获取WindowManager，直接添加view
     * 这种方法不太好使
     */
    public static void showWindowDialog(){
        BaseApplication mContext = BaseApplication.getInstance();
        WindowManager mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_custom_window, null);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        // 类型
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        // WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        // 设置flag
        params.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        //params.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        // WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        mWindowManager.addView(view, params);
    }



    /**
     * 全局对话框
     * 第三个方法，获取栈顶Activity，弹窗
     * 报错：IllegalStateException: You need to use a Theme.AppCompat theme (or descendant) with this activity.
     * 解决办法：
     * context：Activity
     * 注意，由于是在Activity页面弹窗，因此不需要设置setType属性；
     *      如果是设置主题，那么弹窗无法居中，new AlertDialog.Builder(context,R.style.AppTheme);        //传统主题
     */
    public static void showActivityDialog(final Activity activity){
        //final Activity activity = BaseAppManager.getInstance().currentActivity();
        if(AppUtil.isActivityLiving(activity)){
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            final AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);


            View view = LayoutInflater.from(activity).inflate(R.layout.dialog_custom_view, null);
            alertDialog.setView(view);
            TextView tv_first = (TextView) view.findViewById(R.id.tv_first);
            TextView tv_second = (TextView) view.findViewById(R.id.tv_second);
            TextView tv_three = (TextView) view.findViewById(R.id.tv_three);
            tv_first.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //吐槽跳转意见反馈页面
                    activity.startActivity(new Intent(activity, MeFeedBackActivity.class));
                    alertDialog.dismiss();
                }
            });
            tv_second.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //评分跳转应用宝 com.tencent.android.qqdownloader
                    if(AppUtil.isPkgInstalled(activity,"com.tencent.android.qqdownloader")){

                        ArrayList<String> installAppMarkets = GoToScoreUtils.queryInstalledMarketPkgs(activity);
                        ArrayList<String> filterInstallMarkets = GoToScoreUtils.filterInstalledPkgs(activity, installAppMarkets);

                        final ArrayList<String> markets = new ArrayList<>();
                        for(int a=0 ; a<installAppMarkets.size() ; a++){
                            Log.e("应用市场----",installAppMarkets.get(a));
                        }

                        if(filterInstallMarkets.size()>0){
                            //过滤
                            for(int a=0 ; a<filterInstallMarkets.size() ; a++){
                                Log.e("应用市场++++",filterInstallMarkets.get(a));
                                String pkg = filterInstallMarkets.get(a);
                                if(installAppMarkets.contains(pkg)){
                                    markets.add(pkg);
                                }
                            }
                            List<String> names = new ArrayList<>();
                            for(int b=0 ; b<markets.size() ; b++){
                                com.blankj.utilcode.util.AppUtils.AppInfo appInfo = com.blankj.utilcode.util.AppUtils.getAppInfo(markets.get(b));
                                String name = appInfo.getName();
                                names.add(name);
                            }
                            showDialog(activity , new CustomSelectDialog.SelectDialogListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    GoToScoreUtils.launchAppDetail(activity,"com.zero2ipo.harlanhu.pedaily",markets.get(position));
                                }
                            }, names);
                        }else {
                            //投资界应用宝评分链接
                            String QQUrl = "http://android.myapp.com/myapp/detail.htm?apkName=com.zero2ipo.harlanhu.pedaily";
                            Intent intent = new Intent(activity, WebViewActivity.class);
                            intent.putExtra("url", QQUrl);
                            activity.startActivity(intent);
                        }
                    }else {
                        Intent intent = new Intent(activity, WebViewActivity.class);
                        intent.putExtra("url", Constant.QQUrl);
                        activity.startActivity(intent);
                    }
                    alertDialog.dismiss();
                }
            });
            tv_three.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            if(alertDialog.getWindow()!=null){
                Window window = alertDialog.getWindow();
                window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                window.setBackgroundDrawableResource(R.color.colorTransparent);

                WindowManager.LayoutParams params = window.getAttributes();
                //WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.MATCH_PARENT;
                params.gravity = Gravity.CENTER;
                window.setAttributes(params);

                //window.setGravity(Gravity.CENTER);                          //此处可以设置dialog显示的位置
                //window.setWindowAnimations(R.style.dialog_custom_view);       //添加动画
            }

            //报错：Unable to add window -- token null is not for an application
            //全局弹窗必须依附Activity，必须在Activity运行下才能弹窗，否则崩溃
            //注意，小米，三星等手机需要手动打开权限才行
            if (Build.VERSION.SDK_INT >= 23) {
                if(!Settings.canDrawOverlays(activity)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                    return;
                } else {
                    //Android6.0以上
                    if (!alertDialog.isShowing()) {
                        alertDialog.show();
                    }
                }
            } else {
                //Android6.0以下，不用动态声明权限
                if (!alertDialog.isShowing()) {
                    alertDialog.show();
                }
            }
            alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if(keyCode==KeyEvent.KEYCODE_BACK){
                        if(alertDialog.isShowing()){
                            alertDialog.dismiss();
                        }
                    }
                    return false;
                }
            });

            AppUtil.setBackgroundAlpha(activity,0.5f);
            //Unable to add window android.view.ViewRootImpl$W@12b82d6 -- permission denied for this window type
            //alertDialog.show();
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    AppUtil.setBackgroundAlpha(activity,1.0f);
                }
            });
        }
    }


    /**展示对话框视图，构造方法创建对象*/
    private static CustomSelectDialog showDialog(Activity activity , CustomSelectDialog.SelectDialogListener listener, List<String> names) {
        CustomSelectDialog dialog = new CustomSelectDialog(activity, R.style.transparentFrameWindowStyle, listener, names);
        if (!activity.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }



}
