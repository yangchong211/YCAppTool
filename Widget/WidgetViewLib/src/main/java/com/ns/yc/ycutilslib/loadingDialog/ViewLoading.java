package com.ns.yc.ycutilslib.loadingDialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.ycutilslib.R;


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


    public ViewLoading(Context context ,int type , String content) {
        super(context, R.style.Loading);
        if(type == 1){
            // 加载布局
            setContentView(R.layout.layout_dialog_loading);
            TextView message = (TextView) findViewById(R.id.message);
            if(content!=null && content.length()>0){
                message.setText(content);
            }else {
                message.setText("加载中");
            }
        }else {
            // 加载布局
            setContentView(R.layout.layout_dialog_loaded);
        }
        ImageView progressImageView = (ImageView) findViewById(R.id.iv_image);
        //创建旋转动画
        Animation animation =new RotateAnimation(0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(2000);
        //动画的重复次数
        animation.setRepeatCount(10);
        //设置为true，动画转化结束后被应用
        animation.setFillAfter(true);
        //开始动画
        progressImageView.startAnimation(animation);
        // 设置Dialog参数
        Window window = getWindow();
        if(window!=null){
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
        }
    }

    /**
     * 封装Dialog消失的回调
     */
    @Override
    public void onBackPressed() {
        //回调
        loadCancel();
        //关闭Loading
        dismiss();
    }

    /**
     * 抽象方法，子类继承实现
     * 处理消失后的逻辑
     */
    protected abstract void loadCancel();

}
