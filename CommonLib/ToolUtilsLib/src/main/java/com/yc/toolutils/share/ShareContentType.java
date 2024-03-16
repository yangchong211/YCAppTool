package com.yc.toolutils.share;


import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2019/5/11
 *     desc   : 分享类型
 *     revise :
 *     GitHub : https://github.com/yangchong211/YCToolLib
 * </pre>
 */
@StringDef({ShareContentType.TEXT, ShareContentType.IMAGE,
        ShareContentType.AUDIO, ShareContentType.VIDEO, ShareContentType.FILE})
@Retention(RetentionPolicy.SOURCE)
public @interface ShareContentType {
    /**
     * Share Text
     */
    String TEXT = "text/plain";

    /**
     * Share Image
     */
    String IMAGE = "image/*";

    /**
     * Share Audio
     */
    String AUDIO = "audio/*";

    /**
     * Share Video
     */
    String VIDEO = "video/*";

    /**
     * Share File
     */
    String FILE = "*/*";
}
