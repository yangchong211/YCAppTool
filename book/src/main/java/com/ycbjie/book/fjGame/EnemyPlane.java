package com.ycbjie.book.fjGame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.List;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/4/18
 *     desc  : 敌机类，从上向下沿直线运动
 *     revise:
 * </pre>
 */
public class EnemyPlane extends AutoSprite {

    private int power = 1;//敌机的抗打击能力
    private int value = 0;//打一个敌机的得分

    public EnemyPlane(Bitmap bitmap){
        super(bitmap);
    }

    public void setPower(int power){
        this.power = power;
    }

    public int getPower(){
        return power;
    }

    public void setValue(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    @Override
    protected void afterDraw(Canvas canvas, Paint paint, GameView gameView) {
        super.afterDraw(canvas, paint, gameView);

        //绘制完成后要检查自身是否被子弹打中
        if(!isDestroyed()){
            //敌机在绘制完成后要判断是否被子弹打中

            List<Bullet> bullets = gameView.getAliveBullets();
            for(Bullet bullet : bullets){
                //判断敌机是否与子弹相交
                Point p = getCollidePointWithOther(bullet);
                if(p != null){
                    //如果有交点，说明子弹打到了飞机上
                    bullet.destroy();
                    power--;
                    if(power <= 0){
                        //敌机已经没有能量了，执行爆炸效果
                        explode(gameView);
                        return;
                    }
                }
            }
        }
    }

    //创建爆炸效果后会销毁敌机
    public void explode(GameView gameView){
        //创建爆炸效果
        float centerX = getX() + getWidth() / 2;
        float centerY = getY() + getHeight() / 2;
        Bitmap bitmap = gameView.getExplosionBitmap();
        Explosion explosion = new Explosion(bitmap);
        explosion.centerTo(centerX, centerY);
        gameView.addSprite(explosion);

        //创建爆炸效果完成后，向GameView中添加得分并销毁敌机
        gameView.addScore(value);
        destroy();
    }
}