package com.yc.easyexecutor;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2019/5/11
 *     desc   : 抽象task任务类
 *     revise :
 *     GitHub : https://github.com/yangchong211/YCThreadPool
 * </pre>
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public abstract class AbsTaskExecutor {

    public abstract void executeOnCore(@NonNull Runnable runnable);

    public abstract void executeOnDiskIO(@NonNull Runnable runnable);

    public abstract void executeOnCpu(@NonNull Runnable runnable);

    public abstract void postToMainThread(@NonNull Runnable runnable);

    public abstract Handler getMainHandler();

    public abstract void postIoHandler(@NonNull Runnable runnable);

    public void executeOnMainThread(@NonNull Runnable runnable) {
        if (isMainThread()) {
            runnable.run();
        } else {
            postToMainThread(runnable);
        }
    }

    public abstract boolean isMainThread();
}
