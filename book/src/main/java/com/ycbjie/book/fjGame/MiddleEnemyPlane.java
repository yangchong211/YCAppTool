package com.ycbjie.book.fjGame;

import android.graphics.Bitmap;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/4/18
 *     desc  : 中敌机类，体积中等，抗打击能力中等
 *     revise:
 * </pre>
 */
public class MiddleEnemyPlane extends EnemyPlane {

    public MiddleEnemyPlane(Bitmap bitmap){
        super(bitmap);
        setPower(4);//中敌机抗抵抗能力为4，即需要4颗子弹才能销毁中敌机
        setValue(6000);//销毁一个中敌机可以得6000分
    }

}