package com.yc.percent;

public class PercentVal {

    public float percent = -1;
    public PercentBaseMode basemode;

    public PercentVal() {
    }

    public PercentVal(float percent, PercentBaseMode baseMode) {
        this.percent = percent;
        this.basemode = baseMode;
    }

    @Override
    public String toString() {
        return "PercentVal{" +
                "percent=" + percent +
                ", basemode=" + basemode.name() +
                '}';
    }

}
