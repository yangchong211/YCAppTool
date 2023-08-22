package com.yc.grpcserver;


import com.yc.dataclonelib.BaseParentBean;

/**
 * 请求体的基类
 */
public class BaseRequest extends BaseParentBean {

    //请求超时的时间，默认是10s
    protected long timeoutMil = 10 * 1000;
    //是否放在后台请求
    protected boolean isBackground = false;
    //是否开启压缩功能，开启的话传:gzip
    protected String onlyZip = "";
    //方法名
    protected String methodName = "";
    //默认的压缩方法
    public static final String G_ZIP = "gzip";
    public static final String NO_ZIP = "none";

    public BaseRequest(long timeoutMil, boolean isBackground) {
        this.timeoutMil = timeoutMil;
        this.isBackground = isBackground;
    }

    public BaseRequest() {

    }

    public BaseRequest(long timeoutMil, boolean isBackground, String onlyZip) {
        this.timeoutMil = timeoutMil;
        this.isBackground = isBackground;
        this.onlyZip = onlyZip;
    }


    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public long getTimeoutMil() {
        return timeoutMil;
    }

    public void setTimeoutMil(long timeoutMil) {
        this.timeoutMil = timeoutMil;
    }

    public boolean isBackground() {
        return isBackground;
    }

    public void setBackground(boolean background) {
        isBackground = background;
    }

    public String getOnlyZip() {
        return onlyZip;
    }

}
