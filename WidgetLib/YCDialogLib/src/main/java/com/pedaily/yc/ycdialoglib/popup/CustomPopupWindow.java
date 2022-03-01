package com.pedaily.yc.ycdialoglib.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.pedaily.yc.ycdialoglib.utils.DialogUtils;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/1/5
 *     desc  : 自定义PopupWindow控件【学习Builder模式，可以借鉴AlertDialog.Builder模式】
 *     revise:
 * </pre>
 */
public class CustomPopupWindow implements PopupWindow.OnDismissListener {

    private Context mContext;
    private int mResLayoutId;
    private View mContentView;
    private int mAnimationStyle;
    private int mWidth;
    private int mHeight;
    private boolean mIsFocusable;
    private boolean mIsOutside;
    private PopupWindow mPopupWindow;
    private boolean mClippEnable;
    private boolean mIgnoreCheekPress;
    private int mInputMode;
    private int mSoftInputMode;
    private boolean mTouchable;
    private boolean mIsBackgroundDark;
    private float mBackgroundDrakValue;
    private View.OnTouchListener mOnTouchListener;
    private PopupWindow.OnDismissListener mOnDismissListener;
    private Window mWindow;

    /**
     * 第一步，继承PopupWindow.OnDismissListener，实现该方法
     */
    @Override
    public void onDismiss() {
        this.dismiss();
    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        if(this.mOnDismissListener != null) {
            this.mOnDismissListener.onDismiss();
        }

        if(this.mWindow != null) {
            WindowManager.LayoutParams params = this.mWindow.getAttributes();
            params.alpha = 1.0F;
            this.mWindow.setAttributes(params);
        }

        if(this.mPopupWindow != null && this.mPopupWindow.isShowing()) {
            this.mPopupWindow.dismiss();
        }
    }

