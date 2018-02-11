package com.ns.yc.lifehelper.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2018/1/8
 * 描    述：正则表达式工具类
 * 修订历史：
 * ================================================
 */
public class RegularUtils {

    //判断手机格式是否正确
    public static boolean isMobile(String mobiles) {
        Pattern p = compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }


}
