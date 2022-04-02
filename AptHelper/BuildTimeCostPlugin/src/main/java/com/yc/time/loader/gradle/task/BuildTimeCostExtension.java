package com.yc.time.loader.gradle.task;

public final class BuildTimeCostExtension {

    //task执行时间超过该值才会统计
    private int threshold;

    //是否按照task执行时长进行排序，true-表示从大到小进行排序，false-表示不排序
    private boolean sorted;

    public void threshold(int threshold) {
        this.threshold = threshold;
    }

    public void sorted(boolean sorted) {
        this.sorted = sorted;
    }

    public int getThreshold() {
        return threshold;
    }

    public boolean isSorted() {
        return sorted;
    }
}
