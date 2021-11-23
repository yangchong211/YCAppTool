package com.yc.location.listener;

import com.yc.location.config.LocationUpdateOption;

/**
 * 监听位置的listener的封装类
 */
public class LocationListenerWrapper {
    //配置选项
    private LocationUpdateOption option;
    //外部传入的监听位置listener
    private LocationListener listener;
    private boolean neverNotified = true;

    public boolean isNeverNotified() {
        return neverNotified;
    }

    public void setNeverNotified(boolean neverNotified) {
        this.neverNotified = neverNotified;
    }

    public LocationListenerWrapper(LocationListener listener, LocationUpdateOption option) {
        this.listener = listener;
        this.option = option;
    }

    public LocationUpdateOption getOption() {
        return option;
    }

    public void setOption(LocationUpdateOption option) {
        this.option = option;
    }

    public LocationListener getListener() {
        return listener;
    }

    public void setListener(LocationListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof LocationListenerWrapper) {
            LocationListener listener = ((LocationListenerWrapper) other).getListener();
            LocationUpdateOption option = ((LocationListenerWrapper) other).getOption();
            if (this.listener.equals(listener)
                    && this.option.getInterval().getValue() == option.getInterval().getValue()){
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return listener.hashCode();
    }
}
