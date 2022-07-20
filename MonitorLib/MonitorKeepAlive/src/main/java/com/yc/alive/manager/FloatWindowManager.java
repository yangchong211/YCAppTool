package com.yc.alive.manager;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.RestrictTo;

import com.yc.alive.nova.ka.R;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;

/**
 * 悬浮窗
 */
@RestrictTo(LIBRARY)
public class FloatWindowManager {

    private Context mContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;

    private View mView;

    private FloatWindowManager() {
    }

    private static final class InnerHolder {
        private static final FloatWindowManager INSTANCE = new FloatWindowManager();
    }

    public static FloatWindowManager getInstance() {
        return InnerHolder.INSTANCE;
    }

    public void show(Context context) {
        mContext = context.getApplicationContext();
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        showFloatView();
    }

    public void close() {
        if (mView != null) {
            try {
                mWindowManager.removeView(mView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mView = null;
    }

    private void showFloatView() {
        try {
            mWindowManager.addView(getFloatView(), mLayoutParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View getFloatView() {
        if (mView != null) {
            return mView;
        }

        mLayoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mLayoutParams.type =
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        }
        mLayoutParams.flags =
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP; // 显示在屏幕左上角
        // 显示位置与指定位置的相对位置差
        mLayoutParams.x = 0;
        mLayoutParams.y = 0;
        // 悬浮窗的宽高
        mLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        mLayoutParams.format = PixelFormat.TRANSPARENT;

        int floatWindowLayoutId = AssistantManager.getInstance().getFloatWindowLayoutId();
        if (floatWindowLayoutId <= 0) {
            floatWindowLayoutId = R.layout.ka_layout_float_window;
        }
        mView = LayoutInflater.from(mContext).inflate(floatWindowLayoutId, null, false);

        View closeView = mView.findViewById(R.id.ka_float_window_close);
        if (closeView != null) {
            closeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    close();
                }
            });
        }

        return mView;
    }
}
