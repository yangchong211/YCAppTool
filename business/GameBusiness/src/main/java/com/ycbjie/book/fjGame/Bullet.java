package com.ycbjie.book.fjGame;

import android.graphics.Bitmap;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/4/18
 *     desc  : 子弹类，从下向上沿直线移动
 *     revise:
 * </pre>
 */
public class Bullet extends AutoSprite {

    public Bullet(Bitmap bitmap){
        super(bitmap);
        setSpeed(-10);//负数表示子弹向上飞
    }

}