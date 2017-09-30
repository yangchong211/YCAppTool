package com.ns.yc.lifehelper.ui.weight;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.ns.yc.lifehelper.R;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/4/21
 * 描    述：全局加载弹窗
 * 修订历史：
 * ================================================
 */
public abstract class ViewLoading extends Dialog {

    public abstract void loadCancel();

    public ViewLoading(Context context) {
        super(context, R.style.Loading);
        // 加载布局
        setContentView(R.layout.dialog_toast_view);
        ImageView progressImageView = (ImageView) findViewById(R.id.iv_image);
        //创建旋转动画
        Animation animation =new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(2000);
        animation.setRepeatCount(10);//动画的重复次数
        animation.setFillAfter(true);//设置为true，动画转化结束后被应用
        progressImageView.startAnimation(animation);//开始动画
        // 设置Dialog参数
        Window window = getWindow();
        if(window!=null){
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
        }

    }

    // 封装Dialog消失的回调
    @Override
    public void onBackPressed() {
        //回调
        loadCancel();
        //关闭Loading
        dismiss();
    }

}
