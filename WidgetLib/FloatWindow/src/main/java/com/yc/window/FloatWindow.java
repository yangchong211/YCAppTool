package com.yc.window;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.eventuploadlib.ExceptionReporter;
import com.yc.window.config.ParamsConfig;
import com.yc.window.draggable.AbsTouchListener;
import com.yc.window.inter.IClickListener;
import com.yc.window.inter.IFloatView;
import com.yc.window.inter.ILifecycleListener;
import com.yc.window.inter.ILongClickListener;
import com.yc.window.inter.ITouchListener;
import com.yc.window.lifecycle.ActivityLifecycle;
import com.yc.window.view.WindowLayout;
import com.yc.window.wrapper.ViewClickWrapper;
import com.yc.window.wrapper.ViewLongClickWrapper;
import com.yc.window.wrapper.ViewTouchWrapper;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/08/18
 *     desc  : 悬浮控件
 *     revise:
 * </pre>
 */
public class FloatWindow implements IFloatView {

    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 根布局
     */
    private ViewGroup mDecorView;
    /**
     * 悬浮窗口
     */
    private WindowManager mWindowManager;
    /**
     * 窗口参数
     */
    private WindowManager.LayoutParams mWindowParams;

    /**
     * 当前是否已经显示
     */
    private boolean mShowing;
    /**
     * 悬浮窗 生命周期管理
     */
    private ActivityLifecycle mLifecycle;
    /**
     * 自定义拖动处理
     */
    private AbsTouchListener mDraggable;
    /**
     * 吐司显示和取消监听
     */
    private ILifecycleListener mListener;

