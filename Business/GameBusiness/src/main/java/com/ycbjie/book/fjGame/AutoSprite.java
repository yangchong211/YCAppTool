package com.ycbjie.book.fjGame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/4/18
 *     desc  : 走直线的Sprite类，其位置只能直上直下
 *     revise:
 * </pre>
 */
public class AutoSprite extends Sprite {
    //每帧移动的像素数,以向下为正
    private float speed = 2;

    public AutoSprite(Bitmap bitmap){
        super(bitmap);
    }

    public void setSpeed(float speed){
        this.speed = speed;
    }

    public float getSpeed(){
        return speed;
    }

    @Override
    protected void beforeDraw(Canvas canvas, Paint paint, GameView gameView) {
        if(!isDestroyed()){
            //在y轴方向移动speed像素
            move(0, speed * gameView.getDensity());
        }
    }

    @Override
    protected void afterDraw(Canvas canvas, Paint paint, GameView gameView){
        if(!isDestroyed()){
            //检查Sprite是否超出了Canvas的范围，如果超出，则销毁Sprite
            RectF canvasRecF = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
            RectF spriteRecF = getRectF();
            if(!RectF.intersects(canvasRecF, spriteRecF)){
                destroy();
            }
        }
    }
}