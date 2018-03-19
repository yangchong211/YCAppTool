package com.ns.yc.lifehelper.ui.other.weather.view.weather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.ns.yc.lifehelper.R;

import java.util.ArrayList;
import java.util.Random;

public class RainDayDrawer extends BaseDrawer {

    private ArrayList<RainDayHolder> holders = new ArrayList<RainDayHolder>();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Context context;
    Bitmap bg;


    public RainDayDrawer(Context context, boolean isNight) {
        super(context, false);
        this.context = context;
        if (bg == null) {
            bg = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_cloud_night);
        }
    }

    @Override
    public boolean drawWeather(Canvas canvas, float alpha) {
        if(alpha!=1){
            paint.setAlpha((int)(alpha*255));
        }else{
            paint.setAlpha(255);
        }
        canvas.drawBitmap(bg, new Rect(0, 0, bg.getWidth(), bg.getHeight()), new Rect(0, 0, width, height), paint);
        for (RainDayHolder holder : holders) {
            holder.updateRandom(canvas, holder.matrix, paint,alpha);
        }

        //渐变实现,取消渐变功能


        return true;
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        if (this.holders.size() == 0) {
            for (int i = 0; i < 82; i++) {
                RainDayHolder holder = new RainDayHolder(context, width, height, new Matrix(), i);
                holders.add(holder);
            }
        }
    }

    public static class RainDayHolder{
        float initPositionX;
        float initPositionY;
        Bitmap frame;
        RectF box;
        RectF targetBox;
        int width;
        int height;
        int position = 0;
        protected Matrix matrix ;
        int randomInt;
        Random random=new Random();

        public int[] bitmaps = {
                R.drawable.raindrop_l,
                R.drawable.raindrop_m,
                R.drawable.raindrop_s,
                R.drawable.raindrop_xl,
                R.drawable.raindrop_l,
                R.drawable.raindrop_m,
                R.drawable.raindrop_s,
                R.drawable.raindrop_xl,
                R.drawable.raindrop_l,
                R.drawable.raindrop_m,
                R.drawable.raindrop_s,
                R.drawable.raindrop_xl,
        };

        public RainDayHolder(Context context, int width, int height, Matrix matrix, int i) {
            super();
            this.position = i;
            this.width = width;
            this.height=height;
            this.matrix=matrix;
            box = new RectF();
            targetBox = new RectF();
            if (i == 0) {
                initPositionX = width * 0.039F;
                initPositionY = height * 0.11F;
                frame = BitmapFactory.decodeResource(context.getResources(), R.drawable.moderate_rain_cloud1);
            } else if (i == 1) {
                initPositionX = width * 0.758F;
                initPositionY = height * 0.11F;
                frame = BitmapFactory.decodeResource(context.getResources(), R.drawable.moderate_rain_cloud1);
            }else{
                randomInt=random.nextInt(80);
                initPositionX = width * 0.15F*(position%12==0?1:position%12);
                initPositionY = height * 1.0F*0.05f*(randomInt%20==0?1:randomInt%20);
                frame = BitmapFactory.decodeResource(context.getResources(),bitmaps[randomInt%12]);
            }

            box.set(0, 0, frame.getWidth(), frame.getHeight());
            matrix.reset();
            if (i == 0 || i == 1) {
                matrix.setScale(2f, 2f);
            }else {
                matrix.setScale(6f, 6f);
                matrix.setRotate(-20f);
            }

            matrix.mapRect(targetBox, box);
            matrix.postTranslate(initPositionX - targetBox.width() / 2, initPositionY - targetBox.height() / 2);

        }

        public void updateRandom(Canvas canvas, Matrix matrix, Paint paint, float alpha) {

            if (position == 0 || position == 1) {
                matrix.postTranslate(1.5F, 0);
                //边界处理
                matrix.mapRect(targetBox, box);
                if (targetBox.left > width) {
                    matrix.postTranslate(-targetBox.right, 0);
                }
            }else{
                matrix.postTranslate(10f, 30f);
                //边界处理
                matrix.mapRect(targetBox, box);
                if (targetBox.top > height) {
                    matrix.postTranslate(0, -targetBox.bottom);
                }
                if (targetBox.left > width) {
                    matrix.postTranslate(-targetBox.right, 0);
                }
            }

            //绘制
            if(alpha<1){
                //说明是还在渐变
                paint.setAlpha((int) (alpha*255));
            }else if(alpha==1){
                //不做任何操作'
                if(paint.getAlpha()!=255){
                    paint.setAlpha(255);
                }
            }

            canvas.drawBitmap(frame, matrix, paint);
        }
    }
}
