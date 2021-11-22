package com.yc.appstatuslib.info;

import com.yc.appstatuslib.utils.CpuInfoUtils;

public class CpuInfo {
    public int cpuCount;
    public String cpuTemperature = "";

    public CpuInfo() {
    }

    static CpuInfo builder() {
        CpuInfo cpuInfo = new CpuInfo();
        cpuInfo.cpuCount = CpuInfoUtils.cpuCount();
        cpuInfo.cpuTemperature = CpuInfoUtils.cpuTemperature();
        return cpuInfo;
    }

    public String toString() {
        return "CpuInfo{cpuCount=" + this.cpuCount + ", cpuTemperature='" + this.cpuTemperature + '\'' + '}';
    }
}
