package com.yc.monitortimelib.interceptor;


import java.io.Serializable;

/**
 * @author : yangchong
 * @email : yangchong211@163.com
 * @time : 2017/5/18
 * @desc : 参数
 * revise :
 */
public class BaseParam implements Serializable {

    private long timeout = 30 * 1000;
    /**
     * 主要是用于全局监听中，通过该字段区分不同的方法
     */
    private String methodName;

    public BaseParam(String methodName) {
        this.methodName = methodName;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return "BasePm{" +
                "timeout=" + timeout +
                ", methodName='" + methodName + '\'' +
                '}';
    }
}
