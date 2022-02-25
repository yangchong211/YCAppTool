package com.yc.catonhelperlib.fps;


import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardiogramView extends View implements Runnable {
    private static final float DEFAULT_ITEM_COUNT = 12.0F;
    private static final float MAX_ITEM_COUNT = 14.0F;
    private static final int DEFAULT_FRAME_DELAY = 32;
    private static final int DEFAULT_FRAME_COUNT = 62;
    private float mItemWidth;
    private int mTotalFrameCount = 62;
    private int mCurrentFrameCount = 0;
    private LineRender mRender;
    private List<LineChart.LineData> mList = Collections.synchronizedList(new ArrayList());
    private IDataSource mDataSource;
    private Handler mHandler = new Handler();

    public CardiogramView(Context context) {
        super(context);
        this.init(context);
    }

    public CardiogramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    private void init(Context context) {
        this.mRender = new LineRender(context);
        this.mRender = new LineRender(context);
        this.mRender.setMaxValue(100);
        this.mRender.setMinValue(0);
        this.mRender.setPointSize(5.0F);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.mItemWidth = (float)w / 12.0F;
        this.mRender.measure(this.mItemWidth, (float)h);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        float translateX = this.getCanvasTranslate();
        canvas.translate(translateX, 0.0F);
        this.drawLine(canvas);
        this.checkFirstItemBound();
        canvas.restore();
    }

    private void checkFirstItemBound() {
        ++this.mCurrentFrameCount;
        if (this.mCurrentFrameCount >= this.mTotalFrameCount) {
            this.mCurrentFrameCount = 0;
            if (this.mDataSource != null) {
                this.mList.add(this.mDataSource.createData());
            }

            if ((float)this.mList.size() > 14.0F) {
                ((LineChart.LineData)this.mList.remove(0)).release();
            }
        }

    }

    private float getCanvasTranslate() {
        return -this.mItemWidth * ((float)this.mCurrentFrameCount / (float)this.mTotalFrameCount) + this.mItemWidth * (14.0F - (float)this.mList.size());
    }

    private void drawLine(Canvas canvas) {
        for(int index = 0; (float)index < Math.min((float)this.mList.size(), 13.0F); ++index) {
            this.mRender.setCurrentValue(index, ((LineChart.LineData)this.mList.get(index)).value);
            if (index == this.mList.size() - 2) {
                this.mRender.setShowLabel(true);
                this.mRender.setLabelAlpha(1.0F);
                this.mRender.setLabel(((LineChart.LineData)this.mList.get(index)).label);
            } else if (index == this.mList.size() - 3) {
                this.mRender.setLabel(((LineChart.LineData)this.mList.get(index)).label);
                this.mRender.setLabelAlpha(1.0F - (float)this.mCurrentFrameCount / (float)this.mTotalFrameCount);
                this.mRender.setShowLabel(true);
            } else {
                this.mRender.setLabel(((LineChart.LineData)this.mList.get(index)).label);
                this.mRender.setShowLabel(false);
            }

            if (index == this.mList.size() - 1) {
                this.mRender.setNextValue(0.0F);
                this.mRender.setDrawRightLine(false);
            } else {
                this.mRender.setDrawRightLine(true);
                this.mRender.setNextValue(((LineChart.LineData)this.mList.get(index + 1)).value);
            }

            this.mRender.draw(canvas);
        }

    }

    public void startMove() {
        this.mHandler.removeCallbacks(this);
        this.mHandler.post(this);
    }

    public void stopMove() {
        this.mHandler.removeCallbacks(this);
    }

    public void setInterval(int milliSecond) {
        this.mTotalFrameCount = milliSecond / 32;
    }

    public void setDataSource(@NonNull IDataSource dataSource) {
        this.mDataSource = dataSource;
        this.mList.clear();
        this.mList.add(dataSource.createData());
    }

    public void run() {
        this.invalidate();
        this.mHandler.postDelayed(this, 32L);
    }
}

