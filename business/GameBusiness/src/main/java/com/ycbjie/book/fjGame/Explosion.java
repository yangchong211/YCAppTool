package com.ycbjie.book.fjGame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/4/18
 *     desc  : 爆炸效果类，位置不可变，但是可以显示动态的爆炸效果
 *     revise:
 * </pre>
 */
public class Explosion extends Sprite {

    private int segment = 14;//爆炸效果由14个片段组成
    private int level = 0;//最开始处于爆炸的第0片段
    private int explodeFrequency = 2;//每个爆炸片段绘制2帧

    public Explosion(Bitmap bitmap){
        super(bitmap);
    }

    @Override
    public float getWidth() {
        Bitmap bitmap = getBitmap();
        if(bitmap != null){
            return bitmap.getWidth() / segment;
        }
        return 0;
    }

    @Override
    public Rect getBitmapSrcRec() {
        Rect rect = super.getBitmapSrcRec();
        int left = (int)(level * getWidth());
        rect.offsetTo(left, 0);
        return rect;
    }

    @Override
    protected void afterDraw(Canvas canvas, Paint paint, GameView gameView) {
        if(!isDestroyed()){
            if(getFrame() % explodeFrequency == 0){
                //level自加1，用于绘制下个爆炸片段
                level++;
                if(level >= segment){
                    //当绘制完所有的爆炸片段后，销毁爆炸效果
                    destroy();
                }
            }
        }
    }

    //得到绘制完整爆炸效果需要的帧数，即28帧
    public int getExplodeDurationFrame(){
        return segment * explodeFrequency;
    }
}