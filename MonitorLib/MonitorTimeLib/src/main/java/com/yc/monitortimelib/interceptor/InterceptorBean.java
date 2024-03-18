package com.yc.monitortimelib.interceptor;

import java.io.Serializable;

/**
 * @author : yangchong
 * @email : yangchong211@163.com
 * @time : 2017/5/18
 * @desc : 拦截器实体bean
 * revise :
 */
public class InterceptorBean implements Serializable {

    private long beginTime;
    private long endTime;
    private long id;
    private String methodName;
    private Exception exception;
    private BaseParam param;
    private BaseResult result;
    private boolean isBefore = true;

    public InterceptorBean() {
    }

    public long getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Exception getException() {
        return this.exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public BaseParam getParam() {
        return this.param;
    }

    public void setParam(BaseParam param) {
        this.param = param;
    }

    public BaseResult getResult() {
        return this.result;
    }

    public void setResult(BaseResult result) {
        this.result = result;
    }

    public boolean isBefore() {
        return this.isBefore;
    }

    public void setBefore(boolean before) {
        this.isBefore = before;
    }

    @Override
    public String toString() {
        return "InterceptorBean{" +
                "beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", id=" + id +
                ", methodName='" + methodName + '\'' +
                ", exception=" + exception +
                ", param=" + param +
                ", result=" + result +
                ", isBefore=" + isBefore +
                '}';
    }
}
