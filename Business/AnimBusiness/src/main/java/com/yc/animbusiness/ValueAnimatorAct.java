package com.yc.animbusiness;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.toastutils.ToastUtils;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2022/5/17
 *     desc   : 属性动画
 *     revise :
 * </pre>
 */
public class ValueAnimatorAct extends AppCompatActivity implements View.OnClickListener {

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_anim3);


        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        imageView = findViewById(R.id.iv_image);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == btn1){
            ToastUtils.showRoundRectToast("ObjectAnimator动画java");
            testObjectAnimatorJava();
        } else if (v == btn2){
            ToastUtils.showRoundRectToast("ObjectAnimator动画xml");
            testObjectAnimatorXml();
        }else if (v == btn3){
            ToastUtils.showRoundRectToast("ValueAnimator动画java");
            testValueAnimatorJava();
        }else if (v == btn4){
            ToastUtils.showRoundRectToast("ValueAnimator动画xml");
            testValueAnimatorXml();
        } else if (v == btn5){
            test1();
        } else if (v == btn6){
            test2();
        }
    }


    private void testValueAnimatorXml(){
        Animator mAnim = AnimatorInflater.loadAnimator(this, R.animator.animator_1_0);
        mAnim.setTarget(imageView);
        mAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //动画开始时执行
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                //动画结束时执行
            }
            @Override
            public void onAnimationCancel(Animator animation) {
                //动画取消时执行
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
                //动画重复时执行
            }
        });
        mAnim.addListener(new AnimatorListenerAdapter() {
            // 向addListener()方法中传入适配器对象AnimatorListenerAdapter()
            // 由于AnimatorListenerAdapter中已经实现好每个接口
            // 所以这里不实现全部方法也不会报错
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                ToastUtils.showRoundRectToast("动画结束了");
            }
        });
        mAnim.start();
    }

    private void testValueAnimatorJava(){
        ValueAnimator valueAnimator = setValueAnimator(imageView,
                0, 2, 2000, 500, 2);
        valueAnimator.start();
    }

    public static ValueAnimator setValueAnimator(View view , int start , int end , int time , int delay , int count){
        // 步骤1：设置动画属性的初始值 & 结束值
        ValueAnimator mAnimator = ValueAnimator.ofInt(start, end);
        // ofInt（）作用有两个
        // 1. 创建动画实例
        // 2. 将传入的多个Int参数进行平滑过渡:此处传入0和1,表示将值从0平滑过渡到1
        // 如果传入了3个Int参数 a,b,c ,则是先从a平滑过渡到b,再从b平滑过渡到C，以此类推
        // ValueAnimator.ofInt()内置了整型估值器,直接采用默认的.不需要设置，即默认设置了如何从初始值 过渡到 结束值
        // 关于自定义插值器我将在下节进行讲解
        // 下面看看ofInt()的源码分析 ->>关注1
        mAnimator.setTarget(view);
        // 步骤2：设置动画的播放各种属性
        mAnimator.setDuration(time);
        // 设置动画运行的时长
        mAnimator.setStartDelay(delay);
        // 设置动画延迟播放时间
        mAnimator.setRepeatCount(count);
        // 设置动画重复播放次数 = 重放次数+1
        // 动画播放次数 = infinite时,动画无限重复
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        // 设置重复播放动画模式
        // ValueAnimator.RESTART(默认):正序重放
        // ValueAnimator.REVERSE:倒序回放
        // 步骤3：将改变的值手动赋值给对象的属性值：通过动画的更新监听器
        // 设置 值的更新监听器
        // 即：值每次改变、变化一次,该方法就会被调用一次
        return mAnimator;
    }

    private void testObjectAnimatorXml(){
        Animator mAnim = AnimatorInflater.loadAnimator(this, R.animator.object_animator);
        mAnim.setTarget(imageView);
        mAnim.start();
    }

    private void testObjectAnimatorJava(){
        ObjectAnimator valueAnimator = setObjectAnimator(imageView,
                "", 2, 2000, 500);
        valueAnimator.start();
    }

    public static ObjectAnimator setObjectAnimator(View view , String type , int start , int end , long time){
        ObjectAnimator mAnimator = ObjectAnimator.ofFloat(view, type, start, end);
        // ofFloat()作用有两个
        // 1. 创建动画实例
        // 2. 参数设置：参数说明如下
        // Object object：需要操作的对象
        // String property：需要操作的对象的属性
        // float ....values：动画初始值 & 结束值（不固定长度）
        // 若是两个参数a,b，则动画效果则是从属性的a值到b值
        // 若是三个参数a,b,c，则则动画效果则是从属性的a值到b值再到c值
        // 以此类推
        // 至于如何从初始值 过渡到 结束值，同样是由估值器决定，此处ObjectAnimator.ofFloat（）是有系统内置的浮点型估值器FloatEvaluator，同ValueAnimator讲解

        // 设置动画重复播放次数 = 重放次数+1
        // 动画播放次数 = infinite时,动画无限重复
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        // 设置动画运行的时长
        mAnimator.setDuration(time);
        // 设置动画延迟播放时间
        mAnimator.setStartDelay(0);
        // 设置重复播放动画模式
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        // ValueAnimator.RESTART(默认):正序重放
        // ValueAnimator.REVERSE:倒序回放
        //设置差值器
        mAnimator.setInterpolator(new LinearInterpolator());
        return mAnimator;
    }

    private void test1() {
        AnimatorSet animator = (AnimatorSet) AnimatorInflater.loadAnimator(
                this, R.animator.value_animator);
        // 创建组合动画对象  &  加载XML动画
        animator.setTarget(imageView);
        // 设置动画作用对象
        animator.start();
        // 启动动画
    }


    private void test2() {
        // 步骤1：设置需要组合的动画效果
        ObjectAnimator translation = ObjectAnimator.ofFloat(imageView, "translationX", 300, 200, 100);
        // 平移动画
        ObjectAnimator rotate = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        // 旋转动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0f, 1f);
        // 透明度动画
        // 步骤2：创建组合动画的对象
        AnimatorSet animSet = new AnimatorSet();
        // 步骤3：根据需求组合动画
        animSet.play(translation).with(rotate).before(alpha);
        //动画同时执行
        //animSet.playTogether(translation, rotate, alpha);

        animSet.setDuration(5000);
        // 步骤4：启动动画
        animSet.start();



        //AnimatorSet.play(Animator anim)   ：播放当前动画
        //AnimatorSet.after(long delay)   ：将现有动画延迟x毫秒后执行
        //AnimatorSet.with(Animator anim)   ：将现有动画和传入的动画同时执行
        //AnimatorSet.after(Animator anim)   ：将现有动画插入到传入的动画之后执行
        //AnimatorSet.before(Animator anim) ：  将现有动画插入到传入的动画之前执行
    }


}
