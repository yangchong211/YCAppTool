package com.yc.sceondary;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
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
                    presentation.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                }
            }
        }
    }

    @Override
    public void showView(int layout) {
        if (presentation != null) {
            Context rootContext = presentation.getRootContext();
            FrameLayout rootView = presentation.getRootView();
            View view = LayoutInflater.from(rootContext).inflate(layout, rootView);
            showView(view);
        }
    }

    @Override
    public void showView(View view) {
        if (presentation != null) {
            FrameLayout rootView = presentation.getRootView();
            if (view != null && rootView != null) {
                rootView.removeAllViews();
                rootView.addView(view);
            }
            presentation.show();
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
            presentation.dismiss();
        }
    }

    @Override
    public SurfaceView getSurfaceView() {
        return null;
    }
}
