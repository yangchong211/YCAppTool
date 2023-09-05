package com.yc.crash;

public interface NativeCrashListener {
    void onSignalReceived(int signal, String logPath);
}
