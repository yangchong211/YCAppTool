package com.yc.anrtoollib.watch;

import androidx.annotation.NonNull;

public interface InterruptionListener {
    void onInterrupted(@NonNull InterruptedException exception);
}
