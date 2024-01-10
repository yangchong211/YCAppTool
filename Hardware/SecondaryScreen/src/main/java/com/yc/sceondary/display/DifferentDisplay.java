package com.yc.sceondary.display;

import android.app.Presentation;
import android.content.Context;
import android.view.Display;
import android.widget.FrameLayout;

import com.yc.sceondary.R;

/**
 * 双屏异显第一种实现方式（官方提供的Presentation）
 */
public class DifferentDisplay extends Presentation implements IDisplay{

    private Context context;
    private FrameLayout flDisplayView;

    public DifferentDisplay(Context outerContext, Display display) {
        super(outerContext, display);
        init(outerContext);
    }

    public DifferentDisplay(Context outerContext, Display display, int theme) {
        super(outerContext, display, theme);
        init(outerContext);
    }

    @Override
    public void onDisplayChanged() {
        super.onDisplayChanged();
    }

    @Override
    public void onDisplayRemoved() {
        super.onDisplayRemoved();
    }

    private void init(Context outerContext) {
        context = outerContext;
        setContentView(R.layout.display_screen_view);
        flDisplayView = findViewById(R.id.fl_display_view);
    }

    public FrameLayout getRootView(){
        return flDisplayView;
    }

    public Context getRootContext(){
        return context;
    }

    public void close() {
        onDisplayRemoved();
        dismiss();
    }

    @Override
    public void showDisplay(Context context) {
        if (!this.isShowing()){
            show();
        }
    }

    @Override
    public void dismissDisplay(Context context) {
        if (this.isShowing()){
            close();
        }
    }
}
