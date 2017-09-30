package com.ns.yc.lifehelper.ui.other.myMusic.activity;

import android.view.View;
import android.widget.ImageView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.ui.weight.musicView.RippleAnimationView;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/25
 * 描    述：我的音乐识别音乐震动效果页面
 * 修订历史：
 * ================================================
 */
public class MyMusicRippleActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.layout_RippleAnimation)
    RippleAnimationView layoutRippleAnimation;

    @Override
    public int getContentView() {
        return R.layout.activity_my_music_ripple;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {
        imageView.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView:
                if (layoutRippleAnimation.isRippleRunning()) {
                    layoutRippleAnimation.stopRippleAnimation();
                } else {
                    layoutRippleAnimation.startRippleAnimation();
                }
                break;
        }
    }
}
