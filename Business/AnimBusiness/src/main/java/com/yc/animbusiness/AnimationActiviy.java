package com.yc.animbusiness;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.animbusiness.reveal.RevealAnimationActivity;

public class AnimationActiviy extends AppCompatActivity {

    private TextView tvAnim1;
    private TextView tvAnim2;
    private TextView tvAnim3;
    private TextView tvAnim4;
    private TextView tvAnim5;
    private TextView tvAnim6;
    private TextView tvAnim7;
    private TextView tvAnim8;

    /**
     * 开启页面
     *
     * @param context 上下文
     */
    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, AnimationActiviy.class);
            context.startActivity(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim_main);

        tvAnim1 = findViewById(R.id.tv_anim_1);
        tvAnim2 = findViewById(R.id.tv_anim_2);
        tvAnim3 = findViewById(R.id.tv_anim_3);
        tvAnim4 = findViewById(R.id.tv_anim_4);
        tvAnim5 = findViewById(R.id.tv_anim_5);
        tvAnim6 = findViewById(R.id.tv_anim_6);
        tvAnim7 = findViewById(R.id.tv_anim_7);
        tvAnim8 = findViewById(R.id.tv_anim_8);

        tvAnim1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AnimationActiviy.this,FrameAnimation.class));
            }
        });
        tvAnim2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AnimationActiviy.this,TweenAnimation.class));
            }
        });
        tvAnim3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AnimationActiviy.this,ValueAnimatorAct.class));
            }
        });
        tvAnim4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(AnimationActiviy.this,FrameAnimation.class));
            }
        });
        tvAnim5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AnimationActiviy.this,OtherAnimation.class));
            }
        });
        tvAnim6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AnimationActiviy.this,AnimMainActivity.class));
            }
        });
        tvAnim7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AnimationActiviy.this,PropertyAnimatorAct.class));
            }
        });
        tvAnim8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AnimationActiviy.this, RevealAnimationActivity.class));
            }
        });
    }


}