    /**
     * 第四步
     * 构造方法[私有化]
     * @param context       上下文
     */
    private CustomPopupWindow(Context context) {
        this.mResLayoutId = -1;
        this.mAnimationStyle = -1;
        this.mIsFocusable = true;
        this.mIsOutside = true;
        this.mClippEnable = true;
        this.mIgnoreCheekPress = false;
        this.mInputMode = -1;
        this.mSoftInputMode = -1;
        this.mTouchable = true;
        this.mIsBackgroundDark = false;
        this.mBackgroundDrakValue = 0.0F;
        this.mContext = context;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    /**
     * 第二步：创建具体的Builder类
     */
    public static class PopupWindowBuilder{

        private CustomPopupWindow mCustomPopWindow;

        /**第三步：构造方法*/
        public PopupWindowBuilder(Context context) {
            this.mCustomPopWindow = new CustomPopupWindow(context);
        }

        /**第四步：创建create方法*/
        public CustomPopupWindow create() {
            this.mCustomPopWindow.build();
            return this.mCustomPopWindow;
        }

        /**-----------------------第六步：以下是设置相关操作-----------------------------*/
        /**
         * 设置布局
         * @param resLayoutId       资源文件
         */
        public CustomPopupWindow.PopupWindowBuilder setView(int resLayoutId) {
            this.mCustomPopWindow.mResLayoutId = resLayoutId;
            this.mCustomPopWindow.mContentView = null;
            return this;
        }

        /**
         * 设置布局
         * @param view              view
         */
        public CustomPopupWindow.PopupWindowBuilder setView(View view) {
            this.mCustomPopWindow.mContentView = view;
            this.mCustomPopWindow.mResLayoutId = -1;
            return this;
        }

        /**
         * 设置动画
         * @param animationStyle   资源文件
         */
        public CustomPopupWindow.PopupWindowBuilder setAnimationStyle(int animationStyle) {
            this.mCustomPopWindow.mAnimationStyle = animationStyle;
            return this;
        }

        /**
         * 设置大小
         * @param width             宽
         * @param height            高
         */
        public CustomPopupWindow.PopupWindowBuilder size(int width, int height) {
            this.mCustomPopWindow.mWidth = width;
            this.mCustomPopWindow.mHeight = height;
            return this;
        }

        /**
         * 设置是否可以设置焦点
         * @param focusable         布尔
         */
        public CustomPopupWindow.PopupWindowBuilder setFocusable(boolean focusable) {
            this.mCustomPopWindow.mIsFocusable = focusable;
            return this;
        }

        /**
         * 设置是否可以点击弹窗外部消失
         * @param outsideTouchable  布尔
         */
        public CustomPopupWindow.PopupWindowBuilder setOutsideTouchable(boolean outsideTouchable) {
            this.mCustomPopWindow.mIsOutside = outsideTouchable;
            return this;
        }

        /**
         * 设置是否可以裁剪
         * @param enable            布尔
         */
        public CustomPopupWindow.PopupWindowBuilder setClippingEnable(boolean enable) {
            this.mCustomPopWindow.mClippEnable = enable;
            return this;
        }

        /**
         * 设置是否忽略按下
         * @param ignoreCheekPress  布尔
         */
        public CustomPopupWindow.PopupWindowBuilder setIgnoreCheekPress(boolean ignoreCheekPress) {
            this.mCustomPopWindow.mIgnoreCheekPress = ignoreCheekPress;
            return this;
        }

        /**
         * 设置类型
         * @param mode              类型
         */
        public CustomPopupWindow.PopupWindowBuilder setInputMethodMode(int mode) {
            this.mCustomPopWindow.mInputMode = mode;
            return this;
        }

        /**
         * 设置弹窗关闭监听
         * @param onDismissListener listener
         */
        public CustomPopupWindow.PopupWindowBuilder setOnDissmissListener(PopupWindow.OnDismissListener onDismissListener) {
            this.mCustomPopWindow.mOnDismissListener = onDismissListener;
            return this;
        }

        /**
         * 设置类型
         * @param softInputMode
         */
        public CustomPopupWindow.PopupWindowBuilder setSoftInputMode(int softInputMode) {
            this.mCustomPopWindow.mSoftInputMode = softInputMode;
            return this;
        }

        /**
         * 设置是否可以触摸
         * @param touchable     布尔
         */
        public CustomPopupWindow.PopupWindowBuilder setTouchable(boolean touchable) {
            this.mCustomPopWindow.mTouchable = touchable;
            return this;
        }

        /**
         * 设置触摸拦截
         * @param touchIntercepter  拦截器
         */
        public CustomPopupWindow.PopupWindowBuilder setTouchIntercepter(View.OnTouchListener touchIntercepter) {
            this.mCustomPopWindow.mOnTouchListener = touchIntercepter;
            return this;
        }

        /**
         * 设置是否点击背景变暗
         * @param isDark        布尔
         */
        public CustomPopupWindow.PopupWindowBuilder enableBackgroundDark(boolean isDark) {
            this.mCustomPopWindow.mIsBackgroundDark = isDark;
            return this;
        }

        /**
         * 设置背景
         * @param darkValue     值
         */
        public CustomPopupWindow.PopupWindowBuilder setBgDarkAlpha(float darkValue) {
            this.mCustomPopWindow.mBackgroundDrakValue = darkValue;
            return this;
        }


    }

    /**第四步：创建create方法，待实现*/
    private PopupWindow build() {
        //判断添加的view是否为null
        if(this.mContentView == null) {
            //如果view为null，则获取的资源布局文件
            this.mContentView = LayoutInflater.from(this.mContext).
                    inflate(this.mResLayoutId, null);
        }

        //获取Activity对象
        Activity activity = (Activity)this.mContentView.getContext();
        if(activity != null && this.mIsBackgroundDark) {
            //设置背景透明度
            float alpha = this.mBackgroundDrakValue > 0.0F && this.mBackgroundDrakValue < 1.0F
                    ? this.mBackgroundDrakValue:0.7F;
            this.mWindow = activity.getWindow();
            if(this.mWindow!=null){
                WindowManager.LayoutParams lp = this.mWindow.getAttributes();
                lp.alpha = alpha;
                if (alpha == 1) {
                    //不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
                    this.mWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                } else {
                    //此行代码主要是解决在华为手机上半透明效果无效的bug
                    this.mWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                }
                this.mWindow.setAttributes(lp);
            }
        }

        //设置宽高
        if(this.mWidth != 0 && this.mHeight != 0) {
            this.mPopupWindow = new PopupWindow(this.mContentView, this.mWidth, this.mHeight);
        } else {
            this.mPopupWindow = new PopupWindow(this.mContentView, -2, -2);
        }

        //设置动画
        if(this.mAnimationStyle != -1) {
            this.mPopupWindow.setAnimationStyle(this.mAnimationStyle);
        }

        this.apply(this.mPopupWindow);
        //设置是否捕获焦点，默认为true
        this.mPopupWindow.setFocusable(this.mIsFocusable);
        //设置背景，默认是全透明
        this.mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
        //设置是否可以点击外部，默认是true
        this.mPopupWindow.setOutsideTouchable(this.mIsOutside);
        if(this.mWidth == 0 || this.mHeight == 0) {
            this.mPopupWindow.getContentView().measure(0, 0);
            this.mWidth = this.mPopupWindow.getContentView().getMeasuredWidth();
            this.mHeight = this.mPopupWindow.getContentView().getMeasuredHeight();
        }

        //实现关闭监听
        this.mPopupWindow.setOnDismissListener(this);
        this.mPopupWindow.update();
        return this.mPopupWindow;
    }

    private void apply(PopupWindow mPopupWindow) {
        mPopupWindow.setClippingEnabled(this.mClippEnable);
        if(this.mIgnoreCheekPress) {
            mPopupWindow.setIgnoreCheekPress();
        }

        if(this.mInputMode != -1) {
            mPopupWindow.setInputMethodMode(this.mInputMode);
        }

        if(this.mSoftInputMode != -1) {
            mPopupWindow.setSoftInputMode(this.mSoftInputMode);
        }

        if(this.mOnDismissListener != null) {
            mPopupWindow.setOnDismissListener(this.mOnDismissListener);
        }

        if(this.mOnTouchListener != null) {
            mPopupWindow.setTouchInterceptor(this.mOnTouchListener);
        }

        mPopupWindow.setTouchable(this.mTouchable);
    }

    /**----------------设置弹窗显示，添加可以设置弹窗位置---------------------------------------------*/
    /**
     * 直接展示
     */
    public CustomPopupWindow showAsDropDown(View anchor) {
        DialogUtils.checkMainThread();
        if(this.mPopupWindow != null) {
            this.mPopupWindow.showAsDropDown(anchor);
        }
        return this;
    }

    /**
     * 传入x，y值位置展示
     */
    public CustomPopupWindow showAsDropDown(View anchor, int xOff, int yOff) {
        DialogUtils.checkMainThread();
        if(this.mPopupWindow != null) {
            this.mPopupWindow.showAsDropDown(anchor, xOff, yOff);
        }
        return this;
    }

    /**
     * 传入x，y值，和gravity位置展示。是相对坐标的gravity位置
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public CustomPopupWindow showAsDropDown(View anchor, int xOff, int yOff, int gravity) {
        DialogUtils.checkMainThread();
        if(this.mPopupWindow != null) {
            this.mPopupWindow.showAsDropDown(anchor, xOff, yOff, gravity);
        }
        return this;
    }

    /**
     * 传入x，y值，和gravity位置展示。是相对gravity的相对位置
     */
    public CustomPopupWindow showAtLocation(View parent, int gravity, int x, int y) {
        DialogUtils.checkMainThread();
        if(this.mPopupWindow != null) {
            this.mPopupWindow.showAtLocation(parent, gravity, x, y);
        }
        return this;
    }

}
