package com.yc.monitorflow;

public final class TrafficBean {

    private long totalRxKB;
    private long totalTxKB;
    private long totalKB;
    private long bootTime;

    public TrafficBean() {

    }

    public TrafficBean(long totalRxKB, long totalTxKB, long totalKB) {
        this.totalRxKB = totalRxKB;
        this.totalTxKB = totalTxKB;
        this.totalKB = totalKB;
    }

    public long getTotalRxKB() {
        return totalRxKB;
    }

    public void setTotalRxKB(long totalRxKB) {
        this.totalRxKB = totalRxKB;
    }

    public long getTotalTxKB() {
        return totalTxKB;
    }

    public void setTotalTxKB(long totalTxKB) {
        this.totalTxKB = totalTxKB;
    }

    public long getTotalKB() {
        return totalKB;
    }

    public void setTotalKB(long totalKB) {
        this.totalKB = totalKB;
    }

    public long getBootTime() {
        return bootTime;
    }

    public void setBootTime(long bootTime) {
        this.bootTime = bootTime;
    }

    @Override
    public String toString() {
        return "Flow{" +
                "totalRxKB=" + totalRxKB +
                ", totalTxKB=" + totalTxKB +
                ", bootTime=" + bootTime +
                '}';
    }

}
