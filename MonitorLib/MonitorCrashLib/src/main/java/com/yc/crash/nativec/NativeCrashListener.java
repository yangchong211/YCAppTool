package com.yc.crash.nativec;

public interface NativeCrashListener {
    void onSignalReceived(int signal, String logPath);
}
