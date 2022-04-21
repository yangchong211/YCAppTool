package com.pedaily.yc.ycdialoglib.toast;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
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

import com.pedaily.yc.ycdialoglib.utils.DialogUtils;
import com.pedaily.yc.ycdialoglib.R;

import java.lang.ref.SoftReference;
import java.lang.reflect.Field;

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
        DialogUtils.checkMainThread();
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
                .setRadius(DialogUtils.dip2px(mApp, 10))
                .setElevation(DialogUtils.dip2px(mApp, 0))
                .build()
                .show();
    }


    public static void showRoundRectToast(CharSequence notice, CharSequence desc) {
        DialogUtils.checkMainThread();
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
                .setRadius(DialogUtils.dip2px(mApp, 10))
                .setElevation(DialogUtils.dip2px(mApp, 0))
                .build()
                .show();
    }


    public static void showRoundRectToast(@LayoutRes int layout) {
        DialogUtils.checkMainThread();
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
            if (!DialogUtils.checkNull(mToast)) {
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


    /**
     * <pre>
     *     @author yangchong
     *     email  : yangchong211@163.com
     *     time  : 20120/5/6
     *     desc  : 利用hook解决toast崩溃问题
     *              https://www.jianshu.com/p/437f473017d6
     *     revise: 7.1.1Toast崩溃解决方案
     *             首先Toast显示依赖于一个窗口，这个窗口被WMS管理（WindowManagerService），
     *             当需要show的时候这个请求会放在WMS请求队列中，并且会传递一个TN类型的Bider对象给WMS，
     *             WMS并生成一个token传递给Android进行显示与隐藏，但是如果UI线程的某个线程发生了阻塞，
     *             并且已经NotificationManager检测已经超时就不删除token记录，此时token已经过期，
     *             阻塞结束的时候再显示的时候就发生了异常。
     * </pre>
     */
    public static class HookToast {

        private static Field sField_TN;
        private static Field sField_TN_Handler;

        static {
            try {
                Class<?> clazz = Toast.class;
                //通过反射拿到，获取class对象的指定属性，拿到tn对象
                sField_TN = clazz.getDeclaredField("mTN");
                sField_TN.setAccessible(true);
                //然后通过反射拿到Toast中内部类TN的mHandler
                sField_TN_Handler = sField_TN.getType().getDeclaredField("mHandler");
                sField_TN_Handler.setAccessible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void hook(Toast toast) {
            try {
                Object tn = sField_TN.get(toast);
                Handler preHandler = (Handler) sField_TN_Handler.get(tn);
                sField_TN_Handler.set(tn, new HookToast.SafelyHandler(preHandler));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static class SafelyHandler extends Handler {

            private final Handler impl;

            public SafelyHandler(Handler impl) {
                this.impl = impl;
            }

            public void dispatchMessage(Message msg) {
                try {
                    // 捕获这个异常，避免程序崩溃
                    super.dispatchMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void handleMessage(Message msg) {
                //需要委托给原Handler执行
                impl.handleMessage(msg);
            }
        }
    }

}
