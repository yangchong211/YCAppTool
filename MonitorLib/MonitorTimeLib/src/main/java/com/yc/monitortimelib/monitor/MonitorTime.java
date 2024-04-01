package com.yc.monitortimelib.monitor;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : time监控器
 *     revise:
 * </pre>
 */
public final class MonitorTime {

    private final MonitorTimeBean timeTraceBean;
    private final Object object = new Object();

    MonitorTime(String actionName) {
        timeTraceBean = new MonitorTimeBean(actionName);
    }

    void start(String actionName) {
        if (actionName == null || actionName.isEmpty()) {
            return;
        }
        long startTime = System.currentTimeMillis();
        //设置开始时间
        synchronized (object) {
            timeTraceBean.setActionStartTime(startTime);
        }
    }

    void end(String actionName, MonitorListener listener) {
        if (actionName == null || actionName.isEmpty()) {
            return;
        }
        long endTime = System.currentTimeMillis();
        //设置结束时间
        synchronized (object) {
            timeTraceBean.setActionEndTime(endTime);
        }
        debug(listener);
    }

    void debug(MonitorListener listener) {
        //计算开始时间和结束时间的差值
        long monitorTime = timeTraceBean.getActionEndTime() - timeTraceBean.getActionStartTime();
        if (listener != null) {
            MonitorFormatAdapter formatAdapter = MonitorTimeHelper.getFormatAdapter();
            String format = formatAdapter.onFormat(timeTraceBean.getActionName(), monitorTime);
            listener.onMonitorResult(timeTraceBean.getActionName(), format);
        }
    }

}
