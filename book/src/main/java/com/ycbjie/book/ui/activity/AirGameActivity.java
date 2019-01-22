package com.ycbjie.book.ui.activity;



import android.graphics.Color;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ycbjie.book.R;
import com.ycbjie.book.fjGame.GameView;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.base.mvp.BaseActivity;

import cn.ycbjie.ycstatusbarlib.bar.StateAppBar;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/9/11
 *     desc  : 飞机大战游戏
 *     revise:
 * </pre>
 */
@Route(path = ARouterConstant.ACTIVITY_OTHER_AIR_ACTIVITY)
public class AirGameActivity extends BaseActivity {

    private GameView gameView;

    @Override
    public int getContentView() {
        return R.layout.activity_air_game;
    }

    @Override
    public void initView() {
        StateAppBar.setStatusBarLightMode(this, Color.WHITE);
        gameView = findViewById(R.id.gameView);
        //0:combatAircraft
        //1:explosion
        //2:yellowBullet
        //3:blueBullet
        //4:smallEnemyPlane
        //5:middleEnemyPlane
        //6:bigEnemyPlane
        //7:bombAward
        //8:bulletAward
        //9:pause1
        //10:pause2
        //11:bomb
        int[] bitmapIds = {
                R.drawable.plane,
                R.drawable.explosion,
                R.drawable.yellow_bullet,
                R.drawable.blue_bullet,
                R.drawable.small,
                R.drawable.middle,
                R.drawable.big,
                R.drawable.bomb_award,
                R.drawable.bullet_award,
                R.drawable.pause1,
                R.drawable.pause2,
                R.drawable.bomb
        };
        gameView.start(bitmapIds);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }


    @Override
    protected void onPause() {
        super.onPause();
        if(gameView != null){
            gameView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(gameView != null){
            gameView.destroy();
        }
        gameView = null;
    }


}