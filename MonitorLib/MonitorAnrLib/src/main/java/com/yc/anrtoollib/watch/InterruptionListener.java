package com.yc.anrtoollib.watch;

import androidx.annotation.NonNull;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2021/8/11
 *     desc   : 拦截监听
 *     revise :
 * </pre>
 */
public interface InterruptionListener {
    void onInterrupted(@NonNull InterruptedException exception);
}
