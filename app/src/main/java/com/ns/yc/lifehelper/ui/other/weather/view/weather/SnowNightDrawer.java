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


public class SnowNightDrawer extends BaseDrawer {

    private ArrayList<SnowNightHolder> holders = new ArrayList<SnowNightHolder>();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Context context;
    Bitmap bg;


    public SnowNightDrawer(Context context, boolean isNight) {
        super(context, false);
        this.context = context;
        if (bg == null) {
            bg = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_snow_night);
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
        for (SnowNightHolder holder : holders) {
            holder.updateRandom(canvas, holder.matrix, paint,alpha);
        }
        return true;
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        if (this.holders.size() == 0) {
            for (int i = 0; i < 80; i++) {
                SnowNightHolder holder = new SnowNightHolder(context, width, height, new Matrix(), i);
                holders.add(holder);
            }
        }
    }

    public static class SnowNightHolder{
        float initPositionX;
        float initPositionY;
        Bitmap frame;
        RectF box;
        RectF targetBox;
        int width;
        int height;
        int position = 0;
        Random random=new Random();
        int randomInt;
        protected Matrix matrix ;
        public int[] bitmaps = {
                R.drawable.snowflake_l,
                R.drawable.snowflake_m,
                R.drawable.snowflake_xl,
                R.drawable.snowflake_xxl,
                R.drawable.snowflake_l,
                R.drawable.snowflake_m,
                R.drawable.snowflake_xl,
                R.drawable.snowflake_xxl,
                R.drawable.snowflake_l,
                R.drawable.snowflake_m,
                R.drawable.snowflake_xl,
                R.drawable.snowflake_xxl,
        };
        public SnowNightHolder(Context context, int width, int height, Matrix matrix, int i) {
            super();
            this.position = i;
            this.width = width;
            this.matrix=matrix;
            this.height=height;
            box = new RectF();
            targetBox = new RectF();
            initPositionX = width * 0.15F*(position%12==0?1:position%12);
            randomInt=random.nextInt(80);
            initPositionY = height * 1.0F*0.05f*(randomInt%20==0?1:randomInt%20);
            frame = BitmapFactory.decodeResource(context.getResources(),bitmaps[randomInt%12]);
            box.set(0, 0, frame.getWidth(), frame.getHeight());
            matrix.reset();
            matrix.setScale(2f, 2f);
            matrix.mapRect(targetBox, box);
            matrix.postTranslate(initPositionX - targetBox.width() / 2, initPositionY - targetBox.height() / 2);
        }

        public void updateRandom(Canvas canvas, Matrix matrix, Paint paint, float alpha) {
            matrix.postTranslate(0, (randomInt%3f==0?1.5f:randomInt%3f));
            //边界处理
            matrix.mapRect(targetBox, box);
            if (targetBox.top > height) {
                matrix.postTranslate(0, -targetBox.bottom);
            }
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
