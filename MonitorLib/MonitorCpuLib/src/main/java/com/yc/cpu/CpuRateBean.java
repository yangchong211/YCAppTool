package com.yc.cpu;

public class CpuRateBean {
    /**
     * 获取CPU占用情况
     */
    private float total;
    private float process;
    private int retryTimes;

    public CpuRateBean(float total, float process) {
        this.total = total;
        this.process = process;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getProcess() {
        return process;
    }

    public void setProcess(float process) {
        this.process = process;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    @Override
    public String toString() {
        return "CPURate{" +
                "total=" + total +
                ", process=" + process +
                ", retryTimes=" + retryTimes +
                '}';
    }

}
