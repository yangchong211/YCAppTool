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


public class CloudyNightDrawer extends BaseDrawer {

    private ArrayList<CloudyNightHolder> holders = new ArrayList<CloudyNightHolder>();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Context context;
    Bitmap bg;


    public CloudyNightDrawer(Context context, boolean isNight) {
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
        for (CloudyNightHolder holder : holders) {
            holder.updateRandom(canvas, holder.matrix, new Paint(Paint.ANTI_ALIAS_FLAG),alpha);
        }

        //渐变实现,取消渐变功能
        return true;
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        if (this.holders.size() == 0) {
            for (int i = 0; i < 9; i++) {
                CloudyNightHolder holder;
                if (i <= 1) {
                    holder = new CloudyNightHolder(context, width, height, new Matrix(), i, i, i);
                } else if (i > 1 && i < 4) {
                    holder = new CloudyNightHolder(context, width, height, new Matrix(), i, i - 1, i - 1);
                } else if (i >= 4 && i <= 6) {
                    holder = new CloudyNightHolder(context, width, height, new Matrix(), i, 10 - i, i / 2);
                } else {
                    holder = new CloudyNightHolder(context, width, height, new Matrix(), i, i, i - 6);
                }
                holders.add(holder);
            }
        }
    }

    public static class CloudyNightHolder {
        float initPositionX;
        float initPositionY;
        Bitmap frame;
        RectF box;
        RectF targetBox;
        int width;
        int position = 0;
        protected Matrix matrix;
        int randomWidth;
        int randomHeight;
        private int addInt = 0;
        public int paintAlpha = 50;//透明度
        public boolean isAdd = true;//标识在加


        public CloudyNightHolder(Context context, int width, int height, Matrix matrix, int i, int randomWidth, int randomHeight) {
            super();
            this.randomHeight = randomHeight;
            this.randomWidth = randomWidth;
            this.position = i;
            this.width = width;
            this.matrix = matrix;
            box = new RectF();
            targetBox = new RectF();

            if (i == 0) {
                initPositionX = width * 0.758F;
                initPositionY = height * 0.49F;
                frame = BitmapFactory.decodeResource(context.getResources(), R.drawable.sunny_day_cloud3);
            } else if (i == 1) {
                initPositionX = width * 0.039F;
                initPositionY = height * 0.49F;
                frame = BitmapFactory.decodeResource(context.getResources(), R.drawable.sunny_day_cloud3);
            } else {
                initPositionX = width * randomWidth / 10F;
                initPositionY = height * randomHeight / 10F;
                frame = BitmapFactory.decodeResource(context.getResources(), R.drawable.sunny_night_star_l);
                addInt = randomWidth + 5;
            }

            box.set(0, 0, frame.getWidth(), frame.getHeight());
            matrix.reset();
            if (i == 0 || i == 1) {
                matrix.setScale(6f, 6f);
            } else {
                matrix.setScale(2f, 2f);
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
            } else {
                if (isAdd) {
                    if (paintAlpha <= 254) {
                        paintAlpha += addInt;
                        if (paintAlpha > 255) {
                            paintAlpha = 255;
                        }
                    } else {
                        isAdd = false;
                    }
                } else {
                    if (paintAlpha >= 50) {
                        paintAlpha -= addInt;
                    } else {
                        isAdd = true;
                    }

                }

                paint.setAlpha(paintAlpha);
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

            //绘制
            canvas.drawBitmap(frame, matrix, paint);
        }
    }
}
