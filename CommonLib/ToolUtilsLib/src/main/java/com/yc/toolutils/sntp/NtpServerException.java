package com.yc.toolutils.sntp;

import java.io.IOException;
import java.util.Locale;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/5/11
 *     desc   : 自定义异常
 *     revise :
 * </pre>
 */
public class NtpServerException extends IOException {

    public final String property;
    public final float expectedValue;
    public final float actualValue;

    NtpServerException(String detailMessage) {
        super(detailMessage);
        this.property = "na";
        this.expectedValue = 0F;
        this.actualValue = 0F;
    }

    NtpServerException(String message, String property, float actualValue, float expectedValue) {
        super(String.format(Locale.getDefault(), message, property, actualValue, expectedValue));
        this.property = property;
        this.actualValue = actualValue;
        this.expectedValue = expectedValue;
    }
}
