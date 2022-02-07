package com.yc.netlib.connect;

/**
 * Connection移动平均计算
 * 主要是获取网络的平均值，里面最重要的就是addMeasurement(double measurement)。
 */
public class ExponentialGeometricAverage {

    private final double mDecayConstant;
    private final int mCutover;

    private double mValue = -1;
    private int mCount;

    public ExponentialGeometricAverage(double decayConstant) {
        mDecayConstant = decayConstant;
        mCutover = decayConstant == 0.0 ? Integer.MAX_VALUE : (int) Math.ceil(1 / decayConstant);
    }

    /**
     * 为移动平均值添加了一个新的测量方法
     * @param measurement                 带宽测量以比特/毫秒增加到移动平均。
     */
    public void addMeasurement(double measurement) {
        //假设mDecayConstant这个值是0.95，看构造方法的代码可知，那么mCutover则是20呢
        double keepConstant = 1 - mDecayConstant;
        // 累加次数大于20次
        if (mCount > mCutover) {
            // 自然对数Math.log(mValue),Math.exp() 函数返回 ex
            mValue = Math.exp(keepConstant * Math.log(mValue) + mDecayConstant * Math.log(measurement));
        } else if (mCount > 0) {
            // 0<mCount<20走这里
            double retained = keepConstant * mCount / (mCount + 1.0);
            double newcomer = 1.0 - retained;
            mValue = Math.exp(retained * Math.log(mValue) + newcomer * Math.log(measurement));
        } else {
            // 第一次mCount是等于0的
            mValue = measurement;
        }
        mCount++;
    }

    /**
     * 获取平均值
     * @return                      平均值
     */
    public double getAverage() {
        return mValue;
    }

    /**
     * 重制
     */
    public void reset() {
        mValue = -1.0;
        mCount = 0;
    }
}
