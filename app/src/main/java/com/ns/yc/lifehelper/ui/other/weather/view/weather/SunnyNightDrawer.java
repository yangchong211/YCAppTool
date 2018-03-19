package com.ns.yc.lifehelper.ui.other.weather.view.weather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;

import com.ns.yc.lifehelper.R;

import java.util.ArrayList;

public class SunnyNightDrawer extends BaseDrawer {

    private ArrayList<SunnyNightHolder> holders = new ArrayList<SunnyNightHolder>();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Context context;
    Bitmap bg;

    public SunnyNightDrawer(Context context, boolean isNight) {
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
        for (final SunnyNightHolder holder : holders) {
            holder.updateRandom(canvas, holder.matrix, paint,alpha);
        }
        return true;
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        if (this.holders.size() == 0) {
            for (int i = 0; i < 4; i++) {
                SunnyNightHolder holder = new SunnyNightHolder(context, width, height, new Matrix(), new Matrix(), new Paint(Paint.ANTI_ALIAS_FLAG)
                        , new Paint(Paint.ANTI_ALIAS_FLAG), i);
                holders.add(holder);
            }
        }
    }

    public static class SunnyNightHolder {
        float initPositionX;
        float initPositionY;
        Bitmap frame;
        Bitmap star;
        RectF box;
        RectF targetBox;
        RectF starBox;
        RectF starTargetBox;
        int width;
        int height;
        int position = 0;
        float initX;
        protected Matrix matrix;
        protected Matrix matrixStar;
        Paint paintStar;
        Paint paint1;
        private Context context;

        public boolean isStar = false;//星星的渐变标识
        public boolean isAdd = true;//渐变标识
        public boolean isOver = false;
        public int paintAlpha = 255;//透明度
        private int number_of_times = 6;
        public int paintStarAlpha = 0;
        public SunnyNightHolder(Context context, int width, int height, Matrix matrix, Matrix matrixStar, Paint paint
                , Paint paintStar, int i) {
            super();
            this.context=context;
            this.paint1 = paint;
            this.paintStar = paintStar;
            this.position = i;
            this.height = height;
            this.width = width;
            this.matrix = matrix;
            this.matrixStar = matrixStar;
            box = new RectF();
            targetBox = new RectF();
            //星星坐标
            starBox = new RectF();
            starTargetBox = new RectF();

            if (i == 0) {
                initPositionX = width * 1F;
                initPositionY = height * 0.0F;
                frame = BitmapFactory.decodeResource(context.getResources(), R.drawable.sunny_night_shooting_start);

                star = BitmapFactory.decodeResource(context.getResources(), R.drawable.sunny_night_star_l);
                starBox.set(0, 0, star.getWidth(), star.getHeight());
                initX = initPositionX + targetBox.width() / 2;
            } else if (i == 1) {
                initPositionX = width * 0.6F;
                initPositionY = height * 0.1F;
                frame = BitmapFactory.decodeResource(context.getResources(), R.drawable.sunny_night_star_l);
            } else if (i == 2) {
                initPositionX = width * 0.72F;
                initPositionY = height * 0.2F;
                frame = BitmapFactory.decodeResource(context.getResources(), R.drawable.sunny_night_star_l);
            } else if (i == 3) {
                initPositionX = width * 0.01F;
                initPositionY = height * 0.7F;
                frame = BitmapFactory.decodeResource(context.getResources(), R.drawable.sunny_night_seawater);
            }

            box.set(0, 0, frame.getWidth(), frame.getHeight());
            matrix.reset();
            if (i == 0) {
                matrix.setScale(2f, 2f);
                matrix.setRotate(45f);

                matrixStar.reset();
                matrixStar.setScale(2f, 2f);
                matrixStar.mapRect(starTargetBox, starBox);
                matrixStar.setTranslate(width - height / 4 - targetBox.bottom -77, height / 4 );
                paintStar.setAlpha(0);
            } else if (i == 1 || i == 2 || i == 3) {
                matrix.setScale(2f, 2f);
            }

            matrix.mapRect(targetBox, box);
            matrix.postTranslate(initPositionX - targetBox.width() / 2, initPositionY - targetBox.height() / 2);

        }

