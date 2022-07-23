package com.yc.apptextview;

import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;

import java.io.UnsupportedEncodingException;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/07/18
 *     desc  : 文本两端对齐的utils
 *     revise:
 * </pre>
 */
public final class TextViewUtils {

    /**
     * 功能：判断字符串是否有中文
     * @param str                       字符串
     * @return
     */
    public static boolean isCN(String str) {
        try {
            byte[] bytes = str.getBytes("UTF-8");
            if (bytes.length == str.length()) {
                return false;
            } else {
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 复制文本到剪切板
     * @param text                  文本
     */
    public static void copy(Context context, String text) {
        if (context == null){
            return;
        }
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        try {
            android.content.ClipData clip = android.content.ClipData.newPlainText(null, text);
            if (clipboard != null) {
                clipboard.setPrimaryClip(clip);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 判断是否包含标点符号等内容
     * @param s                         字符串
     * @return
     */
    public static boolean checkIsSymbol(String s) {
        boolean b = false;
        String tmp = s;
        tmp = tmp.replaceAll("\\p{P}", "");
        if (s.length() != tmp.length()) {
            b = true;
        }

        return b;
    }

    /**
     * dp转为px
     * @param var0                      上下文
     * @param var1                      值
     * @return
     */
    public static int dipToPx(Context var0, float var1) {
        float var2 = var0.getResources().getDisplayMetrics().density;
        return (int) (var1 * var2 + 0.5F);
    }


}
