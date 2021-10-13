package com.ycbjie.love.view.whitesnow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class SnowView extends View {
    private static final long DELAY = 1;
    private static final int NUM_SNOWFLAKES = 200;
    private Runnable runnable = new Runnable() {
        public void run() {
            SnowView.this.invalidate();
        }
    };
    private SnowFlake[] snowflakes;

    public SnowView(Context context) {
        super(context);
    }

    public SnowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SnowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void resize(int width, int height) {
        Paint paint = new Paint(1);
        paint.setColor(-1);
        paint.setStyle(Style.FILL);
        this.snowflakes = new SnowFlake[NUM_SNOWFLAKES];
        for (int i = 0; i < NUM_SNOWFLAKES; i++) {
            this.snowflakes[i] = SnowFlake.create(width, height, paint);
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            resize(w, h);
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (SnowFlake snowFlake : this.snowflakes) {
            snowFlake.draw(canvas);
        }
        getHandler().postDelayed(this.runnable, DELAY);
    }
}
