package com.yc.animbusiness;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.yc.library.base.mvp.BaseActivity;
import com.yc.toastutils.ToastUtils;

public class PropertyAnimatorAct extends BaseActivity implements View.OnClickListener {

    private Button btn1_1;
    private Button btn1_2;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private ImageView ivImage;


    @Override
    public int getContentView() {
        return R.layout.activity_test_anim2;
    }

    @Override
    public void initView() {
        btn1_1 = findViewById(R.id.btn1_1);
        btn1_2 = findViewById(R.id.btn1_2);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        ivImage = findViewById(R.id.iv_image);
    }

    @Override
    public void initListener() {
        btn1_1.setOnClickListener(this);
        btn1_2.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        ivImage.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        if (v == btn1_1) {
            test1_1();
        } else if (v == btn1_2) {
            test1_2();
        } else if (v == btn2) {
            test2();
        } else if (v == btn3) {
            test3();
        } else if (v == btn4) {
            test4();
        } else if (v == ivImage) {
            ToastUtils.showRoundRectToast("吐司");
        }
    }

    /**
     * 上移动
     */
    private void test1_1() {
        float translationY = ivImage.getTranslationY();
        Log.d("移动 上:" , translationY+"");
        ViewPropertyAnimator viewPropertyAnimator = ivImage.animate();
        viewPropertyAnimator.setInterpolator(new DecelerateInterpolator());
        viewPropertyAnimator.setDuration(3000);
//        viewPropertyAnimator.translationX(500f);// 点击X轴位移,再次点击没有效果
        viewPropertyAnimator.translationY(0);// 点击Y轴位移,再次点击没有效果
//                viewPropertyAnimator.translationXBy(500f);// 点击X轴位移,再次点击刚刚才结束位置开始移动
//                viewPropertyAnimator.translationYBy(500f);// 点击Y轴位移,再次点击刚刚才结束位置开始移动
//                viewPropertyAnimator.x(500f); //X 轴位移,再次点击没有效果
//                viewPropertyAnimator.xBy(500f); //X 轴位移,再次点击有效果
//                viewPropertyAnimator.y(500f); //Y 轴位移,再次点击有效果
//                viewPropertyAnimator.yBy(500f); //Y 轴位移,再次点击有效果

        viewPropertyAnimator.start();
        // 动画变化更新监听,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            viewPropertyAnimator.setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Log.e("onAnimationUpdate", "111");
                }
            });
        }
        // 监听
        viewPropertyAnimator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.e("onAnimationStart", "111");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e("onAnimationEnd", "111");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.e("onAnimationCancel", "111");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.e("onAnimationRepeat", "111");
            }
        });

        //使用简写
//        ivImage.animate()
//                .setInterpolator(new DecelerateInterpolator())
//                .setDuration(2000)
//                .translationX(600f)
//                .start();
    }

    /**
     * 下移动
     */
    private void test1_2() {
        float translationY = ivImage.getTranslationY();
        Log.d("移动 下:" , translationY+"");
        ViewPropertyAnimator viewPropertyAnimator = ivImage.animate();
        viewPropertyAnimator.setInterpolator(new DecelerateInterpolator());
        viewPropertyAnimator.setDuration(3000);
//        viewPropertyAnimator.translationX(500f);// 点击X轴位移,再次点击没有效果
        viewPropertyAnimator.translationY(100f);// 点击Y轴位移,再次点击没有效果
//                viewPropertyAnimator.translationXBy(500f);// 点击X轴位移,再次点击刚刚才结束位置开始移动
//                viewPropertyAnimator.translationYBy(500f);// 点击Y轴位移,再次点击刚刚才结束位置开始移动
//                viewPropertyAnimator.x(500f); //X 轴位移,再次点击没有效果
//                viewPropertyAnimator.xBy(500f); //X 轴位移,再次点击有效果
//                viewPropertyAnimator.y(500f); //Y 轴位移,再次点击有效果
//                viewPropertyAnimator.yBy(500f); //Y 轴位移,再次点击有效果

        viewPropertyAnimator.start();
        // 动画变化更新监听,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            viewPropertyAnimator.setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Log.e("onAnimationUpdate", "111");
                }
            });
        }
        // 监听
        viewPropertyAnimator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.e("onAnimationStart", "111");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e("onAnimationEnd", "111");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.e("onAnimationCancel", "111");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.e("onAnimationRepeat", "111");
            }
        });
    }


    private void test2() {
        ViewPropertyAnimator viewPropertyAnimator = ivImage.animate();
        viewPropertyAnimator.setInterpolator(new DecelerateInterpolator());
        viewPropertyAnimator.setDuration(3000);
        viewPropertyAnimator.alpha(0);
        viewPropertyAnimator.start();

        //使用简写
//                imageView.animate().setInterpolator(new DecelerateInterpolator())
//                        .setDuration(2000)
//                        .alpha(0)
//                        .start();
    }

    private void test3() {
        ViewPropertyAnimator viewPropertyAnimator = ivImage.animate();
        viewPropertyAnimator.setInterpolator(new DecelerateInterpolator());
        viewPropertyAnimator.setDuration(3000);
//                viewPropertyAnimator.rotation(360); // 顺时针旋转,再次点击没有效果
//                viewPropertyAnimator.rotationBy(360);// 顺时针旋转,再次点击有效果
//                viewPropertyAnimator.rotationX(360); // X轴旋转,再次点击没有效果
//                viewPropertyAnimator.rotationXBy(360);// X轴旋转,再次点击有效果
//                viewPropertyAnimator.rotationY(360);// Y轴旋转,再次点击有效果
        viewPropertyAnimator.rotationYBy(360);// Y轴旋转,再次点击有效果
        viewPropertyAnimator.start();

        //使用简写
//                imageView.animate().setInterpolator(new DecelerateInterpolator())
//                        .setDuration(2000)
//                        .rotation(360)
//                        .start();
    }

    private void test4() {
        ViewPropertyAnimator viewPropertyAnimator = ivImage.animate();
        viewPropertyAnimator.setInterpolator(new DecelerateInterpolator());
        viewPropertyAnimator.setDuration(3000);
//                viewPropertyAnimator.scaleX(1.2f); // X轴放大,再次点击没有效果
//                viewPropertyAnimator.scaleXBy(1.2f); // X轴放大,再次点击有效果
//                viewPropertyAnimator.scaleY(1.2f); // Y轴放大,再次点击没有效果
        viewPropertyAnimator.scaleYBy(1.2f); // X轴放大,再次点击有效果
        viewPropertyAnimator.start();

        //使用简写
//                imageView.animate().setInterpolator(new DecelerateInterpolator())
//                        .setDuration(2000)
//                        .scaleX(2f)
//                        .scaleY(2f)
//                        .start();
    }

}
