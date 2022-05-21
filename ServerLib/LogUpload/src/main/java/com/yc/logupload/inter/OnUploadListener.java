package com.yc.logupload.inter;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/4/17
 *     desc   : 上传监听listener
 *     revise :
 * </pre>
 */
public interface OnUploadListener {

    /**
     * 上传成功回调
     */
    void onSuccess();

    /**
     * 上传失败回调
     * @param error     error信息
     */
    void onError(String error);

}
