package com.yc.animbusiness;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2022/5/17
 *     desc   : 补间动画
 *     revise :
 * </pre>
 */
public class TweenAnimation extends AppCompatActivity implements View.OnClickListener {


    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_anim1);


        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        imageView = findViewById(R.id.iv_image);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btn1){
            testTranslate();
        } else if (v == btn2){
            testAlpha();
        }else if (v == btn3){
            testRotate();
        }else if (v == btn4){
            testScale();
        }
    }


    /**
     * 渐变透明度动画效果
     */
    private void testAlpha(){
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        //设置持续时间
        alpha.setDuration(500);
        //动画结束后保留结束状态
        alpha.setFillAfter(true);
        //添加差值器
        alpha.setInterpolator(new AccelerateInterpolator());
        imageView.setAnimation(alpha);
    }

    /**
     * 渐变尺寸伸缩动画效果
     */
    private void testScale(){
        ScaleAnimation scale = new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(300);
        scale.setFillAfter(true);
        imageView.setAnimation(scale);
    }

    /**
     * 画面转换位置移动动画效果
     */
    private void testTranslate(){
        TranslateAnimation translate = new TranslateAnimation(30,30,100,100);
        translate.setDuration(300);
        translate.setFillAfter(true);
        imageView.setAnimation(translate);
    }


    /**
     * 画面转换位置移动动画效果
     */
    private void testRotate(){
        RotateAnimation rotate = new RotateAnimation(50, 50, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        imageView.setAnimation(rotate);
        //rotate.start();
//        imageView.startAnimation(rotate);
        imageView.clearAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     *     - mStartTime：动画实际开始时间
     *     - mStartOffset：动画延迟时间
     *     - mFillEnabled：mFillBefore及mFillAfter是否使能
     *     - mFillBefore：动画结束之后是否需要进行应用动画
     *     - mFillAfter：动画开始之前是否需要进行应用动画
     *     - mDuration：单次动画运行时长
     *     - mRepeatMode：动画重复模式（RESTART、REVERSE）
     *     - mRepeatCount：动画重复次数（INFINITE，直接值）
     *     - mInterceptor：动画插间器
     *     - mBackgroundColor：动画背景颜色
     *     - mListener：动画开始、结束、重复回调监听器
     */
}

