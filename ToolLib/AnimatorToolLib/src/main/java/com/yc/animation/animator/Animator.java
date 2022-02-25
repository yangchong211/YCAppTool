package com.yc.animation.animator;

import android.util.Log;
import android.view.View;

import com.yc.animation.utils.LoggerUtil;
import com.yc.animation.wrapper.RunnableWrapper;
import com.yc.animation.event.AnimatorEvent;
import com.yc.animation.event.PropertyBuilder;
import com.yc.animation.inter.IAnimatorSequence;

import java.util.ArrayList;
import java.util.List;

/**
 * 动画控制类，启动、反向启动、停止动画等
 * 通过 {@link PropertyBuilder} 来记录并组合多个 view 的动画属性
 * 调用 {@link Animator#start()} 方法时播放整个动画序列
 * @author 杨充
 * @date 2017/5/11 11:19
 */
public abstract class Animator extends AnimatorEvent implements IAnimatorSequence {
    private static final String TAG = "Animator";

    private List<RunnableWrapper> mWaitForViewShownRunnableList =  new ArrayList<>();

    public abstract void play(PropertyBuilder builder);

    public abstract void with(PropertyBuilder builder);

    public abstract void before(PropertyBuilder builder);

    public abstract void after(PropertyBuilder builder);

    public abstract void build();

    public abstract Animator start();

    private void reverse() {
        Log.d("sofa.animation.Animator", "Reverse is unsupported.");
    }

    public abstract void stop();

    public abstract boolean isRunning();

    public abstract boolean isStarted();

    public void waitForViewShown(RunnableWrapper runnable) {
        if (runnable != null) {
            this.mWaitForViewShownRunnableList.add(runnable);
        }
    }

    public boolean isWaitingForViewShown() {
        return !this.mWaitForViewShownRunnableList.isEmpty();
    }

    private List<RunnableWrapper> getWaitForViewShownRunnableList() {
        return this.mWaitForViewShownRunnableList;
    }

    private void clearWaitingForViewShownRunnableList() {
        this.mWaitForViewShownRunnableList.clear();
    }

    public void prepareAnimation(final PrepareAnimationCallback callback) {
        if (isWaitingForViewShown()) {
            final List<RunnableWrapper> runnableWrapperList = getWaitForViewShownRunnableList();
            for (RunnableWrapper runnableWrapper : runnableWrapperList) {
                final View target = runnableWrapper.getTarget();
                if (target == null) {
                    LoggerUtil.e(TAG, "prepareAnimation error: target is null !!");
                    continue;
                }
                target.setVisibility(View.INVISIBLE);
            }
            for (RunnableWrapper runnableWrapper : runnableWrapperList) {
                final View target = runnableWrapper.getTarget();
                if (target != null) {
                    target.post(new Runnable() {
                        @Override
                        public void run() {
                            for (RunnableWrapper runnableWrapper : runnableWrapperList) {
                                runnableWrapper.getRunnable().run();
                            }
                            if (callback != null) {
                                callback.onPrepared();
                            }
                        }
                    });
                    break;
                }
            }
        } else {
            if (callback != null) {
                callback.onPrepared();
            }
        }
    }

    public interface PrepareAnimationCallback {
        void onPrepared();
    }
}
