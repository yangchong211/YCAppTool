package com.yc.videoview.impl;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import com.yc.videoview.PermissionActivity;
import com.yc.videoview.tool.FloatWindowUtils;
import com.yc.videoview.abs.AbsFloatView;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/10/21
 *     desc  : 抽象view类的实现类
 *     revise: 7.1及以上需申请权限
 * </pre>
 */
public class FloatPhoneImpl extends AbsFloatView {

    private final Context mContext;
    private final WindowManager mWindowManager;
    private final WindowManager.LayoutParams mLayoutParams;
    private View mView;
    private int mX, mY;

    public FloatPhoneImpl(Context applicationContext) {
        mContext = applicationContext;
        //创建WindowManager
        //WindowManager负责窗口的动态操作，比如窗口的增、删、改。
        mWindowManager = (WindowManager)
                applicationContext.getSystemService(Context.WINDOW_SERVICE);
        //负责窗口的静态属性，比如窗口的标题、背景、输入法模式、屏幕方向等等。
        mLayoutParams = new WindowManager.LayoutParams();
    }

    @Override
    public void setSize(int width, int height) {
        mLayoutParams.width = width;
        mLayoutParams.height = height;
    }

    @Override
    public void setView(View view) {
        int layoutType;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Android 8.0以后不允许使用一下窗口类型来在其他应用和窗口上方显示提醒窗口
            //TYPE_PHONE
            //TYPE_PRIORITY_PHONE
            //TYPE_SYSTEM_ALERT
            //TYPE_SYSTEM_OVERLAY
            //TYPE_SYSTEM_ERROR
            //如果需要实现在其他应用和窗口上方显示提醒窗口，那么必须该为TYPE_APPLICATION_OVERLAY的类型。
            layoutType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            //在Android 8.0之前，悬浮窗口设置可以为TYPE_PHONE，这种类型是用于提供用户交互操作的非应用窗口。
            //Android 8.0以上版本你继续使用TYPE_PHONE类型的悬浮窗口，则会出现如下异常信息：
            //android.view.WindowManager$BadTokenException:
            //Unable to add window android.view.ViewRootImpl$W@f8ec928 -- permission denied for window type 2002
            layoutType = WindowManager.LayoutParams.TYPE_PHONE;
        }
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //宽高自适应
        mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置
        mLayoutParams.format = PixelFormat.TRANSPARENT;
        //设置类型
        mLayoutParams.type = layoutType;
        //动画
        mLayoutParams.windowAnimations = 0;
        //token，指标识窗口
        //mLayoutParams.token;
        mView = view;
    }

    @Override
    public void setGravity(int gravity, int xOffset, int yOffset) {
        mLayoutParams.gravity = gravity;
        mLayoutParams.x = mX = xOffset;
        mLayoutParams.y = mY = yOffset;
    }


    @Override
    public void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (FloatWindowUtils.hasPermission(mContext)) {
                //设置bitmap的格式
                mLayoutParams.format = PixelFormat.RGBA_8888;
                //将悬浮窗控件添加到WindowManager
                mWindowManager.addView(mView, mLayoutParams);
            } else {
                //先申请权限，然后在addView进来
                PermissionActivity.request(mContext, new PermissionActivity.PermissionListener() {
                    @Override
                    public void onSuccess() {
                        mLayoutParams.format = PixelFormat.RGBA_8888;
                        mWindowManager.addView(mView, mLayoutParams);
                    }

                    @Override
                    public void onFail() {

                    }
                });
            }
        }
    }

    @Override
    public void dismiss() {
        if (mView != null) {
            mWindowManager.removeView(mView);
        }
    }

    @Override
    public void updateXY(int x, int y) {
        mLayoutParams.x = mX = x;
        mLayoutParams.y = mY = y;
        mWindowManager.updateViewLayout(mView, mLayoutParams);
    }

    @Override
    public void updateX(int x) {
        mLayoutParams.x = mX = x;
        mWindowManager.updateViewLayout(mView, mLayoutParams);

    }

    @Override
    public void updateY(int y) {
        mLayoutParams.y = mY = y;
        mWindowManager.updateViewLayout(mView, mLayoutParams);
    }

    @Override
    public int getX() {
        return mX;
    }

    @Override
    public int getY() {
        return mY;
    }
}
