package com.ycbjie.other.ui.activity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.ycbjie.other.R;

public class AnimActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 开始动画
     */
    private Button mBtn;
    private LottieAnimationView mLottieView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);
        initView();
        startAnim();
    }

    private void initView() {
        mBtn = (Button) findViewById(R.id.btn);
        mBtn.setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        mLottieView = (LottieAnimationView) findViewById(R.id.lottie_view);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn) {
            startAnimating();
        } else if (id == R.id.btn2) {
            stopAnimating();
        }
    }

    @SuppressLint("RestrictedApi")
    private void startAnim(){
        try {
            //None无缓存
            //在assets目录下的动画json文件名。
            mLottieView.setAnimation("121.json");
        } catch (Exception e){
            e.printStackTrace();
        }

//        LottieComposition.fromAssetFileName(this, "hello.json",
//                new LottieComposition.OnCompositionLoadedListener() {
//            @Override
//            public void onCompositionLoaded(LottieComposition composition) {
//                mLottieView.setComposition(composition);
//                mLottieView.setProgress(0.333f);
//                mLottieView.playAnimation();
//            }
//        });

        //设置动画循环播放
        mLottieView.loop(true);
        //assets目录下的子目录，存放动画所需的图片
        //mLottieView.setImageAssetsFolder("image/");
        //播放动画
        //mLottieView.playAnimation();
        //是否在播放
        boolean animating = mLottieView.isAnimating();
        //播放
        //mLottieView.playAnimation();
        //暂停
        //mLottieView.pauseAnimation();
        //取消
        //mLottieView.cancelAnimation();
        //获取动画时长
        mLottieView.getDuration();
        //添加动画监听
        mLottieView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    /**
     * 开始动画
     */
    private  void startAnimating(){
        boolean inPlaying = mLottieView.isAnimating();
        if (!inPlaying) {
            mLottieView.setProgress(0f);
            mLottieView.playAnimation();
        }
    }

    /**
     * 停止动画
     */
    private  void stopAnimating(){
        boolean inPlaying = mLottieView.isAnimating();
        if (inPlaying) {
            mLottieView.cancelAnimation();
        }
    }

    /**
     * 取消动画
     */
    @Override
    protected void onStop() {
        super.onStop();
        stopAnimating();
    }

}
