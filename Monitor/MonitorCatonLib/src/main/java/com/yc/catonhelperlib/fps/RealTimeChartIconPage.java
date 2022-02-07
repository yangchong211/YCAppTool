package com.yc.catonhelperlib.fps;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.yc.catonhelperlib.R;

public class RealTimeChartIconPage extends BaseFloatPage implements View.OnClickListener {

    public static final String TAG = "RealTimeChartIconPage";
    private OnFloatPageChangeListener mListener;

    public RealTimeChartIconPage() {
    }

    protected View onCreateView(Context context, ViewGroup view) {
        ImageView imageView = new ImageView(this.getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.icon_close_white);
        imageView.setOnClickListener(this);
        return imageView;
    }

    protected void onCreate(Context context) {
        super.onCreate(context);
        PerformanceManager.getInstance().init(context);
    }

    protected void onViewCreated(View view) {
        super.onViewCreated(view);
    }

    protected void onLayoutParamsCreated(WindowManager.LayoutParams params) {
        params.flags = 8;
        params.gravity = 53;
        params.width = dp2px(this.getContext(), 40.0F);
        params.height = dp2px(this.getContext(), 40.0F);
    }

    public void setListener(OnFloatPageChangeListener listener) {
        this.mListener = listener;
    }

    protected static void openChartIconPage(OnFloatPageChangeListener listener) {
        FloatPageManager.getInstance().remove("RealTimeChartIconPage");
        PageIntent pageIntent = new PageIntent(RealTimeChartIconPage.class);
        pageIntent.mode = 1;
        pageIntent.tag = "RealTimeChartIconPage";
        Bundle bundle = new Bundle();
        pageIntent.bundle = bundle;
        FloatPageManager.getInstance().add(pageIntent);
        RealTimeChartIconPage page = (RealTimeChartIconPage)FloatPageManager.getInstance().getFloatPage("RealTimeChartIconPage");
        if (page != null) {
            page.setListener(listener);
        }

    }

    protected static void closeChartIconPage() {
        FloatPageManager.getInstance().remove("RealTimeChartPage");
        FloatPageManager.getInstance().remove("RealTimeChartIconPage");
    }

    public void onClick(View v) {
        closeChartIconPage();
        this.notifyPageClose();
        if (this.mListener != null) {
            this.mListener.onFloatPageClose("RealTimeChartIconPage");
        }
    }

    private void notifyPageClose() {
        PerformanceManager.getInstance().stopMonitorFrameInfo();
    }

    public void onEnterForeground() {
        super.onEnterForeground();
        this.getRootView().setVisibility(View.VISIBLE);
    }

    public void onEnterBackground() {
        super.onEnterBackground();
        this.getRootView().setVisibility(View.GONE);
    }

    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }
}

