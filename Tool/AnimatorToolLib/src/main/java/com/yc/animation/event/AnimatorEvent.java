package com.yc.animation.event;

import com.yc.animation.listener.AnimatorListener;


/**
 * <pre>
 *     @author  yangchong
 *     email  : yangchong211@163.com
 *     time  :  2017/5/26
 *     desc  :  动画事件管理类，目前只有 AnimatorListener，后续如有需要，可以添加其他类型的 listener
 *     revise:
 * </pre>
 */
public abstract class AnimatorEvent {

    private AnimatorListener mListener;

    public AnimatorListener getListener() {
        return this.mListener;
    }

    public void setListener(AnimatorListener listener) {
        this.mListener = listener;
    }
}
