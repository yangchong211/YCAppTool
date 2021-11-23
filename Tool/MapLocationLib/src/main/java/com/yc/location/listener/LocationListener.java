package com.yc.location.listener;

import com.yc.location.bean.ErrorInfo;
import com.yc.location.bean.DefaultLocation;

/**
 * 监听位置的listener。
 */
public interface LocationListener {

    /**
     * 获得位置回调函数
     * DIDILocation：获得的位置信息。
     */
    void onLocationChanged(DefaultLocation didiLocation);
    /**
     * 定位出错的回调函数。
     *  <p>
     *      只有在GPS，网络定位，NLP/腾讯兜底定位都拿不到位置情况下，才会认为定位错误（失败），返回错误码。
     *      注意：出错可能只是一个暂态，如临时进入到某个环境，暂时不能获取任何定位依据，但过了一会又可以定位了。业务方结合错误码及自己场景使用。
     *  </p>
     *  errNo：错误号{@link ErrorInfo}
     *        ERROR_LOCATION_PERMISSION：无定位权限（定位开关关闭；或者用户没有授予应用定位相关权限）
     *        ERROR_NO_ELEMENT_FOR_LOCATION：获取不到定位依据：由于某种原因，而拿不到GPS信息，也拿不到wifi、基站信息（如定位开关中设置仅GPS不用网路定位）。
     *        ERROR_APP_PERMISSION：App无SDK使用权限（业务/APP注册时需要传入使用方信息，进行鉴权）
     *        ERROR_MODULE_PERMISSION：业务线模块无SDK使用权限（业务/APP注册时需要传入使用方信息，进行鉴权）
     *        ERROR_NETWORK_CONNECTION：无网络连接。
     *        ERROR_HTTP_REQUEST_NORMAL_ERR：常规网络请求错误，如403，501等。具体网络请求失败错误码可以通过ErrInfo的getReponseCode（）获得。
     *        ERROR_HTTP_REQUEST_EXCEPTION：网络请求发生异常，如超时等。
     *        ERROR_HTTP_REQUEST_NO_LOCATION_RETURN：服务器没有返回位置信息。
     *        ERROR_OTHERS：未知错误。
     *
     *        以下错误只有在国外或切换成直接使用腾讯定位SDK时才会出现：
     *        ERROR_TENCENT_NETWORK：腾讯定位错误：网络问题引起的定位失败。
     *        ERROR_TENCENT_BAD_JSON：腾讯定位错误：GPS, Wi-Fi 或基站错误引起的定位失败：1、用户的手机确实采集不到定位凭据，比如偏远地区比如地下车库电梯内等;2、开关跟权限问题，比如用户关闭了位置信息，关闭了wifi，未授予app定位权限等。
     *        ERROR_TENCENT_WGS84：腾讯定位错误：无法将WGS84坐标转换成GCJ-02坐标时的定位失败
     *  errInfo：错误信息
     *         从ErrInfo可以获得：
     *         getErrCode() 即得到上面的错误码
     *         getErrMessage（）得到错误信息的字符串。
     *         getReponseCode（）当网络请求失败时获得网络响应的错误码。
     *         getResponseMessage（）当出现网络请求错误时，返回网络请求返回的原始的错误信息。
     *         getSource() 错误来自滴滴定位还是腾讯定位兜底。大部分错误都会归结到滴滴定位。
     *
     */
    void onLocationError(int errNo, ErrorInfo errInfo);

    /**
     * 手机GPS等状态变化时的回调函数。
     * Name：定位相关模块，取值{@link DefaultLocation} STATUS_CELL, STATUS_WIFI, STATUS_GPS.
     * Status：当前状态 {@link DefaultLocation}
     * Desc：状态描述
     */
    void onStatusUpdate(String name, int status, String desc);

}