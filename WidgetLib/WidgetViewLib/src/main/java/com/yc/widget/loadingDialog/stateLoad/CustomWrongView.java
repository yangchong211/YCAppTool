package com.yc.widget.loadingDialog.stateLoad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


/**
 * 出现错误时候展示的view
 */
public class CustomWrongView extends View {

    private final String TAG = this.getClass().getSimpleName();

    private OnFinishListener listener;

    private Context mContext;
    private int mWidth = 0;
    private float mPadding = 0f;
    private Paint mPaint;
    private RectF rectF;

    private int line1_x;
    private int line1_y;

    private int line2_x;
    private int line2_y;

    private int times = 0;
    private boolean drawEveryTime = true;
    private int speed = 1;
    private int count = 0;

    public CustomWrongView(Context context) {
        this(context, null);
    }

    public CustomWrongView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomWrongView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode != MeasureSpec.AT_MOST && heightSpecMode != MeasureSpec.AT_MOST) {
            mWidth = widthSpecSize >= heightSpecSize ? widthSpecSize : heightSpecSize;
        } else if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode != MeasureSpec.AT_MOST) {
            mWidth = heightSpecSize;
        } else if (widthSpecMode != MeasureSpec.AT_MOST) {
            mWidth = widthSpecSize;
        } else {
            mWidth = dip2px(mContext, 80);
        }
        setMeasuredDimension(mWidth, mWidth);
        mPadding = 8;
        rectF = new RectF(mPadding, mPadding, mWidth - mPadding, mWidth - mPadding);
    }


    private void initPaint(Context context) {
        mContext = context;
        mPaint = new Paint();
        //抗锯齿
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(8);
    }

    int progress = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        if (drawEveryTime){
            drawDynamic(canvas);
        } else {
            drawStatic(canvas);
            if (listener != null){
                listener.dispatchFinishEvent(this);
            }
        }
    }

    private void drawDynamic(Canvas canvas) {
        if (progress < 100){
            progress += speed;
        }
        //根据进度画圆弧
        canvas.drawArc(rectF, 235, 360 * progress / 100, false, mPaint);

        int line1_start = 3 * mWidth / 10;
        int line2_startX = 7 * mWidth / 10;


        //绘制×
        if (progress == 100) {
            if ((line1_x + line1_start) <= line2_startX) {
                line1_x += speed;
                line1_y += speed;
            }
            //画第一根线
            canvas.drawLine(line1_start, line1_start,
                    line1_start + line1_x, line1_start + line1_y, mPaint);

            if (line1_x == 2 * mWidth / 5) {
                line1_x++;
                line1_y++;
            }

            if (line1_x >= 2 * mWidth / 5 && (line2_startX - line2_y) >= line1_start) {
                line2_x -= speed;
                line2_y += speed;
            }
            //画第二根线
            canvas.drawLine(line2_startX, line1_start,
                    line2_startX + line2_x, line1_start + line2_y, mPaint);

            if ((line2_startX - line2_y) < line1_start) {
                //1.只分发一次绘制完成的事件
                //2.只在最后一次绘制时分发
                if (count == 0 && times == 0 && listener != null) {
                    listener.dispatchFinishEvent(this);
                    count++;
                }
                times--;
                if (times >= 0) {
                    reDraw();
                    invalidate();
                } else {
                    return;
                }
            }
        }
        invalidate();
    }

    private void drawStatic(Canvas canvas) {
        canvas.drawArc(rectF, 0, 360, false, mPaint);

        int line1_start = 3 * mWidth / 10;
        int line2_startX = 7 * mWidth / 10;

        canvas.drawLine(line1_start, line1_start,
                line1_start + 2 * mWidth / 5, line1_start + 2 * mWidth / 5, mPaint);
        canvas.drawLine(line1_start + 2 * mWidth / 5, line1_start,
                line1_start, line1_start + 2 * mWidth / 5, mPaint);
    }

    private void reDraw() {
        line1_x = 0;
        line2_x = 0;
        line1_y = 0;
        line2_y = 0;
        progress = 0;
    }

    //---------------------------对外提供的api-------------------------//

    /**
     * 设置重复绘制的次数，只在drawEveryTime = true时有效
     *
     * @param times 重复次数，例如设置1，除了第一次绘制还会额外重绘一次
     */
    protected void setRepeatTime(int times) {
        if (drawEveryTime){
            this.times = times;
        }
    }

    /**
     * 动态画出还是直接画出
     */
    protected void setDrawDynamic(boolean drawEveryTime) {
        this.drawEveryTime = drawEveryTime;
    }

    /**
     * 设置速度
     */
    protected void setSpeed(int speed) {
        if (speed <= 0 && speed >= 3) {
            throw new IllegalArgumentException("how can u set this speed??  " + speed + "  do not " +
                    "use reflect to use this method!u can see the LoadingDialog class for how to" +
                    "set the speed");
        } else {
            this.speed = speed;
        }
    }

    protected void setDrawColor(int color) {
        mPaint.setColor(color);
    }

    public void setOnDrawFinishListener(OnFinishListener f) {
        this.listener = f;
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
