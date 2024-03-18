package com.yc.monitortimelib.interceptor;


import java.io.Serializable;

/**
 * @author : yangchong
 * @email : yangchong211@163.com
 * @time : 2017/5/18
 * @desc : 结果
 * revise :
 */
public class BaseResult implements Serializable {

    protected boolean success;
    protected String errMsg;
    protected Exception exception;
    protected long timeCos;

    public BaseResult() {

    }

    public BaseResult(boolean success, String errMsg, Exception exception) {
        this.success = success;
        this.errMsg = errMsg;
        this.exception = exception;
    }

    public BaseResult(boolean success, String errMsg, Exception exception, long timeCos) {
        this.success = success;
        this.errMsg = errMsg;
        this.exception = exception;
        this.timeCos = timeCos;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public long getTimeCos() {
        return timeCos;
    }

    public void setTimeCos(long timeCos) {
        this.timeCos = timeCos;
    }

    @Override
    public String toString() {
        return "BaseRt{" +
                "success=" + success +
                ", errMsg='" + errMsg + '\'' +
                ", exception=" + exception +
                ", timeCos=" + timeCos +
                '}';
    }
}
