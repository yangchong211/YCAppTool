package com.yc.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import androidx.annotation.RequiresApi;



/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211/YCDialog
 *     time  : 2017/1/5
 *     desc  : 自定义PopupWindow控件【借鉴AlertDialog.Builder模式】
 *     revise:
 *     GitHub: https://github.com/yangchong211/YCDialog
 * </pre>
 */
public class CustomPopupWindow implements PopupWindow.OnDismissListener {

    private final Context mContext;
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
    private boolean mEnableOutsideTouchDisMiss;
    private View.OnTouchListener mOnTouchListener;
    private PopupWindow.OnDismissListener mOnDismissListener;
    private Window mWindow;

    /**
     * 第一步，继承PopupWindow.OnDismissListener，实现该方法
     */
    @Override
    public void onDismiss() {
        try {
            this.dismiss();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        if(this.mOnDismissListener != null) {
            this.mOnDismissListener.onDismiss();
        }

        if (this.mWindow != null && mWindow.getAttributes().alpha != 1.0f) {
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
        this.mEnableOutsideTouchDisMiss = true;
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

        private final CustomPopupWindow mCustomPopWindow;

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
        public PopupWindowBuilder setView(int resLayoutId) {
            this.mCustomPopWindow.mResLayoutId = resLayoutId;
            this.mCustomPopWindow.mContentView = null;
            return this;
        }

        /**
         * 设置布局
         * @param view              view
         */
        public PopupWindowBuilder setView(View view) {
            this.mCustomPopWindow.mContentView = view;
            this.mCustomPopWindow.mResLayoutId = -1;
            return this;
        }

        /**
         * 设置动画
         * @param animationStyle   资源文件
         */
        public PopupWindowBuilder setAnimationStyle(int animationStyle) {
            this.mCustomPopWindow.mAnimationStyle = animationStyle;
            return this;
        }

        /**
         * 设置大小
         * @param width             宽
         * @param height            高
         */
        public PopupWindowBuilder size(int width, int height) {
            this.mCustomPopWindow.mWidth = width;
            this.mCustomPopWindow.mHeight = height;
            return this;
        }

        /**
         * 设置是否可以设置焦点
         * @param focusable         布尔
         */
        public PopupWindowBuilder setFocusable(boolean focusable) {
            this.mCustomPopWindow.mIsFocusable = focusable;
            return this;
        }

        /**
         * 设置是否可以点击弹窗外部消失
         * @param outsideTouchable  布尔
         */
        public PopupWindowBuilder setOutsideTouchable(boolean outsideTouchable) {
            this.mCustomPopWindow.mIsOutside = outsideTouchable;
            return this;
        }

        /**
         * 设置是否可以裁剪
         * @param enable            布尔
         */
        public PopupWindowBuilder setClippingEnable(boolean enable) {
            this.mCustomPopWindow.mClippEnable = enable;
            return this;
        }

        /**
         * 设置是否忽略按下
         * @param ignoreCheekPress  布尔
         */
        public PopupWindowBuilder setIgnoreCheekPress(boolean ignoreCheekPress) {
            this.mCustomPopWindow.mIgnoreCheekPress = ignoreCheekPress;
            return this;
        }

        /**
         * 设置类型
         * @param mode              类型
         */
        public PopupWindowBuilder setInputMethodMode(int mode) {
            this.mCustomPopWindow.mInputMode = mode;
            return this;
        }

        /**
         * 设置弹窗关闭监听
         * @param onDismissListener listener
         */
        public PopupWindowBuilder setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
            this.mCustomPopWindow.mOnDismissListener = onDismissListener;
            return this;
        }

        /**
         * 设置类型
         * @param softInputMode
         */
        public PopupWindowBuilder setSoftInputMode(int softInputMode) {
            this.mCustomPopWindow.mSoftInputMode = softInputMode;
            return this;
        }

        /**
         * 设置是否可以触摸
         * @param touchable     布尔
         */
        public PopupWindowBuilder setTouchable(boolean touchable) {
            this.mCustomPopWindow.mTouchable = touchable;
            return this;
        }

        /**
         * 设置触摸拦截
         * @param touchIntercepter  拦截器
         */
        public PopupWindowBuilder setTouchIntercepter(View.OnTouchListener touchIntercepter) {
            this.mCustomPopWindow.mOnTouchListener = touchIntercepter;
            return this;
        }

        /**
         * 设置是否点击背景变暗
         * @param isDark        布尔
         */
        public PopupWindowBuilder enableBackgroundDark(boolean isDark) {
            this.mCustomPopWindow.mIsBackgroundDark = isDark;
            return this;
        }

        /**
         * 设置背景
         * @param darkValue     值
         */
        public PopupWindowBuilder setBgDarkAlpha(float darkValue) {
            this.mCustomPopWindow.mBackgroundDrakValue = darkValue;
            return this;
        }

        /**
         * 设置是否允许点击 PopupWindow之外的地方，关闭PopupWindow
         * @param disMiss          默认是true
         * @return
         */
        public PopupWindowBuilder setEnableOutsideTouchable(boolean disMiss){
            this.mCustomPopWindow.mEnableOutsideTouchDisMiss = disMiss;
            return this;
        }
    }

    /**第四步：创建create方法，待实现*/
    private PopupWindow build() {
        //判断添加的view是否为null
        if(this.mContentView == null) {
            //如果view为null，则获取的资源布局文件
            LinearLayout linearLayout = new LinearLayout(mContext);
            this.mContentView = LayoutInflater.from(this.mContext).
                    inflate(this.mResLayoutId, linearLayout);
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
            //设置自适应布局
            //设置PopupWindow的宽高
            //自定义View的时候，最外层的布局设置的宽高无效
            //在外面必须手动设置宽度和高度，并且以外面设置的宽高为主
            mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        //设置动画
        if(this.mAnimationStyle != -1) {
            this.mPopupWindow.setAnimationStyle(this.mAnimationStyle);
        }

        this.apply(this.mPopupWindow);
        //设置是否捕获焦点，默认为true
        //说明PopupWindow不能获得焦点，即使设置设置了背景不为空也不能点击外面消失
        //只能由dismiss()消失，但是外面的View的事件还是可以触发,back键也可以顺利dismiss掉。
        this.mPopupWindow.setFocusable(this.mIsFocusable);
        //设置背景，默认是全透明
        this.mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
        //设置是否可以点击外部，默认是true
        //设置显示PopupWindow之后在外面点击是否有效。如果为false的话，那么点击PopupWindow外面并不会关闭PopupWindow
        this.mPopupWindow.setOutsideTouchable(this.mIsOutside);
        //测量获取popup的宽高
        if(this.mWidth == 0 || this.mHeight == 0) {
            this.mPopupWindow.getContentView().measure(0, 0);
            this.mWidth = this.mPopupWindow.getContentView().getMeasuredWidth();
            this.mHeight = this.mPopupWindow.getContentView().getMeasuredHeight();
        }

        // fix setOutsideTouchable 失效问题，点击穿透问题
        // 判断是否点击PopupWindow之外的地方关闭 popWindow
        if(!mEnableOutsideTouchDisMiss){
            //注意这三个属性必须同时设置，不然不能disMiss，以下三行代码在Android 4.4 上是可以，然后在Android 6.0以上，下面的三行代码就不起作用了，就得用下面的方法
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(false);
            mPopupWindow.setBackgroundDrawable(null);
            //注意下面这三个是contentView 不是PopupWindow
            mPopupWindow.getContentView().setFocusable(true);
            mPopupWindow.getContentView().setFocusableInTouchMode(true);
            mPopupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        mPopupWindow.dismiss();
                        return true;
                    }
                    return false;
                }
            });
            //在Android 6.0以上 ，只能通过拦截事件来解决
            mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int x = (int) event.getX();
                    final int y = (int) event.getY();
                    if ((event.getAction() == MotionEvent.ACTION_DOWN)
                            && ((x < 0) || (x >= mWidth) || (y < 0) || (y >= mHeight))) {
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        return true;
                    }
                    return false;
                }
            });
        }else{
            mPopupWindow.setFocusable(mIsFocusable);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mPopupWindow.setOutsideTouchable(mIsOutside);
        }

        //实现关闭监听
        this.mPopupWindow.setOnDismissListener(this);
        this.mPopupWindow.update();
        return this.mPopupWindow;
    }

    private void apply(PopupWindow mPopupWindow) {
        //设置为false表示不裁剪
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
        PopupUtils.checkMainThread();
        if(this.mPopupWindow != null) {
            this.mPopupWindow.showAsDropDown(anchor);
        }
        return this;
    }

    /**
     * 设置popup弹窗点击事件
     * @param clickListener             点击listener
     * @return
     */
    public CustomPopupWindow setViewClickListener(View.OnClickListener clickListener){
        if(this.mPopupWindow != null) {
            View contentView = this.mPopupWindow.getContentView();
            if (contentView!=null){
                contentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickListener!=null){
                            clickListener.onClick(v);
                        }
                    }
                });
            }
        }
        return this;
    }

    /**
     * 传入x，y值位置展示
     * 设置显示PopupWindow的位置位于View的左下方，x,y表示坐标偏移量
     */
    public CustomPopupWindow showAsDropDown(View anchor, int xOff, int yOff) {
        PopupUtils.checkMainThread();
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
        PopupUtils.checkMainThread();
        if(this.mPopupWindow != null) {
            this.mPopupWindow.showAsDropDown(anchor, xOff, yOff, gravity);
        }
        return this;
    }

    /**
     * 传入x，y值，和gravity位置展示。是相对gravity的相对位置
     */
    public CustomPopupWindow showAtLocation(View parent, int gravity, int x, int y) {
        PopupUtils.checkMainThread();
        if(this.mPopupWindow != null) {
            this.mPopupWindow.showAtLocation(parent, gravity, x, y);
        }
        return this;
    }

}
