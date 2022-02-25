package com.yc.animation.wrapper;

import androidx.annotation.Nullable;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * 等待 View 显示（实际是可见性不为 GONE 时）的回调包装类，用来获取初始状态为 GONE 的 View 的宽、高、坐标等属性值
 * @author 杨充
 * @date 2017/5/31 15:20
 */
public class RunnableWrapper {
    private WeakReference<View> targetView;
    private Runnable runnable;

    public RunnableWrapper(View targetView, Runnable runnable) {
        this.targetView = targetView == null ? null : new WeakReference<>(targetView);
        this.runnable = runnable;
    }

    @Nullable
    public View getTarget() {
        return this.targetView == null ? null : this.targetView.get();
    }

    public Runnable getRunnable() {
        return this.runnable;
    }
}
