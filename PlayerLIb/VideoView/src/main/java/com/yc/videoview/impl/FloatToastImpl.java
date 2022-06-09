package com.yc.videoview.impl;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.yc.videoview.abs.AbsFloatView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/10/21
 *     desc  : 自定义 toast 方式，无需申请权限
 *     revise:
 * </pre>
 */
public class FloatToastImpl extends AbsFloatView {

    private final Toast toast;
    private Object mTN;
    private Method show;
    private Method hide;
    private int mWidth;
    private int mHeight;


    public FloatToastImpl(Context applicationContext) {
        toast = new Toast(applicationContext);
    }


    @Override
    public void setSize(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    @Override
    public void setView(View view) {
        toast.setView(view);
        initTN();
    }

    @Override
    public void setGravity(int gravity, int xOffset, int yOffset) {
        toast.setGravity(gravity, xOffset, yOffset);
    }

    @Override
    public void init() {
        try {
            show.invoke(mTN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        try {
            hide.invoke(mTN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initTN() {
        try {
            Field tnField = toast.getClass().getDeclaredField("mTN");
            tnField.setAccessible(true);
            mTN = tnField.get(toast);
            if (mTN != null) {
                show = mTN.getClass().getMethod("show");
                hide = mTN.getClass().getMethod("hide");
                Field tnParamsField = mTN.getClass().getDeclaredField("mParams");
                tnParamsField.setAccessible(true);
                WindowManager.LayoutParams params = (WindowManager.LayoutParams) tnParamsField.get(mTN);
                if (params != null) {
                    params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                    params.width = mWidth;
                    params.height = mHeight;
                    params.windowAnimations = 0;
                }
                Field tnNextViewField = mTN.getClass().getDeclaredField("mNextView");
                tnNextViewField.setAccessible(true);
                tnNextViewField.set(mTN, toast.getView());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
