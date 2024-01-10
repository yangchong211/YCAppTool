package com.yc.sceondary.display;

import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.widget.FrameLayout;

import com.yc.sceondary.R;

/**
 * 双屏异显（同显）实现方式
 */
public class SecondDisplay extends Presentation implements IDisplay {

    private final Context context;
    private FrameLayout flDisplayView;

    public SecondDisplay(Context outerContext, Display display) {
        this(outerContext, display, 0);
    }

    public SecondDisplay(Context outerContext, Display display, int theme) {
        super(outerContext, display, theme);
        this.context = outerContext;
    }

    @Override
    public void onDisplayChanged() {
        super.onDisplayChanged();
    }

    @Override
    public void onDisplayRemoved() {
        super.onDisplayRemoved();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.display_screen_view);
        flDisplayView = findViewById(R.id.fl_display_view);
    }

    public FrameLayout getRootView() {
        return flDisplayView;
    }

    public Context getRootContext() {
        return context;
    }

    public void close() {
        onDisplayRemoved();
        dismiss();
    }

    @Override
    public void showDisplay(Context context) {
        if (!this.isShowing()) {
            show();
        }
    }

    @Override
    public void dismissDisplay(Context context) {
        if (this.isShowing()) {
            close();
        }
    }
}
