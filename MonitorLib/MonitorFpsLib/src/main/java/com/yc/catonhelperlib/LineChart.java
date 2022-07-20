package com.yc.catonhelperlib;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pools.SimplePool;

public class LineChart extends FrameLayout {
    private TextView mTitle;
    private CardiogramView mLine;

    public LineChart(@NonNull Context context) {
        super(context);
        this.initView(context);
    }

    public LineChart(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.view_line_chart, this);
        this.mTitle = (TextView)this.findViewById(R.id.tv_title);
        this.mLine = (CardiogramView)this.findViewById(R.id.line_chart_view);
    }

    public void setTitle(String title) {
        this.mTitle.setText(title);
    }

    public void startMove() {
        this.mLine.startMove();
    }

    public void stopMove() {
        this.mLine.stopMove();
    }

    public void setInterval(int interval) {
        this.mLine.setInterval(interval);
    }

    public void setDataSource(@NonNull IDataSource dataSource) {
        this.mLine.setDataSource(dataSource);
    }

    public static class LineData {
        public float value;
        public String label;
        private static SimplePool<LineChart.LineData> mPool = new SimplePool(50);

        public LineData(float value, String label) {
            this.value = value;
            this.label = label;
        }

        public static LineChart.LineData obtain(float value, String label) {
            LineChart.LineData lineData = (LineChart.LineData)mPool.acquire();
            if (lineData == null) {
                return new LineChart.LineData(value, label);
            } else {
                lineData.value = value;
                lineData.label = label;
                return lineData;
            }
        }

        public void release() {
            mPool.release(this);
        }
    }
}

