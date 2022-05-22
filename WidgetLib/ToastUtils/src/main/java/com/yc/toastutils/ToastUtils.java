package com.yc.toastutils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import java.lang.ref.SoftReference;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/06/4
 *     desc  : Toast工具类
 *     revise: 具体看GitHub开源项目：https://github.com/yangchong211/YCDialog
 * </pre>
 */
public final class ToastUtils {


    @SuppressLint("StaticFieldLeak")
    private static Application mApp;
    private static int toastBackColor;

    /**
     * 初始化吐司工具类
     *
     * @param app 应用
     */
    public static void init(@NonNull final Application app) {
        mApp = app;
        toastBackColor = mApp.getResources().getColor(R.color.color_000000);
    }

    public static Application getApp() {
        return mApp;
    }

    public static void setToastBackColor(@ColorInt int color) {
        toastBackColor = color;
    }

    /**
     * 私有构造
     */
    private ToastUtils() {
        //避免初始化
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 检查上下文不能为空，必须先进性初始化操作
     */
    private static void checkContext() {
        if (mApp == null) {
            throw new NullPointerException("ToastUtils context is not null，please first init");
        }
    }

    /**
     * 某些系统可能屏蔽通知
     * 1:检查 SystemUtils.isEnableNotification(BaseApplication.getApplication());
     * 2:替代方案 SnackBarUtils.showSnack(topActivity, noticeStr);
     * 圆角
     * 屏幕中间
     *
     * @param notice 内容
     */
    public static void showRoundRectToast(CharSequence notice) {
        checkMainThread();
        checkContext();
        if (TextUtils.isEmpty(notice)) {
            return;
        }
        new Builder(mApp)
                .setDuration(Toast.LENGTH_SHORT)
                .setFill(false)
                .setGravity(Gravity.CENTER)
                .setOffset(0)
                .setTitle(notice)
                .setTextColor(Color.WHITE)
                .setBackgroundColor(toastBackColor)
                .setRadius(dip2px(mApp, 10))
                .setElevation(dip2px(mApp, 0))
                .build()
                .show();
    }


    public static void showRoundRectToast(CharSequence notice, CharSequence desc) {
        checkMainThread();
        checkContext();
        if (TextUtils.isEmpty(notice)) {
            return;
        }
        new Builder(mApp)
                .setDuration(Toast.LENGTH_SHORT)
                .setFill(false)
                .setGravity(Gravity.CENTER)
                .setOffset(0)
                .setDesc(desc)
                .setTitle(notice)
                .setTextColor(Color.WHITE)
                .setBackgroundColor(toastBackColor)
                .setRadius(dip2px(mApp, 10))
                .setElevation(dip2px(mApp, 0))
                .build()
                .show();
    }


    public static void showRoundRectToast(@LayoutRes int layout) {
        checkMainThread();
        checkContext();
        if (layout == 0) {
            return;
        }
        new Builder(mApp)
                .setDuration(Toast.LENGTH_SHORT)
                .setFill(false)
                .setGravity(Gravity.CENTER)
                .setOffset(0)
                .setLayout(layout)
                .build()
                .show();
    }


    public static final class Builder {

        private final Context context;
        private CharSequence title;
        private CharSequence desc;
        private int gravity = Gravity.TOP;
        private boolean isFill;
        private int yOffset;
        private int duration = Toast.LENGTH_SHORT;
        private int textColor = Color.WHITE;
        private int backgroundColor = Color.BLACK;
        private float radius;
        private int elevation;
        private int layout;


        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder setDesc(CharSequence desc) {
            this.desc = desc;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setFill(boolean fill) {
            isFill = fill;
            return this;
        }

        public Builder setOffset(int yOffset) {
            this.yOffset = yOffset;
            return this;
        }

        public Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setRadius(float radius) {
            this.radius = radius;
            return this;
        }

        public Builder setElevation(int elevation) {
            this.elevation = elevation;
            return this;
        }

        public Builder setLayout(@LayoutRes int layout) {
            this.layout = layout;
            return this;
        }

        /**
         * 采用软引用管理toast对象
         * 如果一个对象只具有软引用，那么如果内存空间足够，垃圾回收器就不会回收它；
         * 如果内存空间不足了，就会回收这些对象的内存。只要垃圾回收器没有回收它，该对象就可以被程序使用。
         */
        private SoftReference<Toast> mToast;

        public Toast build() {
            if (!checkNull(mToast)) {
                mToast.get().cancel();
            }
            Toast toast = new Toast(context);
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                //android 7.1.1 版本
                HookToast.hook(toast);
            }
            if (isFill) {
                toast.setGravity(gravity | Gravity.FILL_HORIZONTAL, 0, yOffset);
            } else {
                toast.setGravity(gravity, 0, yOffset);
            }
            toast.setDuration(duration);
            toast.setMargin(0, 0);
            if (layout == 0) {
                CardView rootView = (CardView) LayoutInflater.from(context).inflate(R.layout.view_toast_custom, null);
                TextView textView = rootView.findViewById(R.id.toastTextView);
                TextView descTv = rootView.findViewById(R.id.desc);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //rootView.setElevation(elevation);
                    rootView.setCardElevation(elevation);
                }
                rootView.setRadius(radius);
                rootView.setCardBackgroundColor(backgroundColor);
                //rootView.setBackgroundColor(backgroundColor);
                textView.setTextColor(textColor);
                textView.setText(title);
                if (TextUtils.isEmpty(desc)) {
                    descTv.setVisibility(View.GONE);
                } else {
                    descTv.setText(desc);
                    descTv.setVisibility(View.VISIBLE);
                }
                toast.setView(rootView);
            } else {
                View view = LayoutInflater.from(context).inflate(layout, null);
                toast.setView(view);
            }
            mToast = new SoftReference<>(toast);
            return toast;
        }
    }


    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    
    private static boolean checkNull(SoftReference softReference) {
        if (softReference == null || softReference.get() == null) {
            return true;
        }
        return false;
    }

    public static void checkMainThread(){
        if (!isMainThread()){
            throw new IllegalStateException("请不要在子线程中做弹窗操作");
        }
    }

    private static boolean isMainThread(){
        return Looper.getMainLooper() == Looper.myLooper();
    }

}
