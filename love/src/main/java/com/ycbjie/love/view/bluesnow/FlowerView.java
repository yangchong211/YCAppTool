package com.ycbjie.love.view.bluesnow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;


import com.ycbjie.love.R;
import com.ycbjie.love.util.DeviceInfo;

import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/05/23
 *     desc  : 雪花效果
 *     revise: 出处:http://blog.csdn.net/yayun0516/article/details/49488701
 * </pre>
 */
public class FlowerView extends View {
    float de;
    MyFlower[] flowers;
    Matrix m;
    Bitmap mFlowers;
    int mH;
    int mW;
    private Integer[] offsetX;
    private Integer[] offsetY;
    Paint p;
    Random r;

    class MyFlower {
        int a;
        int g;
        float s;
        int t;
        int x;
        int y;

        public void init() {
            float aa = FlowerView.this.r.nextFloat();
            this.x = FlowerView.this.r.nextInt(FlowerView.this.mW - 80) + 80;
            this.y = 0;
            if (aa >= 1.0f) {
                this.s = 1.1f;
            } else if (((double) aa) <= 0.2d) {
                this.s = 0.4f;
            } else {
                this.s = aa;
            }
            this.a = FlowerView.this.r.nextInt(155) + 100;
            this.t = FlowerView.this.r.nextInt(100) + 1;
            this.g = FlowerView.this.offsetY[FlowerView.this.r.nextInt(4)].intValue();
        }

        public MyFlower() {
            init();
        }
    }

    public void setWH(int pW, int pH, float de) {
        this.mW = pW;
        this.mH = pH;
        this.de = de;
        this.offsetX = new Integer[]{(int) (2.0f * de), (int) (-2.0f * de),
                (int) (-1.0f * de), 0, (int) (1.0f * de), (int) (2.0f * de), (int) (1.0f * de)};
        this.offsetY = new Integer[]{(int) (3.0f * de), (int) (5.0f * de),
                (int) (5.0f * de), (int) (3.0f * de), (int) (4.0f * de)};
    }

    public FlowerView(Context context) {
        super(context);
        this.mFlowers = null;
        this.flowers = new MyFlower[100];
        this.r = new Random();
        this.m = new Matrix();
        this.p = new Paint();
        this.mW = DeviceInfo.mScreenWidthForPortrait;
        this.mH = DeviceInfo.mScreenHeightForPortrait;
        this.de = 0.0f;
    }

    public FlowerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mFlowers = null;
        this.flowers = new MyFlower[100];
        this.r = new Random();
        this.m = new Matrix();
        this.p = new Paint();
        this.mW = DeviceInfo.mScreenWidthForPortrait;
        this.mH = DeviceInfo.mScreenHeightForPortrait;
        this.de = 0.0f;
    }

    public FlowerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mFlowers = null;
        this.flowers = new MyFlower[100];
        this.r = new Random();
        this.m = new Matrix();
        this.p = new Paint();
        this.mW = DeviceInfo.mScreenWidthForPortrait;
        this.mH = DeviceInfo.mScreenHeightForPortrait;
        this.de = 0.0f;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < this.flowers.length; i++) {
            MyFlower rect = this.flowers[i];
            int t = rect.t - 1;
            if (t <= 0) {
                rect.y += rect.g;
                canvas.save();
                this.m.reset();
                this.m.setScale(rect.s, rect.s);
                canvas.setMatrix(this.m);
                this.p.setAlpha(rect.a);
                canvas.drawBitmap(this.mFlowers, (float) rect.x, (float) rect.y, this.p);
                canvas.restore();
            }
            rect.t = t;
            if (rect.y >= this.mH) {
                rect.init();
            }
            if (rect.x >= this.mW || rect.x < -20) {
                rect.init();
            }
            this.flowers[i] = rect;
        }
    }

    @SuppressLint("CheckResult")
    public void loadFlower() {
        Observable.just(R.mipmap.pink_snow)
                .map(new Function<Integer, Bitmap>() {
                    @Override
                    public Bitmap apply(Integer integer) throws Exception {
                        return ((BitmapDrawable)
                                FlowerView.this.getResources().getDrawable(integer)).getBitmap();
                    }
                })
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        FlowerView.this.mFlowers = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 3, bitmap.getHeight() / 3, true);
                    }
                });
    }

    public void recly() {
        if (this.mFlowers != null && !this.mFlowers.isRecycled()) {
            this.mFlowers = null;
        }
    }

    public void addRect() {
        for (int i = 0; i < this.flowers.length; i++) {
            this.flowers[i] = new MyFlower();
        }
    }

    public void inva() {
        invalidate();
    }
}
