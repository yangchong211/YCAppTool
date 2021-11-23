package com.yc.location.bean;

/**
 * 定位出错时，记录出错详细信息。
 */

public class ErrorInfo {

    /**
     * 无定位权限（定位开关关闭；GPS关闭；或者用户没有授予应用定位相关权限）
     */
    public static final int ERROR_LOCATION_PERMISSION = 101;
    /**
     * 获取不到定位依据：由于某种原因，而拿不到GPS信息，也拿不到wifi、基站信息（如定位开关中设置仅GPS不用网路定位或环境原因）。
     */
    public static final int ERROR_NO_ELEMENT_FOR_LOCATION = 103;
    /**
     * App无SDK使用权限（业务/APP注册时需要传入使用方信息，进行鉴权）
     */
    public static final int ERROR_APP_PERMISSION = 201;
    /**
     * 业务线模块无SDK使用权限（业务/APP注册时需要传入使用方信息，进行鉴权）
     */
    public static final int ERROR_MODULE_PERMISSION = 202;
    /**
     * 无网络连接。
     */
    public static final int ERROR_NETWORK_CONNECTION = 301;
    /**
     * 常规网络请求错误，如403，501等。具体网络请求失败错误码可以通过ErrInfo的getReponseCode（）获得。
     */
    public static final int ERROR_HTTP_REQUEST_NORMAL_ERR = 302;
    /**
     * 网络请求发生异常，如超时等。
     */
    public static final int ERROR_HTTP_REQUEST_EXCEPTION = 303;
    /**
     * 服务器没有返回位置信息。
     */
    public static final int ERROR_HTTP_REQUEST_NO_LOCATION_RETURN = 304;
    /**
     * 网络请求响应为空。
     */
    public static final int ERROR_HTTP_RESPONSE_NULL = 305;

    /**
     * 其他错误
     */
    public static final int ERROR_OTHERS = 1000;

    public static final int ERROR_OK                    = 0;
    /**
     * 腾讯定位错误：网络问题引起的定位失败。
     */
    public static final int ERROR_TENCENT_NETWORK               = 1;
    /**
     *腾讯定位错误：GPS, Wi-Fi 或基站错误引起的定位失败：
     1、用户的手机确实采集不到定位凭据，比如偏远地区比如地下车库电梯内等;
     2、开关跟权限问题，比如用户关闭了位置信息，关闭了wifi，未授予app定位权限等。
     */
    public static final int ERROR_TENCENT_BAD_JSON              = 2;
    /**
     *无法将WGS84坐标转换成GCJ-02坐标时的定位失败
     */
    public static final int ERROR_TENCENT_WGS84                 = 4;


    private int errNo = 0;
    private String errMessage = null;
    private int responseCode = 0;
    private String source;
    private String responseMessage = null;
    //发生错误的时间
    private long time;
    private long localTime = 0;
    //网络请求定位出现异常，异常信息
    private String requestExceptionMessage = null;

    //发生错误时间
    public long getTime() {
        return time;
    }
    //设置错误时间
    public void setTime(long time) {
        this.time = time;
    }

    public static final String SOURCE_DIDI = "didi";
    public static final String SOURCE_TENCENT = "tencent";

    public ErrorInfo(int errNo) {
        this.errNo = errNo;
    }

    public ErrorInfo() {

    }

    /**
     * 当出现网络请求错误时，返回网络请求返回的原始的错误说明信息
     * @return 网络请求请求错误信息
     */
    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    /**
     * 网络请求发生异常，异常信息(Http Exception)。用于统计分析。
     * @return 异常信息(Http Exception)。用于统计分析。
     */
    public String getRequestExceptionMessage() {
        return requestExceptionMessage;
    }

    public void setRequestExceptionMessage(String requestExceptionMessage) {
        this.requestExceptionMessage = requestExceptionMessage;
    }

    /**
     * 取得定位错误来源，也就是定位来源。
     * @return 定位错误来源。只有SDK内部切换为使用腾讯定位，来源才会是腾讯，否则都是滴滴。
     */
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    /**
     * 本错误信息对应的错误码。详见{@link DefaultLocation}
     * @return 错误码
     */
    public int getErrNo() {
        return errNo;
    }

    /**
     * 设置错误码。为了便于内部记录错误码。
     * @param errNo 错误码
     */
    public void setErrNo(int errNo) {
        this.errNo = errNo;
    }

    /**
    错误说明信息
     */
    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    /**
     * 当是网络定位出错时，返回网络请求的错误码。
     * @return 请求结果错误码
     */
    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public void setLocalTime(long localTime) {
        this.localTime = localTime;
    }

    public long getLocalTime() {
        return this.localTime;
    }
}
