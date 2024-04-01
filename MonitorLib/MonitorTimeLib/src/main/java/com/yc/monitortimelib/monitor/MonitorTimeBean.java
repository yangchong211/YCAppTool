package com.yc.monitortimelib.monitor;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : time实体bean
 *     revise:
 * </pre>
 */
public final class MonitorTimeBean {

    /**
     * action名称
     */
    private String actionName;
    /**
     * 开始时间
     */
    private long actionStartTime;
    /**
     * 结束时间
     */
    private long actionEndTime;

    MonitorTimeBean(String actionName){
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
