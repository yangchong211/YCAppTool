package com.yc.location.config;

/**
 *  设置监听位置的一些配置选项，当前只包含监听时间间隔。
 */

public class LocationUpdateOption {

    private IntervalMode mIntervalMode = IntervalMode.NORMAL;
    /**
     * 分配给各业务线的key
     */
    private String moduleKey;

    public String getModuleKey() {
        return moduleKey;
    }

    /**
     * 分配给各业务线的key（暂时使用POI业务分配的key）。务必传入各业务线自己正确的key。
     * @param moduleKey
     */
    public void setModuleKey(String moduleKey) {
        this.moduleKey = moduleKey;
    }

    public enum IntervalMode{
        /**
         * 高频率
         */
        HIGH_FREQUENCY(1000),
        /**
         * 正常
         */
        NORMAL(3000),
        /**
         * 低频率
         */
        LOW_FREQUENCY(9000),
        /**
         *
         */
        BATTERY_SAVE(36000);
        private long mInterval;
        IntervalMode(long interval) {
            mInterval = interval;
        }
        public long getValue() {
            return mInterval;
        }
    }
    /**
     * 设置监听位置时间间隔。
     * @param mode 时间间隔的枚举变量。见此类内部定义的{@link IntervalMode}
     */
    public void setInterval(IntervalMode mode) {
        this.mIntervalMode = mode;
    }

    public IntervalMode getInterval() {
        return mIntervalMode;
    }
}
