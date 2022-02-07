package com.yc.anrtoollib.watch;

import androidx.annotation.NonNull;

public interface ANRListener {
    /**
     * onAppNotResponding()的实现方式暴露给开发者，当检测到ANR时调用。
     * Called when an ANR is detected.
     *
     * @param error The error describing the ANR.
     */
    void onAppNotResponding(@NonNull ANRError error);

}
