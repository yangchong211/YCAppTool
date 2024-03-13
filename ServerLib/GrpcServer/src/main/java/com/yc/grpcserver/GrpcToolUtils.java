package com.yc.grpcserver;

import java.util.regex.Pattern;

public class GrpcToolUtils {

    /**
     * 判断是否是ip地址
     * @param input         ip地址
     * @return              ture表示是ip地址
     */
    public static boolean isIP(final CharSequence input) {
        //IP格式为四个三位数；且数字输入有严格限制。所以就要用到正则表达式判断。
        String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
        return isMatch(REGEX_IP, input);
    }

    private static boolean isMatch(final String regex, final CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }

}
