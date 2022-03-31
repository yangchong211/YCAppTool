package com.yc.time.loader.gradle.task;

public final class TaskExecTimeInfo {
    /**
     * task执行总时长
     */
    private long total;
    /**
     * task 路径
     */
    private String path;
    /**
     * task 执行开始时间
     */
    private long start;
    /**
     * task 结束时间
     */
    private long end;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
