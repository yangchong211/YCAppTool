package com.yc.kernel.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/10/21
 *     desc  : 常量类
 *     revise:
 * </pre>
 */
public final class PlayerConstant {


    /**
     * 视频传入url为空
     */
    public static final int MEDIA_INFO_URL_NULL = -1;

    /**
     * 开始渲染视频画面
     */
    public static final int MEDIA_INFO_VIDEO_RENDERING_START = 3;

    /**
     * 缓冲开始
     */
    public static final int MEDIA_INFO_BUFFERING_START = 701;

    /**
     * 缓冲结束
     */
    public static final int MEDIA_INFO_BUFFERING_END = 702;

    /**
     * 视频旋转信息
     */
    public static final int MEDIA_INFO_VIDEO_ROTATION_CHANGED = 10001;

    /**
     * 通过注解限定类型
     * TYPE_IJK                 IjkPlayer，基于IjkPlayer封装播放器
     * TYPE_NATIVE              MediaPlayer，基于原生自带的播放器控件
     * TYPE_EXO                 基于谷歌视频播放器
     * TYPE_RTC                 基于RTC视频播放器
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface PlayerType {
        int TYPE_IJK = 1;
        int TYPE_NATIVE = 2;
        int TYPE_EXO = 3;
        int TYPE_RTC = 4;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ErrorType {
        //错误的链接
        int TYPE_SOURCE = 1;
        //解析异常
        int TYPE_PARSE = 2;
        //其他异常
        int TYPE_UNEXPECTED = 3;
    }

}
