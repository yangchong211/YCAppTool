package com.ycbjie.book.fjGame;

import android.graphics.Bitmap;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/4/18
 *     desc  : 小敌机类，体积小，抗打击能力低
 *     revise:
 * </pre>
 */
public class SmallEnemyPlane extends EnemyPlane {

    public SmallEnemyPlane(Bitmap bitmap){
        super(bitmap);
        setPower(1);//小敌机抗抵抗能力为1，即一颗子弹就可以销毁小敌机
        setValue(1000);//销毁一个小敌机可以得1000分
    }

}