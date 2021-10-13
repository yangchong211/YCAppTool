package com.ycbjie.book.fjGame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import java.util.List;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/4/18
 *     desc  : 战斗机类，可以通过交互改变位置
 *     revise:
 * </pre>
 */
public class CombatAircraft extends Sprite {
    private boolean collide = false;//标识战斗机是否被击中
    private int bombAwardCount = 0;//可使用的炸弹数

    //双发子弹相关
    private boolean single = true;//标识是否发的是单一的子弹
    private int doubleTime = 0;//当前已经用双子弹绘制的次数
    private int maxDoubleTime = 140;//使用双子弹最多绘制的次数

    //被撞击后闪烁相关
    private long beginFlushFrame = 0;//要在第beginFlushFrame帧开始闪烁战斗机
    private int flushTime = 0;//已经闪烁的次数
    private int flushFrequency = 16;//在闪烁的时候，每隔16帧转变战斗机的可见性
    private int maxFlushTime = 10;//最大闪烁次数

    public CombatAircraft(Bitmap bitmap){
        super(bitmap);
    }

    @Override
    protected void beforeDraw(Canvas canvas, Paint paint, GameView gameView) {
        if(!isDestroyed()){
            //确保战斗机完全位于Canvas范围内
            validatePosition(canvas);

            //每隔7帧发射子弹
            if(getFrame() % 7 == 0){
                fight(gameView);
            }
        }
    }

    //确保战斗机完全位于Canvas范围内
    private void validatePosition(Canvas canvas){
        if(getX() < 0){
            setX(0);
        }
        if(getY() < 0){
            setY(0);
        }
        RectF rectF = getRectF();
        int canvasWidth = canvas.getWidth();
        if(rectF.right > canvasWidth){
            setX(canvasWidth - getWidth());
        }
        int canvasHeight = canvas.getHeight();
        if(rectF.bottom > canvasHeight){
            setY(canvasHeight - getHeight());
        }
    }

    //发射子弹
    public void fight(GameView gameView){
        //如果战斗机被撞击了或销毁了，那么不会发射子弹
        if(collide || isDestroyed()){
            return;
        }

        float x = getX() + getWidth() / 2;
        float y = getY() - 5;
        if(single){
            //单发模式下发射单发黄色子弹
            Bitmap yellowBulletBitmap = gameView.getYellowBulletBitmap();
            Bullet yellowBullet = new Bullet(yellowBulletBitmap);
            yellowBullet.moveTo(x, y);
            gameView.addSprite(yellowBullet);
        }
        else{
            //双发模式下发射两发蓝色子弹
            float offset = getWidth() / 4;
            float leftX = x - offset;
            float rightX = x + offset;
            Bitmap blueBulletBitmap = gameView.getBlueBulletBitmap();

            Bullet leftBlueBullet = new Bullet(blueBulletBitmap);
            leftBlueBullet.moveTo(leftX, y);
            gameView.addSprite(leftBlueBullet);

            Bullet rightBlueBullet = new Bullet(blueBulletBitmap);
            rightBlueBullet.moveTo(rightX, y);
            gameView.addSprite(rightBlueBullet);

            doubleTime++;
            if(doubleTime >= maxDoubleTime){
                single = true;
                doubleTime = 0;
            }
        }
    }

    //战斗机如果被击中，执行爆炸效果
    //具体来说，首先隐藏战斗机，然后创建爆炸效果，爆炸用28帧渲染完成
    //爆炸效果完全渲染完成后，爆炸效果消失
    //然后战斗机会进入闪烁模式，战斗机闪烁一定次数后销毁
    @Override
    protected void afterDraw(Canvas canvas, Paint paint, GameView gameView){
        if(isDestroyed()){
            return;
        }

        //在飞机当前还没有被击中时，要判断是否将要被敌机击中
        if(!collide){
            List<EnemyPlane> enemies = gameView.getAliveEnemyPlanes();
            for(EnemyPlane enemyPlane : enemies){
                Point p = getCollidePointWithOther(enemyPlane);
                if(p != null){
                    //p为战斗机与敌机的碰撞点，如果p不为null，则表明战斗机被敌机击中
                    explode(gameView);
                    break;
                }
            }
        }

        //beginFlushFrame初始值为0，表示没有进入闪烁模式
        //如果beginFlushFrame大于0，表示要在第如果beginFlushFrame帧进入闪烁模式
        if(beginFlushFrame > 0){
            long frame = getFrame();
            //如果当前帧数大于等于beginFlushFrame，才表示战斗机进入销毁前的闪烁状态
            if(frame >= beginFlushFrame){
                if((frame - beginFlushFrame) % flushFrequency == 0){
                    boolean visible = getVisibility();
                    setVisibility(!visible);
                    flushTime++;
                    if(flushTime >= maxFlushTime){
                        //如果战斗机闪烁的次数超过了最大的闪烁次数，那么销毁战斗机
                        destroy();
                        //Game.gameOver();
                    }
                }
            }
        }

        //在没有被击中的情况下检查是否获得了道具
        if(!collide){
            //检查是否获得炸弹道具
            List<BombAward> bombAwards = gameView.getAliveBombAwards();
            for(BombAward bombAward : bombAwards){
                Point p = getCollidePointWithOther(bombAward);
                if(p != null){
                    bombAwardCount++;
                    bombAward.destroy();
                    //Game.receiveBombAward();
                }
            }

            //检查是否获得子弹道具
            List<BulletAward> bulletAwards = gameView.getAliveBulletAwards();
            for(BulletAward bulletAward : bulletAwards){
                Point p = getCollidePointWithOther(bulletAward);
                if(p != null){
                    bulletAward.destroy();
                    single = false;
                    doubleTime = 0;
                }
            }
        }
    }

    //战斗机爆炸
    private void explode(GameView gameView){
        if(!collide){
            collide = true;
            setVisibility(false);
            float centerX = getX() + getWidth() / 2;
            float centerY = getY() + getHeight() / 2;
            Explosion explosion = new Explosion(gameView.getExplosionBitmap());
            explosion.centerTo(centerX, centerY);
            gameView.addSprite(explosion);
            beginFlushFrame = getFrame() + explosion.getExplodeDurationFrame();
        }
    }

    //获取可用的炸弹数量
    public int getBombCount(){
        return bombAwardCount;
    }

    //战斗机使用炸弹
    public void bomb(GameView gameView){
        if(collide || isDestroyed()){
            return;
        }

        if(bombAwardCount > 0){
            List<EnemyPlane> enemyPlanes = gameView.getAliveEnemyPlanes();
            for(EnemyPlane enemyPlane : enemyPlanes){
                enemyPlane.explode(gameView);
            }
            bombAwardCount--;
        }
    }

    public boolean isCollide(){
        return collide;
    }

    public void setNotCollide(){
        collide = false;
    }
}