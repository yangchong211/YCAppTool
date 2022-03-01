/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.ycbjie.webviewlib.utils;

import android.os.Build;
import android.text.Html;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : 工具类
 * </pre>
 */
public final class EncodeUtils {

    private EncodeUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 返回url编码的字符串
     * @param input                         url
     * @return                              编码的字符串
     */
    public static String urlEncode(final String input) {
        return urlEncode(input, "UTF-8");
    }

    /**
     * 返回url编码的字符串
     *
     * @param input                         url
     * @param charsetName                   字符集的名称
     * @return                              url编码后的字符串
     */
    public static String urlEncode(final String input, final String charsetName) {
        try {
            return URLEncoder.encode(input, charsetName);
        } catch (UnsupportedEncodingException e) {
            X5LogUtils.e("urlEncode"+e.getMessage());
        } catch (Exception e) {
            X5LogUtils.e("urlEncode"+e.getMessage());
        }
        return "";
    }

    /**
     * 返回解码的字符串
     *
     * @param input                         url
     * @return                              解码后的url字符串
     */
    public static String urlDecode(final String input) {
        return urlDecode(input, "UTF-8");
    }

    /**
     * 返回解码的字符串
     *
     * @param input                         url
     * @param charsetName                   字符集的名称
     * @return the string of decode urlencoded string
     */
    public static String urlDecode(final String input, final String charsetName) {
        try {
            return URLDecoder.decode(input, charsetName);
        } catch (UnsupportedEncodingException e) {
            X5LogUtils.e("urlDecode"+ e.getMessage());
        } catch (Exception e) {
            X5LogUtils.e("urlDecode"+e.getMessage());
        }
        return "";
    }

    /**
     * 返回base64编码字节
     *
     * @param input                         url
     * @return                              base64编码字节
     */
    public static byte[] base64Encode(final String input) {
        return base64Encode(input.getBytes());
    }

    /**
     * 返回base64编码字节
     *
     * @param input                         byte字节
     * @return                              base64编码字节
     */
    public static byte[] base64Encode(final byte[] input) {
        return Base64.encode(input, Base64.DEFAULT);
    }

    /**
     * 返回base64编码的字符串
     *
     * @param input                         byte字节
     * @return                              base64编码字符串
     */
    public static String base64Encode2String(final byte[] input) {
        return Base64.encodeToString(input, Base64.DEFAULT);
    }

    /**
     * 返回解码base64编码字符串的字节
     *
     * @param input                         url
     * @return                              解码base64编码的字节
     */
    public static byte[] base64Decode(final String input) {
        return Base64.decode(input, Base64.DEFAULT);
    }

    /**
     * 返回解码base64编码字符串的字节
     *
     * @param input                         字节
     * @return                              解码base64编码的字节
     */
    public static byte[] base64Decode(final byte[] input) {
        return Base64.decode(input, Base64.DEFAULT);
    }

    /**
     * 返回html编码字符串
     *
     * @param input                         字符串
     * @return                              html编码字符串
     */
    public static String htmlEncode(final CharSequence input) {
        StringBuilder sb = new StringBuilder();
        char c;
        for (int i = 0, len = input.length(); i < len; i++) {
            c = input.charAt(i);
            switch (c) {
                case '<':
                    sb.append("&lt;"); //$NON-NLS-1$
                    break;
                case '>':
                    sb.append("&gt;"); //$NON-NLS-1$
                    break;
                case '&':
                    sb.append("&amp;"); //$NON-NLS-1$
                    break;
                case '\'':
                    //http://www.w3.org/TR/xhtml1
                    // The named character reference &apos; (the apostrophe, U+0027) was
                    // introduced in XML 1.0 but does not appear in HTML. Authors should
                    // therefore use &#39; instead of &apos; to work as expected in HTML 4
                    // user agents.
                    sb.append("&#39;"); //$NON-NLS-1$
                    break;
                case '"':
                    sb.append("&quot;"); //$NON-NLS-1$
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 返回解码html编码的字符串
     *
     * @param input                             input
     * @return                                  解码html编码的字符串
     */
    @SuppressWarnings("deprecation")
    public static CharSequence htmlDecode(final String input) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(input);
        }
    }
}
