package com.yc.sceondary.display;

import android.app.Presentation;
import android.content.Context;
import android.view.Display;
import android.widget.FrameLayout;

import com.yc.sceondary.R;

/**
 * 双屏异显（同显）实现方式
 */
public class SecondDisplay extends Presentation {

    private Context context;
    private FrameLayout flDisplayView;

    public SecondDisplay(Context outerContext, Display display) {
        super(outerContext, display);
        init(outerContext);
    }

    public SecondDisplay(Context outerContext, Display display, int theme) {
        super(outerContext, display, theme);
        init(outerContext);
    }

    @Override
    public void onDisplayChanged() {
        super.onDisplayChanged();
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
}
