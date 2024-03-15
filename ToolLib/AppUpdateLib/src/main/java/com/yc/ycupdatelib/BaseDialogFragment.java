package com.yc.ycupdatelib;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.yc.eventuploadlib.LoggerReporter;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://www.jianshu.com/p/514eb6193a06
 *     time  : 2017/8/9
 *     desc  : 自定义布局弹窗
 *     revise:
 * </pre>
 */
public abstract class BaseDialogFragment extends DialogFragment {

    private static final String TAG = "BaseDialogFragment";
    private static final float DEFAULT_DIM = 0.2f;
    private Dialog dialog;
    private Local local = Local.BOTTOM;

    public enum Local {
        /**
         * top顶部
         */
        TOP,
        /**
         * center居中
         */
        CENTER,
        /**
         * bottom底部
         */
        BOTTOM
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (local == Local.BOTTOM) {
            setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BottomDialog);
        } else if (local == Local.CENTER || local == Local.TOP) {
            setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CenterDialog);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dialog = getDialog();
        if (dialog != null) {
            if (dialog.getWindow() != null) {
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            }
            dialog.setCanceledOnTouchOutside(isCancel());
            dialog.setCancelable(isCancel());
        }
        View v = inflater.inflate(getLayoutRes(), container, false);
        bindView(v);
        return v;
    }

    /**
     * 设置是否可以cancel
     *
     * @return
     */
    protected abstract boolean isCancel();

    /**
     * 获取布局资源文件
     *
     * @return 布局资源文件id值
     */
    @LayoutRes
    public abstract int getLayoutRes();

    /**
     * 绑定
     *
     * @param v view
     */
    public abstract void bindView(View v);

    /**
     * 开始展示
     */
    @Override
    public void onStart() {
        super.onStart();
        if (dialog == null) {
            dialog = getDialog();
        }
        Window window = null;
        if (dialog != null) {
            window = dialog.getWindow();
        }
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount = getDimAmount();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            if (getHeight() > 0) {
                params.height = getHeight();
            } else {
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            }
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }


    public int getHeight() {
        return -1;
    }

    public float getDimAmount() {
        return DEFAULT_DIM;
    }

    public String getFragmentTag() {
        return TAG;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public void show(FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            show(fragmentManager, getFragmentTag());
        } else {
            LoggerReporter.report(TAG,"must be set fragmentManager");
        }
    }

    /**
     * 一定要销毁dialog，设置为null，防止内存泄漏
     * GitHub地址：https://github.com/yangchong211
     * 如果可以，欢迎star
     */
    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
            LoggerReporter.report(TAG,"dismiss dialog");
        }
    }

}
