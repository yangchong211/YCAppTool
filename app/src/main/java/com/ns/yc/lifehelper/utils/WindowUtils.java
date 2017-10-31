package com.ns.yc.lifehelper.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.ui.main.view.activity.WebViewActivity;
import com.ns.yc.lifehelper.ui.me.view.activity.MeFeedBackActivity;

import java.util.ArrayList;

/**
 * Created by PC on 2017/9/30.
 * 作者：全局弹窗
 */

public class WindowUtils {

    private static final String LOG_TAG = "WindowUtils";
    private static View mView = null;
    private static WindowManager mWindowManager = null;
    private static Context mContext = null;
    public static Boolean isShown = false;

    /**
     * 显示弹出框
     * @param context
     */
    public static void showPopupWindow(final Context context) {
        if (isShown) {
            Log.i(LOG_TAG, "return cause already shown");
            return;
        }
        isShown = true;
        Log.i(LOG_TAG, "showPopupWindow");
        // 获取应用的Context
        mContext = context.getApplicationContext();
        // 获取WindowManager
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mView = setUpView(context);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        // 类型
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        // WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        // 设置flag
        int flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        params.flags = flags;
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        mWindowManager.addView(mView, params);
        Log.i(LOG_TAG, "add view");
    }
    /**
     * 隐藏弹出框
     */
    public static void hidePopupWindow() {
        Log.i(LOG_TAG, "hide " + isShown + ", " + mView);
        if (isShown && null != mView) {
            Log.i(LOG_TAG, "hidePopupWindow");
            mWindowManager.removeView(mView);
            isShown = false;
        }
    }

    private static View setUpView(final Context context) {
        Log.i(LOG_TAG, "setUp view");
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_custom_window, null);
        TextView tv_first = (TextView) view.findViewById(R.id.tv_first);
        TextView tv_second = (TextView) view.findViewById(R.id.tv_second);
        TextView tv_three = (TextView) view.findViewById(R.id.tv_three);
        tv_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //吐槽跳转意见反馈页面
                context.startActivity(new Intent(context, MeFeedBackActivity.class));
                // 隐藏弹窗
                WindowUtils.hidePopupWindow();
            }
        });
        tv_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //评分跳转应用宝 com.tencent.android.qqdownloader
                if(AppUtil.isPkgInstalled(context,"com.tencent.android.qqdownloader")){
                    ArrayList<String> installAppMarkets = GoToScoreUtils.getInstallAppMarkets(context);
                    ArrayList<String> filterInstallMarkets = GoToScoreUtils.getFilterInstallMarkets(context, installAppMarkets);
                    GoToScoreUtils.launchAppDetail(context,"com.zero2ipo.harlanhu.pedaily",filterInstallMarkets.get(0));
                }else {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("url", Constant.QQUrl);
                    context.startActivity(intent);
                }
                WindowUtils.hidePopupWindow();
            }
        });
        tv_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WindowUtils.hidePopupWindow();
            }
        });

        // 点击窗口外部区域可消除
        // 这点的实现主要将悬浮窗设置为全屏大小，外层有个透明背景，中间一部分视为内容区域
        // 所以点击内容区域外部视为点击悬浮窗外部
        final View popupWindowView = view.findViewById(R.id.popup_window);// 非透明的内容区域
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i(LOG_TAG, "onTouch");
                int x = (int) event.getX();
                int y = (int) event.getY();
                Rect rect = new Rect();
                popupWindowView.getGlobalVisibleRect(rect);
                if (!rect.contains(x, y)) {
                    WindowUtils.hidePopupWindow();
                }
                Log.i(LOG_TAG, "onTouch : " + x + ", " + y + ", rect: " + rect);
                return false;
            }
        });
        // 点击back键可消除
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        WindowUtils.hidePopupWindow();
                        return true;
                    default:
                        return false;
                }
            }
        });
        return view;
    }

}
