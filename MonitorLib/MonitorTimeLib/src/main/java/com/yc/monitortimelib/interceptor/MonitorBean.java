package com.yc.monitortimelib.interceptor;

import java.io.Serializable;

/**
 * 监控耗时的实体
 */
public class MonitorBean implements Serializable {

    /**
     * 耗时
     */
    private long cos;
    private String methodName;
    private BaseParam param;
    private BaseResult result;

    public long getCos() {
        return cos;
    }

    public void setCos(long cos) {
        this.cos = cos;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public BaseParam getParam() {
        return param;
    }

    public void setParam(BaseParam param) {
        this.param = param;
    }

    public BaseResult getResult() {
        return result;
    }

    public void setResult(BaseResult result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "MonitorBean{" +
                "方法耗时cos=" + cos +
                ", methodName='" + methodName + '\'' +
                ", param=" + param +
                ", result=" + result +
                '}';
    }
}
