package com.ycbjie.library.utils.animation;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/31
 * 描    述：属性动画工具类
 * 修订历史：
 * ================================================
 */
public class AnimatorUtils {


    /**
     * 设置属性动画ValueAnimator
     * @param view                      view
     * @param start                     开始值
     * @param end                       结束值
     * @param time                      运行时间
     * @param delay                     延迟播放时间
     * @param count                     重复播放次数
     * @return                          ValueAnimator的对象
     */
    public static ValueAnimator setValueAnimator(View view , int start , int end , int time , int delay , int count){
        // 步骤1：设置动画属性的初始值 & 结束值
        // ValueAnimator.oFloat（）采用默认的浮点型估值器 (FloatEvaluator)
        // ValueAnimator.ofInt（）采用默认的整型估值器（IntEvaluator)
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

    /**
     * 设置属性动画ObjectAnimator
     * @param view                      操作对象
     * @param type                      对象属性
     * @param start                     开始值
     * @param end                       结束值
     * @param time                      运行时长
     * @return                          返回ObjectAnimator对象
     */
    public static ObjectAnimator setObjectAnimator(View view , String type , int start , int end , long time , int delay){
        ObjectAnimator mAnimator = ObjectAnimator.ofFloat(view, type, start, end);
        // ofFloat()作用有两个
        // 1. 创建动画实例
        // 2. 参数设置：参数说明如下
        // Object object：需要操作的对象
        // String property：需要操作的对象的属性
        // property属性值常见有：此处先展示四种基本变换：平移、旋转、缩放 & 透明度   translationX  rotation scaleX alpha
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
        mAnimator.setStartDelay(delay);
        // 设置重复播放动画模式
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        // ValueAnimator.RESTART(默认):正序重放
        // ValueAnimator.REVERSE:倒序回放
        // 设置差值器
        mAnimator.setInterpolator(new LinearInterpolator());
        return mAnimator;
    }

}
