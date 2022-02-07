package com.yc.catonhelperlib.fps;

public class PerformanceData {
    //日期
    public String date;
    //时间
    public String time;
    //范围
    public float parameter;

    public PerformanceData(String date, String time, float parameter) {
        this.date = date;
        this.time = time;
        this.parameter = parameter;
    }
}
