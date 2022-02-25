package com.yc.catonhelperlib.fps;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;

public class RealTimeChartPage extends BaseFloatPage {

    public static final String TAG = "RealTimeChartPage";
    private LineChart mLineChart;

    public RealTimeChartPage() {
    }

    protected View onCreateView(Context context, ViewGroup view) {
        this.mLineChart = new LineChart(context);
        return this.mLineChart;
    }

    protected void onViewCreated(View view) {
        super.onViewCreated(view);
        this.init();
    }

    public void init() {
        String title = this.getBundle().getString("title");
        int interval = this.getBundle().getInt("interval", 1000);
        IDataSource dataSource = new FrameDataSource();
        this.mLineChart.setTitle(title);
        this.mLineChart.setInterval(interval);
        this.mLineChart.setDataSource(dataSource);
        this.mLineChart.startMove();
    }

    protected void onLayoutParamsCreated(LayoutParams params) {
        params.flags = 24;
        params.width = -1;
        params.height = dp2px(this.getContext(), 240.0F);
    }

    protected boolean onBackPressed() {
        return false;
    }

    public static void openChartPage(String title,int interval, OnFloatPageChangeListener listener) {
        if (!updateChartPage(title,interval, listener)) {
            closeChartPage();
            PageIntent pageIntent = new PageIntent(RealTimeChartPage.class);
            pageIntent.mode = 1;
            pageIntent.tag = "RealTimeChartPage";
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putInt("interval", interval);
            pageIntent.bundle = bundle;
            FloatPageManager.getInstance().add(pageIntent);
            RealTimeChartIconPage.openChartIconPage(listener);
        }
    }

    private static boolean updateChartPage(String title, int interval, OnFloatPageChangeListener listener) {
        RealTimeChartPage chartPage = (RealTimeChartPage)FloatPageManager.getInstance().getFloatPage("RealTimeChartPage");
        RealTimeChartIconPage chartIconPage = (RealTimeChartIconPage)FloatPageManager.getInstance().getFloatPage("RealTimeChartIconPage");
        if (chartIconPage != null && chartPage != null) {
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putInt("interval", interval);
            chartPage.setBundle(bundle);
            chartPage.init();
            bundle = new Bundle();
            chartIconPage.setBundle(bundle);
            chartIconPage.setListener(listener);
            return true;
        } else {
            return false;
        }
    }

    public static void closeChartPage() {
        FloatPageManager.getInstance().remove("RealTimeChartPage");
        FloatPageManager.getInstance().remove("RealTimeChartIconPage");
    }

    public static void removeCloseListener() {
        RealTimeChartIconPage page = (RealTimeChartIconPage)FloatPageManager
                .getInstance().getFloatPage("RealTimeChartIconPage");
        if (page != null) {
            page.setListener((OnFloatPageChangeListener)null);
        }

    }

    public void onEnterForeground() {
        super.onEnterForeground();
        this.mLineChart.startMove();
        this.getRootView().setVisibility(View.VISIBLE);
    }

    public void onEnterBackground() {
        super.onEnterBackground();
        this.mLineChart.stopMove();
        this.getRootView().setVisibility(View.GONE);
    }


    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }
}

