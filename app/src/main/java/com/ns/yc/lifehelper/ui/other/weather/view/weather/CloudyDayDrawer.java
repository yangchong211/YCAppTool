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

public class CloudyDayDrawer extends BaseDrawer {

    private ArrayList<CloudyDayHolder> holders = new ArrayList<>();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Context context;
    Bitmap bg;


    public CloudyDayDrawer(Context context, boolean isNight) {
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
        for (CloudyDayHolder holder : holders) {
            holder.updateRandom(canvas, holder.matrix, paint,alpha);
        }
        return true;
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        if (this.holders.size() == 0) {
            for (int i = 0; i < 3; i++) {
                CloudyDayHolder holder = new CloudyDayHolder(context, width, height, new Matrix(), i);
                holders.add(holder);
            }
        }
    }

    public static class CloudyDayHolder {

        float initPositionX;
        float initPositionY;
        Bitmap frame;
        RectF box;
        RectF targetBox;
        int width;
        int position = 0;
        protected Matrix matrix;

        public CloudyDayHolder(Context context, int width, int height, Matrix matrix, int i) {
            super();
            this.position = i;
            this.width = width;
            this.matrix = matrix;
            box = new RectF();
            targetBox = new RectF();

            if (i == 0) {
                initPositionX = width * 0.039F;
                initPositionY = height * 0.49F;
                frame = BitmapFactory.decodeResource(context.getResources(), R.drawable.fog_day_fog_top);
            } else if (i == 1) {
                initPositionX = width * 0.039F;
                initPositionY = height * 0.69F;
                frame = BitmapFactory.decodeResource(context.getResources(), R.drawable.fine_day_cloud1);
            } else if (i == 2) {
                initPositionX = width * 0.758F;
                initPositionY = height * 0.69F;
                frame = BitmapFactory.decodeResource(context.getResources(), R.drawable.fine_day_cloud1);
            }

            box.set(0, 0, frame.getWidth(), frame.getHeight());
            matrix.reset();
            if (i == 0) {
                matrix.setScale(2f, 2f);
            } else if (i == 1||i==2) {
                matrix.setScale(3f, 3f);
            }
            matrix.mapRect(targetBox, box);
            matrix.postTranslate(initPositionX - targetBox.width() / 2, initPositionY - targetBox.height() / 2);
        }


        public void updateRandom(Canvas canvas, Matrix matrix, Paint paint, float alpha) {
            matrix.postTranslate(0.5F, 0);
            //边界处理
            matrix.mapRect(targetBox, box);
            if (targetBox.left > width) {
                matrix.postTranslate(-targetBox.right, 0);
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
