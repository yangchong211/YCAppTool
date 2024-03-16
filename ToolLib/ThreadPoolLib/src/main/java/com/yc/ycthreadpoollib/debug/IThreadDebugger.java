

package com.yc.ycthreadpoollib.debug;

public interface IThreadDebugger {

    IThreadDebugger add(String key);

    IThreadDebugger add(String startWithKey, String alias);

    IThreadDebugger ignoreUnknownCategory();

    void refresh();

    String drawUpEachThreadSize();

    String drawUpEachThreadInfo();

    String drawUpEachThreadSizeDiff();

    String drawUpEachThreadInfoDiff();

    String drawUpUnknownInfo();


    boolean isSizeChanged();

    boolean isChanged();
}
