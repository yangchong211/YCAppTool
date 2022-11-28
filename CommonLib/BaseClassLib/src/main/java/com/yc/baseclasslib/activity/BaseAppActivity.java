package com.yc.baseclasslib.activity;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     email : yangchong211@163.com
 *     time  : 2018/11/9
 *     desc  : 父类activity
 *     revise:
 * </pre>
 */
public abstract class BaseAppActivity extends AppCompatActivity {

    /**
     * 正常生命周期
     *
     *
     * 旋转生命周期
     * 第一种情况：当前的Activity不销毁。【设置Activity的android:configChanges="orientation|keyboardHidden|screenSize"时，切屏不会重新调用各个生命周期，只会执行onConfigurationChanged方法】
     * 第二种情况：销毁当前的Activity后重建，这种也尽量避免。【不设置Activity的android:configChanges时，切屏会重新调用各个生命周期，默认首先销毁当前activity,然后重新加载】
     *
     *
     * 异常生命周期
     * onSaveInstanceState：保存数据
     * onRestoreInstanceState：恢复数据
     */

    /**
     * 表示Activity 正在创建，常做初始化工作，如setContentView界面资源、初始化数据。
     * onCreate()方法其实也有一个Bundle类型的参数。这个参数在一般情况下都是null，
     * 当活动被系统回收之前有通过 onSaveInstanceState()方法来保存数据的话，这个参就会带有之前所保存的全部数据
     * @param savedInstanceState    bundle
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 表示Activity 正在启动，这时Activity 可见但不在前台，无法和用户交互
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 表示Activity 获得焦点，此时Activity 可见且在前台并开始活动
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 表示Activity 正在停止，可做 数据存储、停止动画等操作
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 表示activity 即将停止，可做稍微重量级回收工作，如取消网络连接、注销广播接收器等
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 表示当Activity由后台切换到前台，由不可见到可见时会调用，表示Activity 重新启动
     */
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /**
     * 表示Activity 即将销毁，常做回收工作、资源释放
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 屏幕旋转时候，如果不做任何处理，activity会经过销毁到重建的过程。一般这种效果都不是想要的。
     * 比如视频播放器就经常会涉及屏幕旋转场景。
     * @param newConfig     config
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 当非人为终止Activity时，比如系统配置发生改变时导致Activity被杀死并重新创建、资源内存不足导致低优先级的Activity被杀
     * 会调用 onSavaInstanceState() 来保存状态。该方法调用在onStop之前，但和onPause没有时序关系。
     * onSaveInstanceState()与onPause()的区别，onSaveInstanceState()适用于对临时性状态的保存，而onPause()适用于对数据的持久化保存。
     * @param outState      bundle
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 当异常崩溃后App又重启了，会执行该方法，可以在该方法中取出onSaveInstanceState()保存的状态数据。
     * @param savedInstanceState    bundle
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * 当与活动关联的主窗口已附加到窗口管理器时调用
     */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /**
     * 当与活动关联的主窗口已从窗口管理器分离时调用。
     */
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

}
