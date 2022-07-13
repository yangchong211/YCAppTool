package com.yc.monitortimelib;


public final class TimeTraceBean {

    private String actionName;
    private long actionStartTime;
    private long actionEndTime;

    TimeTraceBean(String actionName){
        setActionName(actionName);
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public long getActionStartTime() {
        return actionStartTime;
    }

    public void setActionStartTime(long actionStartTime) {
        this.actionStartTime = actionStartTime;
    }

    public long getActionEndTime() {
        return actionEndTime;
    }

    public void setActionEndTime(long actionEndTime) {
        this.actionEndTime = actionEndTime;
    }
}