        public void updateRandom(Canvas canvas, Matrix matrix, Paint paint, float alphabg) {
            if (position == 0) {
                //移动
                matrix.postTranslate(-10f, 10f);
                //边界处理
                matrix.mapRect(targetBox, box);
                if (targetBox.top > height / 4) {
                    //回退
                    //消失.出现星闪一下,消失
                    matrix.postTranslate(initX - targetBox.right, -targetBox.bottom);

                    number_of_times--;
                    if (number_of_times == 0) {
                        number_of_times = 6;
                        isOver = false;
                    }
                }
                if (number_of_times == 5) {
                    if(targetBox.top > height/8&&targetBox.top < height/4){
                        if(!isOver){
                            if(paintAlpha-20>0){
                                paintAlpha-=20;
                            }else{
                                paintAlpha=0;
                                isStar=true;
                                isAdd=true;
                                isOver=true;//代表结束一次循环操作
                                Message msg= Message.obtain();
                                msg.obj=this;
                                msg.what=1;
                                handler1.sendMessage(msg);
                            }
                            paint1.setAlpha(paintAlpha);
                        }

                    } else {
                        paintAlpha = 255;
                        paint1.setAlpha(255);
                    }
                } else {
                    paint1.setAlpha(0);
                }
                canvas.drawBitmap(star, matrixStar, paintStar);
            } else if (position == 1) {
                if (isAdd) {
                    if (paintAlpha <= 254) {
                        paintAlpha += 10;
                        if (paintAlpha > 255) {
                            paintAlpha = 255;
                        }
                    } else {
                        isAdd = false;
                    }
                } else {
                    if (paintAlpha >= 50) {
                        paintAlpha -= 10;
                    } else {
                        isAdd = true;
                    }

                }

                paint1.setAlpha(paintAlpha);
            } else if (position == 2) {
                if (isAdd) {
                    if (paintAlpha <= 254) {
                        paintAlpha += 8;
                        if (paintAlpha > 255) {
                            paintAlpha = 255;
                        }
                    } else {
                        isAdd = false;
                    }
                } else {
                    if (paintAlpha >= 50) {
                        paintAlpha -= 8;
                    } else {
                        isAdd = true;
                    }

                }

                paint1.setAlpha(paintAlpha);
            } else if (position == 3) {
                matrix.postTranslate(1.5F, 0);
                //边界处理--不断移动中，将box（当前）的坐标赋值给targetBox
                matrix.mapRect(targetBox, box);
                //当距离过半的时候就是停止重新来过
                if (targetBox.right > width * 2 / 3) {
                    matrix.postTranslate(targetBox.left * 3 / 2, 0);
                }

                //渐变透明
                //获取移动的距离---宽度---width*2/3
                //前1/3从模糊到清晰，后1/3从清晰到消失
                if (targetBox.right > 0 && targetBox.right < (width * 2 / 3 - 120)) {

                    if (targetBox.right - frame.getWidth() > 0 && targetBox.right - frame.getWidth() < 255) {
                        //移动的距离
                        paintAlpha = (int) (targetBox.right - frame.getWidth()) % 255;
                    } else if (targetBox.right - frame.getWidth() > 255) {
                        paintAlpha = 255;
                    } else {
                        //未负数的时候就是重置
                        paintAlpha = 0;
                    }


                } else if (targetBox.right > 0 && targetBox.right < width * 2 / 3) {
                    if ((int) (255 - (targetBox.right - frame.getWidth()) % 255) > 200) {

                    } else {
                        paintAlpha = (int) (255 - (targetBox.right - frame.getWidth()) % 255);
                    }

                }

                paint1.setAlpha(paintAlpha);
            }

            if(alphabg<1){
                //说明是还在渐变
                paint1.setAlpha((int) (alphabg*255));
            }else if(alphabg==1){
                //不做任何操作'
//                if(paint1.getAlpha()!=255){
//                    paint1.setAlpha(255);
//                }
            }


            canvas.drawBitmap(frame, matrix, paint1);
        }

        public void updateStar(){
            if(isAdd){
                if(paintStarAlpha+50<=254){
                    paintStarAlpha+=50;
                }else {
                    paintStarAlpha=255;
                    isAdd=false;
                }
            }else{
                if(paintStarAlpha-50>0){
                    paintStarAlpha-=50;
                }else{
                    paintStarAlpha=0;
                    isStar=false;
                }

            }

            paintStar.setAlpha(paintStarAlpha);

            if(isStar){
                Message msg= Message.obtain();
                msg.obj=this;
                msg.what=1;
                handler1.sendMessageDelayed(msg,100);
            }
        }



    }


    public static Handler handler1=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    SunnyNightHolder s= (SunnyNightHolder) msg.obj;
                    s.updateStar();
                    break;

            }

        }
    };
}