    /**
     * 创建一个局部悬浮窗
     */
    public FloatWindow(Activity activity) {
        this((Context) activity);
        if ((activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0 ||
                (activity.getWindow().getDecorView().getSystemUiVisibility() & View.SYSTEM_UI_FLAG_FULLSCREEN) != 0) {
            // 如果当前 Activity 是全屏模式，那么需要添加这个标记，否则会导致 WindowManager 在某些机型上移动不到状态栏的位置上
            // 如果不想让状态栏显示的时候把 WindowManager 顶下来，可以添加 FLAG_LAYOUT_IN_SCREEN，但是会导致软键盘无法调整窗口位置
            addWindowFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        // 跟随 Activity 的生命周期
        mLifecycle = new ActivityLifecycle(this, activity);
    }

    /**
     * 创建一个全局悬浮窗
     */
    public FloatWindow(Application application) {
        this((Context) application);
        // 设置成全局的悬浮窗，注意需要先申请悬浮窗权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Android 8.0以后不允许使用一下窗口类型来在其他应用和窗口上方显示提醒窗口
            //TYPE_PHONE
            //TYPE_PRIORITY_PHONE
            //TYPE_SYSTEM_ALERT
            //TYPE_SYSTEM_OVERLAY
            //TYPE_SYSTEM_ERROR
            //如果需要实现在其他应用和窗口上方显示提醒窗口，那么必须该为TYPE_APPLICATION_OVERLAY的类型。
            setWindowType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        } else {
            //在Android 8.0之前，悬浮窗口设置可以为TYPE_PHONE，这种类型是用于提供用户交互操作的非应用窗口。
            //Android 8.0以上版本你继续使用TYPE_PHONE类型的悬浮窗口，则会出现如下异常信息：
            //android.view.WindowManager$BadTokenException:
            //Unable to add window android.view.ViewRootImpl$W@f8ec928 -- permission denied for window type 2002
            //setWindowType(WindowManager.LayoutParams.TYPE_PHONE);
            setWindowType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
    }

    private FloatWindow(Context context) {
        mContext = context;
        mDecorView = new WindowLayout(context);
        //创建WindowManager
        //WindowManager负责窗口的动态操作，比如窗口的增、删、改。
        mWindowManager = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
        //负责窗口的静态属性，比如窗口的标题、背景、输入法模式、屏幕方向等等。
        // 配置一些默认的参数
        mWindowParams = new WindowManager.LayoutParams();

        //设置一些属性
        //宽高自适应
        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置位图格式
        mWindowParams.format = PixelFormat.TRANSLUCENT;
        mWindowParams.windowAnimations = android.R.style.Animation_Toast;
        mWindowParams.packageName = context.getPackageName();
        // 设置触摸外层布局（除 WindowManager 外的布局，默认是 WindowManager 显示的时候外层不可触摸）
        // 需要注意的是设置了 FLAG_NOT_TOUCH_MODAL 必须要设置 FLAG_NOT_FOCUSABLE，否则就会导致用户按返回键无效
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    }

    /**
     * 设置宽度和高度
     */
    @Override
    public FloatWindow setSize(int width, int height) {
        mWindowParams.width = width;
        mWindowParams.height = height;
        if (mDecorView.getChildCount() > 0) {
            View contentView = mDecorView.getChildAt(0);
            ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
            if (layoutParams != null && layoutParams.width != width) {
                layoutParams.width = width;
            }
            if (layoutParams != null && layoutParams.height != height) {
                layoutParams.height = height;
            }
            contentView.setLayoutParams(layoutParams);
        }
        update();
        return this;
    }

    @Override
    public FloatWindow setGravity(int gravity, int xOffset, int yOffset) {
        //设置窗口重心
        mWindowParams.gravity = gravity;
        //设置水平偏移量
        mWindowParams.x = xOffset;
        //设置垂直偏移量
        mWindowParams.y = yOffset;
        update();
        return this;
    }

    @Override
    public FloatWindow setParamsConfig(ParamsConfig paramsConfig) {
        //设置窗口方向
        mWindowParams.screenOrientation = paramsConfig.getScreenOrientation();
        //是否外层可触摸
        int flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        if (paramsConfig.isTouchable()) {
            addWindowFlags(flags);
        } else {
            clearWindowFlags(flags);
        }
        //设置token
        if (paramsConfig.getToken() != null) {
            mWindowParams.token = paramsConfig.getToken();
        }
        //设置窗口透明度
        if (paramsConfig.getAlpha() > -1) {
            mWindowParams.alpha = paramsConfig.getAlpha();
        }
        //设置垂直间距
        if (paramsConfig.getVerticalMargin() > -1) {
            mWindowParams.verticalMargin = paramsConfig.getVerticalMargin();
        }
        //设置水平间距
        if (paramsConfig.getHorizontalMargin() > -1) {
            mWindowParams.horizontalMargin = paramsConfig.getHorizontalMargin();
        }
        //设置状态栏可见性
        if (paramsConfig.getSystemUiVisibility()!=-1){
            mWindowParams.systemUiVisibility = paramsConfig.getSystemUiVisibility();
        }
        //设置窗口标题
        if (paramsConfig.getTitle()!=null){
            mWindowParams.setTitle(paramsConfig.getTitle());
        }
        //设置屏幕的亮度
        if (paramsConfig.getScreenBrightness()>=0){
            mWindowParams.screenBrightness = paramsConfig.getScreenBrightness();
        }
        //设置垂直权重
        if (paramsConfig.getVerticalWeight()>=0){
            mWindowParams.verticalWeight = paramsConfig.getVerticalWeight();
        }
        //设置窗口在哪个显示屏上显示
        if (paramsConfig.getPreferredDisplayModeId()>=0){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mWindowParams.preferredDisplayModeId = paramsConfig.getPreferredDisplayModeId();
            }
        }
        //设置按键的亮度
        if (paramsConfig.getButtonBrightness()>=0){
            mWindowParams.buttonBrightness = paramsConfig.getButtonBrightness();
        }
        update();
        return this;
    }

    @Override
    public void dismiss() {
        cancel();
    }

    /**
     * 设置窗口背景阴影强度
     */
    public FloatWindow setBackgroundDimAmount(float amount) {
        if (amount < 0 || amount > 1) {
            throw new IllegalArgumentException("are you ok?");
        }
        mWindowParams.dimAmount = amount;
        int flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        if (amount != 0) {
            addWindowFlags(flags);
        } else {
            clearWindowFlags(flags);
        }
        update();
        return (FloatWindow) this;
    }

    /**
     * 是否有这个标志位
     */
    public boolean hasWindowFlags(int flags) {
        return (mWindowParams.flags & flags) != 0;
    }

    /**
     * 添加一个标记位
     */
    public FloatWindow addWindowFlags(int flags) {
        mWindowParams.flags |= flags;
        update();
        return (FloatWindow) this;
    }

    /**
     * 移除一个标记位
     */
    public FloatWindow clearWindowFlags(int flags) {
        mWindowParams.flags &= ~flags;
        update();
        return (FloatWindow) this;
    }

    /**
     * 设置标记位
     */
    public FloatWindow setWindowFlags(int flags) {
        mWindowParams.flags = flags;
        update();
        return (FloatWindow) this;
    }

    /**
     * 设置窗口类型
     */
    public FloatWindow setWindowType(int type) {
        mWindowParams.type = type;
        update();
        return (FloatWindow) this;
    }

    /**
     * 设置动画样式
     */
    public FloatWindow setAnimStyle(int id) {
        mWindowParams.windowAnimations = id;
        update();
        return (FloatWindow) this;
    }

    /**
     * 设置软键盘模式
     * <p>
     * {@link WindowManager.LayoutParams#SOFT_INPUT_STATE_UNSPECIFIED}：没有指定状态,系统会选择一个合适的状态或依赖于主题的设置
     * {@link WindowManager.LayoutParams#SOFT_INPUT_STATE_UNCHANGED}：不会改变软键盘状态
     * {@link WindowManager.LayoutParams#SOFT_INPUT_STATE_HIDDEN}：当用户进入该窗口时，软键盘默认隐藏
     * {@link WindowManager.LayoutParams#SOFT_INPUT_STATE_ALWAYS_HIDDEN}：当窗口获取焦点时，软键盘总是被隐藏
     * {@link WindowManager.LayoutParams#SOFT_INPUT_ADJUST_RESIZE}：当软键盘弹出时，窗口会调整大小
     * {@link WindowManager.LayoutParams#SOFT_INPUT_ADJUST_PAN}：当软键盘弹出时，窗口不需要调整大小，要确保输入焦点是可见的
     */
    public FloatWindow setSoftInputMode(int mode) {
        mWindowParams.softInputMode = mode;
        // 如果设置了不能触摸，则擦除这个标记，否则会导致无法弹出输入法
        clearWindowFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        update();
        return (FloatWindow) this;
    }

    /**
     * 设置挖孔屏下的显示模式
     */
    public FloatWindow setLayoutInDisplayCutoutMode(int mode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            mWindowParams.layoutInDisplayCutoutMode = mode;
            update();
        }
        return (FloatWindow) this;
    }



