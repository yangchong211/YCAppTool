package com.pedaily.yc.ycdialoglib.dialog.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.Window;

import com.pedaily.yc.ycdialoglib.R;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/5/2
 *     desc  : dialog的base类
 *     revise:
 * </pre>
 */
public class BaseCustomDialog<T> extends Dialog {

    protected Context mContext;
    // 动画资源id
    protected int mAnimId = -1;
    // 显示位置
    protected int mGravity = -1;
    protected boolean isFullScreen = false;

    public BaseCustomDialog(Context context) {
        super(context, R.style.CustomBottomDialog);
        mContext = context;
    }

    public BaseCustomDialog(Context context ,@StyleRes int style) {
        super(context, style);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Window window = getWindow();
        if (window != null) {
            if (mGravity != -1) {
                window.setGravity(mGravity);
            } else {
                window.setGravity(Gravity.CENTER);
            }
            if (mAnimId != -1) {
                window.setWindowAnimations(mAnimId);
            }
        }
    }

    /**
     * 设置动画样式
     *
     * @param animId
     * @return
     */
    public T setWindowAnimation(int animId) {
        mAnimId = animId;
        return (T) this;
    }


    /**
     * 设置显示位置
     *
     * @param gravity
     * @return
     */
    public T setLocation(int gravity) {
        mGravity = gravity;
        return (T) this;
    }

    /**
     * 设置全屏幕显示
     *
     * @param flag
     * @return
     */
    public T setFullScreen(boolean flag) {
        isFullScreen = flag;
        return (T) this;
    }

    /**
     * 展示弹窗
     */
    @Override
    public void show() {
        if (mContext instanceof Activity) {
            if (((Activity) mContext).isFinishing()) {
                return;
            }
        }
        if (isShowing()){
            return;
        }
        super.show();
    }

    /**
     * 销毁弹窗
     */
    @Override
    public void dismiss() {
        if (mContext == null){
            return;
        }
        if (mContext instanceof Activity){
            if (((Activity) mContext).isFinishing()) {
                return;
            }
        }
        if (!isShowing()){
            return;
        }
        super.dismiss();
    }
}
