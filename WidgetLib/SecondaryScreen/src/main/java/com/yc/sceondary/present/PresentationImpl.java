package com.yc.sceondary.present;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.yc.sceondary.display.SecondDisplay;

public class PresentationImpl implements IPresentation {

    private SecondDisplay presentation;

    @Override
    public void initView(Context context) {
        if (hasSecondScreen()) {
            //双屏显示
            //屏幕管理类
            DisplayManager displayManager;
            //屏幕数组
            Display[] displays;
            displayManager = (DisplayManager) context.getApplicationContext()
                    .getSystemService(Context.DISPLAY_SERVICE);
            if (displayManager != null) {
                //得到显示器数组
                displays = displayManager.getDisplays();
                if (displays.length > 1) {
                    //displays[1]是副屏
                    if (presentation == null) {
                        presentation = new SecondDisplay(context, displays[1]);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        presentation.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                    }else {
                        presentation.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    }
                }
            }
        }
    }

    @Override
    public void showView(int layout) {
        if (presentation != null) {
            Context rootContext = presentation.getRootContext();
            //FrameLayout rootView = presentation.getRootView();
            //这种不行
            //View view = LayoutInflater.from(rootContext).inflate(layout, rootView);
            //View view = LayoutInflater.from(rootContext).inflate(layout, rootView,true);
            View view = LayoutInflater.from(rootContext).inflate(layout, null);
            showView(view);
        }
    }

    @Override
    public void showView(View view) {
        if (presentation != null && view!=null) {
            //先移除view布局的父布局
            ViewParent parent = view.getParent();
            if (parent != null) {
                //fix The specified child already has a parent. You must call removeView()
                //指定的孩子已经有一个父母。您必须先在孩子的父母上调用removeView
                ((ViewGroup) parent).removeView(view);
            }

            //然后在移除root根布局的所有子布局，最后再将view添加到root根布局中
            FrameLayout rootView = presentation.getRootView();
            if (rootView != null) {
                if (rootView.getChildCount() > 0){
                    rootView.removeAllViews();
                }
                rootView.addView(view);
            }
            presentation.showDisplay(view.getContext());
        }
    }

    @Override
    public boolean hasSecondScreen() {
        return true;
    }

    @Override
    public void closeDisplay() {
        if (presentation != null){
            presentation.close();
            presentation = null;
        }
    }

    @Override
    public void destroyView(Context context) {
        if (presentation != null) {
            presentation.dismissDisplay(context);
        }
    }

    @Override
    public SurfaceView getSurfaceView() {
        return null;
    }

    /**
     * 将View从父控件中移除
     */
    private void removeViewFormParent(View v) {
        if (v == null) {
            return;
        }
        ViewParent parent = v.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(v);
        }
    }

}