    /**
     * 设置窗口的刷新率
     */
    public FloatWindow setPreferredRefreshRate(float preferredRefreshRate) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWindowParams.preferredRefreshRate = preferredRefreshRate;
            update();
        }
        return (FloatWindow) this;
    }

    /**
     * 设置窗口的颜色模式
     */
    public FloatWindow setColorMode(int colorMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mWindowParams.setColorMode(colorMode);
            update();
        }
        return (FloatWindow) this;
    }


    /**
     * 重新设置 WindowManager 参数集
     */
    public FloatWindow setWindowParams(WindowManager.LayoutParams params) {
        mWindowParams = params;
        update();
        return (FloatWindow) this;
    }

    /**
     * 设置拖动规则
     */
    public FloatWindow setDraggable(AbsTouchListener draggable) {
        // 如果当前是否设置了不可触摸，如果是就擦除掉这个标记
        clearWindowFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        // 如果当前是否设置了可移动窗口到屏幕之外，如果是就擦除这个标记
        clearWindowFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        mDraggable = draggable;
        if (isShowing()) {
            update();
            mDraggable.start(this);
        }
        return (FloatWindow) this;
    }

    /**
     * 设置生命周期监听
     */
    public FloatWindow setOnLifecycle(ILifecycleListener listener) {
        mListener = listener;
        return (FloatWindow) this;
    }

    /**
     * 设置内容布局
     */
    public FloatWindow setContentView(int id) {
        View view = LayoutInflater.from(mContext).inflate(id, mDecorView, false);
        return setContentView(view);
    }

    public FloatWindow setContentView(View view) {
        if (mDecorView.getChildCount() > 0) {
            mDecorView.removeAllViews();
        }
        mDecorView.addView(view);

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = ((ViewGroup.MarginLayoutParams) layoutParams);
            // 清除 Margin，因为 WindowManager 没有这一属性可以设置，并且会跟根布局相冲突
            marginLayoutParams.topMargin = 0;
            marginLayoutParams.bottomMargin = 0;
            marginLayoutParams.leftMargin = 0;
            marginLayoutParams.rightMargin = 0;
        }

        // 如果当前没有设置重心，就自动获取布局重心
        if (mWindowParams.gravity == Gravity.NO_GRAVITY) {
            if (layoutParams instanceof FrameLayout.LayoutParams) {
                int gravity = ((FrameLayout.LayoutParams) layoutParams).gravity;
                if (gravity != FrameLayout.LayoutParams.UNSPECIFIED_GRAVITY) {
                    mWindowParams.gravity = gravity;
                }
            } else if (layoutParams instanceof LinearLayout.LayoutParams) {
                int gravity = ((LinearLayout.LayoutParams) layoutParams).gravity;
                if (gravity != FrameLayout.LayoutParams.UNSPECIFIED_GRAVITY) {
                    mWindowParams.gravity = gravity;
                }
            }
            if (mWindowParams.gravity == Gravity.NO_GRAVITY) {
                // 默认重心是居中
                mWindowParams.gravity = Gravity.CENTER;
            }
        }

        if (layoutParams != null) {
            if (mWindowParams.width == WindowManager.LayoutParams.WRAP_CONTENT &&
                    mWindowParams.height == WindowManager.LayoutParams.WRAP_CONTENT) {
                // 如果当前 Dialog 的宽高设置了自适应，就以布局中设置的宽高为主
                mWindowParams.width = layoutParams.width;
                mWindowParams.height = layoutParams.height;
            } else {
                // 如果当前通过代码动态设置了宽高，则以动态设置的为主
                layoutParams.width = mWindowParams.width;
                layoutParams.height = mWindowParams.height;
            }
        }
        update();
        return (FloatWindow) this;
    }

    @Override
    public void showAsDropDown(View anchorView) {
        showAsDropDown(anchorView, Gravity.BOTTOM);
    }

    @Override
    public void showAsDropDown(View anchorView, int showGravity) {
        showAsDropDown(anchorView, showGravity, 0, 0);
    }

    /**
     * 将悬浮窗显示在某个 View 下方（和 PopupWindow 同名方法作用类似）
     *
     * @param anchorView  锚点 View
     * @param showGravity 显示重心
     * @param xOff        水平偏移
     * @param yOff        垂直偏移
     */
    @Override
    public void showAsDropDown(View anchorView, int showGravity, int xOff, int yOff) {
        if (mDecorView.getChildCount() == 0 || mWindowParams == null) {
            throw new IllegalArgumentException("WindowParams and view cannot be empty");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // 适配布局反方向
            showGravity = Gravity.getAbsoluteGravity(showGravity,
                    anchorView.getResources().getConfiguration().getLayoutDirection());
        }

        //首先获取目标anchorView在屏幕中的位置
        int[] anchorViewLocation = new int[2];
        anchorView.getLocationOnScreen(anchorViewLocation);

        //获取目标anchorView的屏幕参照
        Rect windowVisibleRect = new Rect();
        anchorView.getWindowVisibleDisplayFrame(windowVisibleRect);

        //然后计算悬浮窗的x和y的点
        mWindowParams.gravity = Gravity.TOP | Gravity.START;
        //x 计算 = 目标view的x绝对位置 - 屏幕参照rect的left间距 + x偏移量
        mWindowParams.x = anchorViewLocation[0] - windowVisibleRect.left + xOff;
        //y 计算 = 目标view的y绝对位置 - 屏幕参照rect的top间距 + y偏移量
        mWindowParams.y = anchorViewLocation[1] - windowVisibleRect.top + yOff;

        if ((showGravity & Gravity.LEFT) == Gravity.LEFT) {
            int rootViewWidth = mDecorView.getWidth();
            if (rootViewWidth == 0) {
                rootViewWidth = mDecorView.getMeasuredWidth();
            }
            if (rootViewWidth == 0) {
                mDecorView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                rootViewWidth = mDecorView.getMeasuredWidth();
            }
            mWindowParams.x -= rootViewWidth;
        } else if ((showGravity & Gravity.RIGHT) == Gravity.RIGHT) {
            mWindowParams.x += anchorView.getWidth();
        }

        if ((showGravity & Gravity.TOP) == Gravity.TOP) {
            int rootViewHeight = mDecorView.getHeight();
            if (rootViewHeight == 0) {
                rootViewHeight = mDecorView.getMeasuredHeight();
            }
            if (rootViewHeight == 0) {
                mDecorView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                rootViewHeight = mDecorView.getMeasuredHeight();
            }
            mWindowParams.y -= rootViewHeight;
        } else if ((showGravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
            mWindowParams.y += anchorView.getHeight();
        }

        show();
    }

    /**
     * 显示悬浮窗
     */
    @Override
    public void show() {
        if (mDecorView.getChildCount() == 0 || mWindowParams == null) {
            throw new IllegalArgumentException("WindowParams and view cannot be empty");
        }

        // 如果当前已经显示则进行更新
        if (mShowing) {
            update();
            return;
        }

        if (mContext instanceof Activity) {
            if (((Activity) mContext).isFinishing() ||
                    (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                            ((Activity) mContext).isDestroyed())) {
                return;
            }
        }

        try {
            // 如果 View 已经被添加的情况下，就先把 View 移除掉
            if (mDecorView.getParent() != null) {
                mWindowManager.removeViewImmediate(mDecorView);
            }
            mWindowManager.addView(mDecorView, mWindowParams);
            // 当前已经显示
            mShowing = true;

            // 如果设置了拖拽规则
            if (mDraggable != null) {
                mDraggable.start(this);
            }

            // 注册 Activity 生命周期
            if (mLifecycle != null) {
                mLifecycle.register();
            }

            // 回调监听
            if (mListener != null) {
                mListener.onShow(this);
            }

        } catch (NullPointerException | IllegalStateException |
                IllegalArgumentException | WindowManager.BadTokenException e) {
            // 如果这个 View 对象被重复添加到 WindowManager 则会抛出异常
            // java.lang.IllegalStateException: View has already been added to the window manager.
            e.printStackTrace();
            ExceptionReporter.report("Float FloatWindow addView", e);
        }
    }

    /**
     * 销毁悬浮窗
     */
    private void cancel() {
        if (!mShowing) {
            return;
        }
        try {
            // 反注册 Activity 生命周期
            if (mLifecycle != null) {
                mLifecycle.unregister();
            }

            // 如果当前 WindowManager 没有附加这个 View 则会抛出异常
            // java.lang.IllegalArgumentException: View not attached to window manager
            //mWindowManager.removeViewImmediate(mDecorView);
            mWindowManager.removeView(mDecorView);
            // 回调监听
            if (mListener != null) {
                mListener.onDismiss(this);
            }
        } catch (NullPointerException | IllegalArgumentException | IllegalStateException e) {
            e.printStackTrace();
            ExceptionReporter.report("Float FloatWindow removeView", e);
        } finally {
            // 当前没有显示
            mShowing = false;
        }
    }

    /**
     * 刷新悬浮窗
     */
    public void update() {
        if (!isShowing()) {
            return;
        }
        // 更新 WindowManger 的显示
        mWindowManager.updateViewLayout(mDecorView, mWindowParams);
    }

    /**
     * 回收释放
     */
    public void recycle() {
        if (isShowing()) {
            cancel();
        }
        if (mListener != null) {
            mListener.onRecycler(this);
        }
        mListener = null;
        mContext = null;
        mDecorView = null;
        mWindowManager = null;
        mWindowParams = null;
        mLifecycle = null;
        mDraggable = null;
    }

    /**
     * 当前是否已经显示
     */
    public boolean isShowing() {
        return mShowing;
    }

    /**
     * 获取 WindowManager 对象
     */
    public WindowManager getWindowManager() {
        return mWindowManager;
    }

    /**
     * 获取 WindowManager 参数集
     */
    public WindowManager.LayoutParams getWindowParams() {
        return mWindowParams;
    }

    /**
     * 获取上下文对象
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * 获取根布局
     */
    public View getDecorView() {
        return mDecorView;
    }

    /**
     * 获取内容布局
     */
    public View getContentView() {
        if (mDecorView.getChildCount() == 0) {
            return null;
        }
        return mDecorView.getChildAt(0);
    }

    /**
     * 根据 ViewId 获取 View
     */
    public <V extends View> V findViewById(int id) {
        return mDecorView.findViewById(id);
    }

    /**
     * 设置可见状态
     */
    public FloatWindow setVisibility(int id, int visibility) {
        findViewById(id).setVisibility(visibility);
        return (FloatWindow) this;
    }

    /**
     * 设置文本
     */
    public FloatWindow setText(int id) {
        return setText(android.R.id.message, id);
    }

    public FloatWindow setText(int viewId, int stringId) {
        return setText(viewId, mContext.getResources().getString(stringId));
    }

    public FloatWindow setText(CharSequence text) {
        return setText(android.R.id.message, text);
    }

    public FloatWindow setText(int id, CharSequence text) {
        ((TextView) findViewById(id)).setText(text);
        return (FloatWindow) this;
    }

    /**
     * 设置文本颜色
     */
    public FloatWindow setTextColor(int id, int color) {
        ((TextView) findViewById(id)).setTextColor(color);
        return (FloatWindow) this;
    }

    /**
     * 设置提示
     */
    public FloatWindow setHint(int viewId, int stringId) {
        return setHint(viewId, mContext.getResources().getString(stringId));
    }

    public FloatWindow setHint(int id, CharSequence text) {
        ((TextView) findViewById(id)).setHint(text);
        return (FloatWindow) this;
    }

    /**
     * 设置提示文本颜色
     */
    public FloatWindow setHintColor(int id, int color) {
        ((TextView) findViewById(id)).setHintTextColor(color);
        return (FloatWindow) this;
    }

    /**
     * 设置背景
     */
    public FloatWindow setBackground(int viewId, int drawableId) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = mContext.getDrawable(drawableId);
        } else {
            drawable = mContext.getResources().getDrawable(drawableId);
        }
        return setBackground(viewId, drawable);
    }

    public FloatWindow setBackground(int id, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            findViewById(id).setBackground(drawable);
        } else {
            findViewById(id).setBackgroundDrawable(drawable);
        }
        return (FloatWindow) this;
    }

    /**
     * 设置图片
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    public FloatWindow setImageDrawable(int viewId, int drawableId) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = mContext.getDrawable(drawableId);
        } else {
            drawable = mContext.getResources().getDrawable(drawableId);
        }
        return setImageDrawable(viewId, drawable);
    }

    public FloatWindow setImageDrawable(int viewId, Drawable drawable) {
        ((ImageView) findViewById(viewId)).setImageDrawable(drawable);
        return (FloatWindow) this;
    }

    /**
     * 设置点击事件
     */
    public FloatWindow setOnClickListener(IClickListener listener) {
        return setOnClickListener(mDecorView, listener);
    }

    public FloatWindow setOnClickListener(int id, IClickListener listener) {
        return setOnClickListener(findViewById(id), listener);
    }

    private FloatWindow setOnClickListener(View view, IClickListener listener) {
        // 如果当前是否设置了不可触摸，如果是就擦除掉
        clearWindowFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        view.setClickable(true);
        view.setOnClickListener(new ViewClickWrapper(this, listener));
        return (FloatWindow) this;
    }

    /**
     * 设置长按事件
     */
    public FloatWindow setOnLongClickListener(ILongClickListener listener) {
        return setOnLongClickListener(mDecorView, listener);
    }

    public FloatWindow setOnLongClickListener(int id, ILongClickListener listener) {
        return setOnLongClickListener(findViewById(id), listener);
    }

    private FloatWindow setOnLongClickListener(View view, ILongClickListener listener) {
        // 如果当前是否设置了不可触摸，如果是就擦除掉
        clearWindowFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        view.setClickable(true);
        view.setOnLongClickListener(new ViewLongClickWrapper(this, listener));
        return (FloatWindow) this;
    }

    /**
     * 设置触摸事件
     */
    public FloatWindow setOnTouchListener(ITouchListener listener) {
        return setOnTouchListener(mDecorView, listener);
    }

    public FloatWindow setOnTouchListener(int id, ITouchListener listener) {
        return setOnTouchListener(findViewById(id), listener);
    }

    private FloatWindow setOnTouchListener(View view, ITouchListener listener) {
        // 当前是否设置了不可触摸，如果是就擦除掉
        clearWindowFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        view.setEnabled(true);
        view.setOnTouchListener(new ViewTouchWrapper(this, listener));
        return (FloatWindow) this;
    }

}